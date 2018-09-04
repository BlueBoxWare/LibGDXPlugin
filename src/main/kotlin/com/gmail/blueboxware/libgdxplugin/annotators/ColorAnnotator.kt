package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.psi.impl.compiled.ClsElementImpl
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.siyeh.ig.psiutils.MethodCallUtils
import org.jetbrains.kotlin.asJava.elements.KtLightElement
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.idea.imports.getImportableTargets
import org.jetbrains.kotlin.idea.intentions.callExpression
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.refactoring.getLineNumber
import org.jetbrains.kotlin.idea.references.AbstractKtReference
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import java.awt.Color

/*
 * Copyright 2016 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class ColorAnnotator: Annotator {

  private val ANNOTATIONS_KEY = key<MutableList<Pair<Int, Color>>>("annotations")
  private val CACHE_KEY = key<ColorAnnotatorCache>("cache")

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if (holder.currentAnnotationSession.getUserData(CACHE_KEY) == null) {
      holder.currentAnnotationSession.putUserData(CACHE_KEY, ColorAnnotatorCache(element.project))
    }

    holder.currentAnnotationSession.getUserData(CACHE_KEY)?.let { cache ->

      if (cache.colorAnnotationsEnabled) {
        getColor(cache, element)?.let { color ->
          annotateWithColor(color, element, holder)
        }
      }

    }

  }

  private fun annotateWithColor(color: Color, element: PsiElement, holder: AnnotationHolder) {

    val annotationSessions = holder.currentAnnotationSession

    if (annotationSessions.getUserData(ANNOTATIONS_KEY) == null) {
      annotationSessions.putUserData(ANNOTATIONS_KEY, mutableListOf())
    }

    annotationSessions.getUserData(ANNOTATIONS_KEY)?.let { currentAnnotations ->
      for ((first, second) in currentAnnotations) {
        if (first == element.getLineNumber() && second == color) {
          return
        }
      }

      currentAnnotations.add(element.getLineNumber() to color)
    }

    if (ApplicationManager.getApplication()?.isUnitTestMode == true) {
      val msg = String.format("#%02x%02x%02x%02x", color.red, color.green, color.blue, color.alpha)
      holder.createWeakWarningAnnotation(element, msg)
    } else {
      val annotation = holder.createInfoAnnotation(element, null)
      annotation.gutterIconRenderer = GutterColorRenderer(color)
    }

  }

  internal fun getColor(cache: ColorAnnotatorCache, element: PsiElement, ignoreContext: Boolean = false): Color? {

    val isSpecialColorMethod =
            if (element is KtCallExpression || element is KtDotQualifiedExpression || element is PsiMethodCallExpression) isSpecialColorMethod(cache, element) else false

    if (!ignoreContext) {
      if (element.context is PsiMethodCallExpression
              || element.context is KtCallExpression
              || element.context is PsiReferenceExpression
              || element.context is KtNameReferenceExpression
              || element.context is KtDotQualifiedExpression
      ) {
        if (!isSpecialColorMethod) {
          return null
        }
      }
    }

    if (cache.colorCache.containsKey(element)) {
      return cache.colorCache[element]
    }

    val color = findColor(cache, element, isSpecialColorMethod)

    cache.colorCache[element] = color

    return color
  }

  private fun findColor(cache: ColorAnnotatorCache, element: PsiElement, isSpecialColorMethod: Boolean): Color? {

    val type = if (element is KtExpression) {
     element.getType(element.analyzePartial())?.fqName()
    } else if (element is PsiExpression) {
      element.type?.getCanonicalText(false)
    } else {
      null
    }

    if (type != COLOR_CLASS_NAME
      && type != "java.lang.String"
      && type != "kotlin.String"
      && !isSpecialColorMethod
    ) {
      return null
    }

    val initialValue = getRoot(cache, element)

    if (initialValue is KtStringTemplateExpression) {

      if (initialValue.text.startsWith("\"#")) {
        return color(initialValue.text)
      }

    } else if (initialValue is PsiLiteralExpression) {
      if (initialValue.type.isStringType(element)) {
        if (initialValue.text.startsWith("\"#")) {
          return color(initialValue.text)
        }
      }

    }

    if (type != COLOR_CLASS_NAME && !isSpecialColorMethod) return null

    if (initialValue is KtCallExpression) {

      var isColorCall = isSpecialColorMethod

      if (!isColorCall) {
        val references = initialValue.calleeExpression?.references ?: return null

        for (reference in references) {
          val targetName = (resolve(cache, reference) as? PsiMethod)?.getKotlinFqName()?.asString() ?: continue
          if (targetName == "com.badlogic.gdx.graphics.Color.Color"
                  || targetName == "com.badlogic.gdx.graphics.Color.valueOf"
                  || targetName == "com.badlogic.gdx.scenes.scene2d.ui.Skin.getColor"
                  || targetName == "com.badlogic.gdx.scenes.scene2d.ui.Skin.get"
                  || targetName == "com.badlogic.gdx.scenes.scene2d.ui.Skin.optional"
                  || targetName == "com.badlogic.gdx.graphics.Colors.get"
                  || targetName == "com.badlogic.gdx.graphics.Colors.put"
                  || targetName == "com.badlogic.gdx.utils.ObjectMap.get"
          ) {
            isColorCall = true
          }
        }

        if (!isColorCall) return null
      }

      val arguments = initialValue.valueArguments
      val argument1type = arguments.firstOrNull()?.getArgumentExpression()?.getType(initialValue.analyzePartial()) ?: return null

      if (arguments.size == 1 && KotlinBuiltIns.isInt(argument1type)) {
        // Color(int)
        arguments.firstOrNull()?.getArgumentExpression()?.let { expr ->
          val arg = getRoot(cache, expr)
          ktInt(arg)?.let { int ->
            return rgbaToColor(int.toLong())
          }
        }
      } else if (KotlinBuiltIns.isString(argument1type)) {
        initialValue.resolveCallToStrings()?.let { resolvedCall ->
          arguments.firstOrNull()?.getArgumentExpression()?.let { expr ->
            val arg = getRoot(cache, expr)
            if (arg is KtStringTemplateExpression || arg is PsiLiteralExpression) {
              if (resolvedCall.second == "valueOf") {
                // Color.valueOf(String)
                return color(arg.text)
              } else if (resolvedCall.second == "getColor") {
                // Skin.getColor(String)
                val resourceName = StringUtil.unquoteString(arg.text)
                initialValue.getAssetFiles().let { (skinFiles) ->
                  for (skinFile in skinFiles) {
                    skinFile.getResources(COLOR_CLASS_NAME, resourceName).firstOrNull()?.let {
                      return it.asColor(true)
                    }
                  }
                }
              } else if (resolvedCall.first == COLORS_CLASS_NAME && resolvedCall.second == "get") {
                // Colors.get(String)
                ((arg as? PsiLiteralExpression)?.asString()
                        ?: (arg as? KtStringTemplateExpression)?.plainContent)?.let { str ->
                  return initialValue.project.getColorsMap()[str]?.valueElement?.let { getColor(cache, it) }
                }
              } else if (resolvedCall.first == OBJECT_MAP_CLASS_NAME && resolvedCall.second == "get") {
                // Colors.getColors.get(String)
                ((initialValue.parent as? KtDotQualifiedExpression)?.receiverExpression as? KtDotQualifiedExpression)?.resolveCallToStrings()?.let { (clazz, method) ->
                  if (clazz == COLORS_CLASS_NAME && method == "getColors") {
                    ((arg as? PsiLiteralExpression)?.asString()
                            ?: (arg as? KtStringTemplateExpression)?.plainContent)?.let { str ->
                      return initialValue.project.getColorsMap()[str]?.valueElement?.let { getColor(cache, it) }
                    }
                  }
                }
              } else if ((resolvedCall.second == "get" || resolvedCall.second == "optional") && arguments.size == 2) {
                ((initialValue.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression as? KtClassLiteralExpression)?.let { classLiteralExpression ->
                  (classLiteralExpression.receiverExpression as? KtReferenceExpression
                          ?: (classLiteralExpression.receiverExpression as? KtDotQualifiedExpression)?.selectorExpression as? KtReferenceExpression)
                          ?.getImportableTargets(initialValue.analyzePartial())
                          ?.firstOrNull()
                          ?.let { clazz ->
                    element.findClass(clazz.fqNameSafe.asString())?.let { psiClass ->
                      if (psiClass.qualifiedName == COLOR_CLASS_NAME) {
                        // Skin.get(string, Color::class.java)
                        val resourceName = StringUtil.unquoteString(arg.text)
                        initialValue.getAssetFiles().let { (skinFiles) ->
                          for (skinFile in skinFiles) {
                            skinFile.getResources(COLOR_CLASS_NAME, resourceName).firstOrNull()?.let {
                              return it.asColor(true)
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      } else if (arguments.size == 1) {
        // Color(Color)
        arguments.firstOrNull()?.getArgumentExpression()?.let { expr ->
          val arg = getRoot(cache, expr)
          return getColor(cache, arg)
        }
      } else if (arguments.size == 4) {
        // Color(float, float, float, float)
        val floats = arrayOf(0f, 0f, 0f, 0f)
        val context = initialValue.analyzePartial()
        for (i in 0 .. 3) {
          arguments.getOrNull(i)?.getArgumentExpression()?.let { expr ->
            val argType = expr.getType(context)
            if (argType == null || !KotlinBuiltIns.isFloat(argType)) return null
            val root = getRoot(cache, expr)
            val float = psiFloat(cache, root) ?: return null
            floats[i] = float
          }
        }
        return color(floats[0], floats[1], floats[2], floats[3])
      }

    } else if (initialValue is PsiCall) {

      val arguments = initialValue.argumentList?.expressions ?: return null
      val types = initialValue.argumentList?.expressionTypes ?: return null
      val argument1type = types.firstOrNull() ?: return null

      if (arguments.size == 1 && argument1type == PsiType.INT) {
        // new Color(int)
        arguments.firstOrNull()?.let { expr ->
          val arg = getRoot(cache, expr)
          javaInt(arg)?.let { int ->
            return rgbaToColor(int)
          }
        }
      } else if (argument1type.isStringType(element)) {
        arguments.firstOrNull()?.let { expr ->
          val arg = getRoot(cache, expr)
          if (arg is KtStringTemplateExpression || arg is PsiLiteralExpression) {
            (initialValue as? PsiMethodCallExpression)?.let { methodCallExpression ->
              methodCallExpression.resolveCallToStrings()?.let { resolved ->
                if (resolved.second == "valueOf") {
                  // Color.valueOf(String)
                  return color(arg.text)
                } else if (resolved.second == "getColor") {
                  // Skin.getColor(String)
                  methodCallExpression.getAssetFiles().let { (skinFiles) ->
                    for (skinFile in skinFiles) {
                      skinFile.getResources(COLOR_CLASS_NAME, StringUtil.unquoteString(arg.text)).firstOrNull()?.let {
                        return it.asColor(true)
                      }
                    }
                  }
                } else if (resolved.first == COLORS_CLASS_NAME && resolved.second == "get") {
                  // Colors.get(String)
                  ((arg as? PsiLiteralExpression)?.asString()
                          ?: (arg as? KtStringTemplateExpression)?.plainContent)?.let { str ->
                    return initialValue.project.getColorsMap()[str]?.valueElement?.let { getColor(cache, it) }
                  }
                } else if (resolved.first == OBJECT_MAP_CLASS_NAME && resolved.second == "get") {
                  // Colors.getColors().get(String)
                  (initialValue as? PsiMethodCallExpression)?.let { methodCall ->
                    MethodCallUtils.getQualifierMethodCall(methodCall)?.resolveCallToStrings()?.let { (clazz, method) ->
                      if (clazz == COLORS_CLASS_NAME && method == "getColors") {
                        ((arg as? PsiLiteralExpression)?.asString()
                                ?: (arg as? KtStringTemplateExpression)?.plainContent)?.let { str ->
                          return initialValue.project.getColorsMap()[str]?.valueElement?.let { getColor(cache, it) }
                        }
                      }
                    }
                  }
                } else if ((resolved.second == "get" || resolved.second == "optional") && arguments.size == 2) {
                  methodCallExpression.getAssetFiles().let { (skinFiles) ->
                    for (skinFile in skinFiles) {
                      skinFile.getResources(COLOR_CLASS_NAME, StringUtil.unquoteString(arg.text)).firstOrNull()?.let {
                        return it.asColor(true)
                      }
                    }
                  }
                }

                Unit
              }
            }
          }
        }

      } else if (arguments.size == 1) {
        // new Color(Color)
        arguments.firstOrNull()?.let { expr ->
          val arg = getRoot(cache, expr)
          return getColor(cache, arg)
        }
      } else if (arguments.size == 4) {
        // new Color(float, float, float, float)
        val floats = arrayOf(0f, 0f, 0f, 0f)
        for (i in 0 .. 3) {
          arguments[i]?.let { expr ->
            if (expr.type == PsiType.FLOAT) {
              val root = getRoot(cache, expr)
              val float = psiFloat(cache, root) ?: return null
              floats[i] = float
            } else if (expr.type == PsiType.INT) {
              val arg = getRoot(cache, expr)
              val int = javaInt(arg) ?: return null
              floats[i] = int.toFloat()
            } else {
              return null
            }
          }
        }
        return color(floats[0], floats[1], floats[2], floats[3])
      }

    }

    return null
  }

  private fun isSpecialColorMethod(cache: ColorAnnotatorCache, element: PsiElement): Boolean {


    if (element is KtElement) {

      val actualElement = (element as? KtCallExpression) ?: ((element as? KtDotQualifiedExpression)?.selectorExpression as? KtCallExpression)

      actualElement?.calleeExpression?.references?.let { references ->
        for (reference in references) {
          resolve(cache, reference)?.getKotlinFqName()?.asString()?.let { fqName ->
            if (fqName in COLOR_METHODS) {
              return true
            }
          }
        }
      }

    } else if (element is PsiMethodCallExpression) {

      element.resolveMethod()?.getKotlinFqName()?.asString()?.let { fqName ->
        if (fqName in COLOR_METHODS) {
          return true
        }
      }

    }

    return false

  }

  private fun getRoot(cache: ColorAnnotatorCache, element: PsiElement): PsiElement {

    if (cache.rootCache.containsKey(element)) {
      cache.rootCache[element]?.let { return it }
    }

    val root = findRoot(cache, element)

    cache.rootCache[element] = root

    return root

  }

  /*
   *
   * Finds the root in a chain of references
   *
   */
  private fun findRoot(cache: ColorAnnotatorCache, element: PsiElement): PsiElement {

    if (element is KtNameReferenceExpression || element is PsiReferenceExpression) {

      for (reference in element.references) {

        if (reference is KtSimpleNameReference || reference is PsiReferenceExpression) {

          resolve(cache, reference)?.let { origin ->
            getInitializer(cache, origin)?.let { initializer ->
              return getRoot(cache, initializer)
            }
          }

        }

      }

    } else if (element is KtDotQualifiedExpression) {

      element.selectorExpression?.let { selector ->
        return getRoot(cache, selector)
      }

    } else if (element is KtQualifiedExpression) {

      element.callExpression?.let { callExpression ->
        return getRoot(cache, callExpression)
      }

    } else if (element is PsiMethodCallExpression) {

      element.resolveMethod()?.let { resolved ->
        if (resolved is KtLightMethod) {
          getInitializer(cache, resolved)?.let {
            return it
          }
        }
      }

    }

    return element

  }

  private fun getInitializer(cache: ColorAnnotatorCache, element: PsiElement): PsiElement? {

    var origin = (element as? ClsElementImpl)?.navigationElement ?: element

    if (origin is KtLightElement<*, *>) {
      origin = origin.navigationElement
    }

    if (origin is ClsElementImpl) {
      origin = origin.mirror ?: origin
    }

    if (origin is PsiField) {
      if (origin.modifierList?.hasModifierProperty(PsiModifier.FINAL) == true) {
        (origin.navigationElement as? PsiField)?.initializer?.let { initializer ->
          return getRoot(cache, initializer)
        }
      }
    } else if (origin is PsiVariable) {
      if (origin.modifierList?.hasModifierProperty(PsiModifier.FINAL) == true) {
        origin.initializer?.let { initializer ->
          return getRoot(cache, initializer)
        }
      }
    } else if (origin is KtProperty && !origin.isVar) {
      origin.initializer?.let { initializer ->
        return getRoot(cache, initializer)
      }
    }


    return null

  }

  private fun ktInt(expr: PsiElement): Int? {

    if (expr is KtConstantExpression) {

      expr.getType(expr.analyzePartial())?.let { type ->

        if (KotlinBuiltIns.isInt(type)) {

          return try {
            expr.text.toInt()
          } catch (e: NumberFormatException) {
            null
          }

        }

      }

    }

    return null
  }

  private fun javaInt(expr: PsiElement): Long? {

    if (expr is PsiExpression && expr.type == PsiType.INT) {

      try {
        if (expr.text.startsWith("0x")) {
          return java.lang.Long.valueOf(expr.text.substring(2), 16)
        } else {
          return java.lang.Long.valueOf(expr.text)
        }
      } catch (e: NumberFormatException) {

      }

    }

    return null
  }


  private fun psiFloat(cache: ColorAnnotatorCache, expr: PsiElement): Float? {

    val context = expr.context

    if (context is KtDotQualifiedExpression) {
      if (context.receiverExpression.getType(context.analyzePartial())?.fqName() == COLOR_CLASS_NAME) {
        getColor(cache, context.receiverExpression, ignoreContext = true)?.let { color ->
          return when(context.selectorExpression?.text) {
            "r" -> color.red /  255f
            "g" -> color.green / 255f
            "b" -> color.blue / 255f
            "a" -> color.alpha / 255f
            else -> null
          }
        }
      }

    } else if (expr is PsiReferenceExpression) {
      if ((expr.qualifierExpression?.type as? PsiClassReferenceType)?.canonicalText == COLOR_CLASS_NAME) {
        expr.qualifierExpression?.let { qualifierExpr ->
          getColor(cache, qualifierExpr, ignoreContext = true)?.let { color ->
            return when(expr.referenceName) {
              "r" -> color.red /  255f
              "g" -> color.green / 255f
              "b" -> color.blue / 255f
              "a" -> color.alpha / 255f
              else -> null
            }
          }
        }
      }
    }

    val arg = getRoot(cache, expr)

    if (arg is PsiLiteralExpression || arg is KtConstantExpression) {

      try {
        return arg.text.toFloat()
      } catch (e: NumberFormatException) {

      }

    }

    return null

  }

  private fun resolve(cache: ColorAnnotatorCache, reference: PsiReference): PsiElement? {

    if (cache.resolveCache.containsKey(reference)) {
      return cache.resolveCache[reference]
    }

    var origin: PsiElement? = null

    if (reference is AbstractKtReference<*>) {

      val results = reference.multiResolve(false)

      for (result in results) {

        val file = result.element?.containingFile
        if (file is KtFile && !file.isCompiled) {
          origin = result.element
        }

      }

      if (origin == null) {
        origin = results.firstOrNull()?.element
      }

    } else {

      origin = reference.resolve()

    }

    cache.resolveCache[reference] = origin

    return origin

  }

  private fun rgbaToColor(value: Long): Color? {
    val r = (value and 0xff000000).ushr(24) / 255f
    val g = (value and 0x00ff0000).ushr(16) / 255f
    val b = (value and 0x0000ff00).ushr(8) / 255f
    val a = (value and 0x000000ff) / 255f
    return color(r, g, b, a)
  }

  private val COLOR_METHODS = listOf(
          "com.badlogic.gdx.graphics.GL20.glClearColor",
          "com.badlogic.gdx.graphics.GL20.glBlendColor",
          "com.badlogic.gdx.graphics.GL30.glClearColor",
          "com.badlogic.gdx.graphics.GL30.glBlendColor",
          "com.badlogic.gdx.graphics.g2d.SpriteBatch.setColor",
          "com.badlogic.gdx.graphics.g2d.Batch.setColor",
          "com.badlogic.gdx.graphics.g2d.CpuSpriteBatch.setColor",
          "com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch.setColor",
          "com.badlogic.gdx.graphics.Color.set",
          "com.badlogic.gdx.graphics.Colors.get",
          "com.badlogic.gdx.graphics.Colors.put",
          "com.badlogic.gdx.graphics.Colors.getColors",
          "com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo.setCol",
          "com.badlogic.gdx.graphics.g2d.BitmapFont.setColor",
          "com.badlogic.gdx.graphics.g2d.BitmapFontCache.setColor",
          "com.badlogic.gdx.graphics.g2d.BitmapFontCache.setColors",
          "com.badlogic.gdx.graphics.g2d.PolygonSprite.setColor",
          "com.badlogic.gdx.graphics.g2d.Sprite.setColor",
          "com.badlogic.gdx.graphics.g2d.ParticleEmitter.Particle.setColor",
          "com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite.setColor",
          "com.badlogic.gdx.graphics.g2d.SpriteCache.setColor",
          "com.badlogic.gdx.graphics.g3d.environment.BaseLight.setColor",
          "com.badlogic.gdx.graphics.g3d.environment.DirectionalLight.setColor",
          "com.badlogic.gdx.graphics.g3d.environment.PointLight.setColor",
          "com.badlogic.gdx.graphics.g3d.environment.SpotLight.setColor",
          "com.badlogic.gdx.graphics.g3d.utils.MeshBuilder.setColor",
          "com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.setColor",
          "com.badlogic.gdx.graphics.glutils.ShapeRenderer.setColor",
          "com.badlogic.gdx.graphics.Pixmap.setColor",
          "com.badlogic.gdx.scenes.scene2d.Actor.setColor",
          "com.badlogic.gdx.scenes.scene2d.Group.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Widget.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Image.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Label.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.List.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Slider.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.SelectBox.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.TextField.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.TextArea.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Touchpad.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Container.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.SplitPane.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Stack.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Table.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Button.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.ImageButton.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.TextButton.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.CheckBox.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Window.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Dialog.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.Tree.setColor",
          "com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup.setColor",
          "com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer.color",
          "com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20.color",
          "com.badlogic.gdx.utils.ObjectMap.get"
  )


}

internal class ColorAnnotatorCache(project: Project) {

  val colorCache = mutableMapOf<PsiElement, Color?>()
  val rootCache = mutableMapOf<PsiElement, PsiElement?>()
  val resolveCache = mutableMapOf<PsiReference, PsiElement?>()

  val colorAnnotationsEnabled =
          project.isLibGDXProject() && (ServiceManager.getService(project, LibGDXPluginSettings::class.java)?.enableColorAnnotations == true)

}