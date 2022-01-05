/*
 * Copyright 2018 Blue Box Ware
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
package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.psi.impl.compiled.ClsElementImpl
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.util.CachedValue
import com.siyeh.ig.psiutils.MethodCallUtils
import org.jetbrains.kotlin.asJava.elements.KtLightElement
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.idea.imports.getImportableTargets
import org.jetbrains.kotlin.idea.intentions.callExpression
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.references.AbstractKtReference
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import java.awt.Color

private val COLOR_KEY = key<CachedValue<Color?>>("color.color")
private val COLOR_ROOT_KEY = key<CachedValue<PsiElement>>("color.root")

internal fun PsiElement.getColor(ignoreContext: Boolean = false): Color? {

    val isSpecialColorMethod =
        if (this is KtCallExpression || this is KtDotQualifiedExpression || this is PsiMethodCallExpression) isSpecialColorMethod() else false

    if (!ignoreContext) {
        if (context is PsiMethodCallExpression
            || context is KtCallExpression
            || context is PsiReferenceExpression
            || context is KtNameReferenceExpression
            || context is KtDotQualifiedExpression
        ) {
            if (!isSpecialColorMethod) {
                return null
            }
        }
    }

    return findColor(isSpecialColorMethod)

}

private fun PsiElement.findColor(isSpecialColorMethod: Boolean): Color? = getCachedValue(COLOR_KEY) {

    val type = when (this) {
        is KtExpression -> getType(analyzePartial())?.fqName()
        is PsiExpression -> type?.getCanonicalText(false)
        else -> null
    }

    if (type != COLOR_CLASS_NAME
        && type != "java.lang.String"
        && type != "kotlin.String"
        && !isSpecialColorMethod
    ) {
        return@getCachedValue null
    }

    val initialValue = findRoot()

    if (initialValue is KtStringTemplateExpression) {

        if (initialValue.text.startsWith("\"#")) {
            return@getCachedValue color(initialValue.text)
        }

    } else if (initialValue is PsiLiteralExpression) {
        if (initialValue.type.isStringType(this)) {
            if (initialValue.text.startsWith("\"#")) {
                return@getCachedValue color(initialValue.text)
            }
        }

    }

    if (type != COLOR_CLASS_NAME && !isSpecialColorMethod) return@getCachedValue null

    if (initialValue is KtCallExpression) {

        var isColorCall = isSpecialColorMethod

        if (!isColorCall) {
            val references = initialValue.calleeExpression?.references ?: return@getCachedValue null

            for (reference in references) {
                val targetName = (reference.resolveForColor() as? PsiMethod)?.getKotlinFqName()?.asString() ?: continue
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

            if (!isColorCall) return@getCachedValue null
        }

        val arguments = initialValue.valueArguments
        val argument1type = arguments.firstOrNull()?.getArgumentExpression()?.getType(initialValue.analyzePartial())
            ?: return@getCachedValue null

        if (arguments.size == 1 && KotlinBuiltIns.isInt(argument1type)) {
            // Color(int)
            arguments.firstOrNull()?.getArgumentExpression()?.let { expr ->
                val arg = expr.findRoot()
                arg.ktInt()?.let { int ->
                    return@getCachedValue rgbaToColor(int.toLong())
                }
            }
        } else if (KotlinBuiltIns.isString(argument1type)) {
            initialValue.resolveCallToStrings()?.let { (clazz, method) ->
                arguments.firstOrNull()?.getArgumentExpression()?.let { expr ->
                    val arg = expr.findRoot()
                    if (arg is KtStringTemplateExpression || arg is PsiLiteralExpression) {
                        if (method == "valueOf") {
                            // Color.valueOf(String)
                            return@getCachedValue color(arg.text)
                        } else if (method == "getColor") {
                            // Skin.getColor(String)
                            val resourceName = StringUtil.unquoteString(arg.text)
                            initialValue.getAssetFiles().let { (skinFiles, _) ->
                                for (skinFile in skinFiles) {
                                    skinFile.getResources(COLOR_CLASS_NAME, resourceName).firstOrNull()?.let {
                                        return@getCachedValue it.asColor(true)
                                    }
                                }
                            }
                        } else if (clazz == COLORS_CLASS_NAME && method == "get") {
                            // Colors.get(String)
                            ((arg as? PsiLiteralExpression)?.asString()
                                ?: (arg as? KtStringTemplateExpression)?.asPlainString())?.let { str ->
                                return@getCachedValue initialValue.project.getColorsMap()[str]?.valueElement?.getColor()
                            }
                        } else if (clazz == OBJECT_MAP_CLASS_NAME && method == "get") {
                            // Colors.getColors.get(String)
                            ((initialValue.parent as? KtDotQualifiedExpression)?.receiverExpression as? KtDotQualifiedExpression)
                                ?.resolveCallToStrings()
                                ?.let { (clazz, method) ->
                                    if (clazz == COLORS_CLASS_NAME && method == "getColors") {
                                        ((arg as? PsiLiteralExpression)?.asString()
                                            ?: (arg as? KtStringTemplateExpression)?.asPlainString())?.let { str ->
                                            return@getCachedValue initialValue.project.getColorsMap()[str]?.valueElement?.getColor()
                                        }
                                    }
                                }
                        } else if ((method == "get" || method == "optional") && arguments.size == 2) {
                            ((initialValue.valueArguments.getOrNull(1)
                                ?.getArgumentExpression() as? KtDotQualifiedExpression)
                                ?.receiverExpression as? KtClassLiteralExpression)
                                ?.let { classLiteralExpression ->
                                    (classLiteralExpression.receiverExpression as? KtReferenceExpression
                                        ?: (classLiteralExpression.receiverExpression as? KtDotQualifiedExpression)?.selectorExpression as? KtReferenceExpression)
                                        ?.getImportableTargets(initialValue.analyzePartial())
                                        ?.firstOrNull()
                                        ?.let { clazz ->
                                            findClass(clazz.fqNameSafe.asString())?.let { psiClass ->
                                                if (psiClass.qualifiedName == COLOR_CLASS_NAME) {
                                                    // Skin.get(string, Color::class.java)
                                                    val resourceName = StringUtil.unquoteString(arg.text)
                                                    initialValue.getAssetFiles().let { (skinFiles, _) ->
                                                        for (skinFile in skinFiles) {
                                                            skinFile.getResources(COLOR_CLASS_NAME, resourceName)
                                                                .firstOrNull()?.let {
                                                                    return@getCachedValue it.asColor(true)
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
                return@getCachedValue expr.findRoot().getColor()
            }
        } else if (arguments.size == 4) {
            // Color(float, float, float, float)
            val floats = arrayOf(0f, 0f, 0f, 0f)
            val context = initialValue.analyzePartial()
            for (i in 0..3) {
                arguments.getOrNull(i)?.getArgumentExpression()?.let { expr ->
                    val argType = expr.getType(context)
                    if (argType == null || !KotlinBuiltIns.isFloat(argType)) return@getCachedValue null
                    val root = expr.findRoot()
                    val float = root.psiFloat() ?: return@getCachedValue null
                    floats[i] = float
                }
            }
            return@getCachedValue color(floats[0], floats[1], floats[2], floats[3])
        }

    } else if (initialValue is PsiCall) {

        val arguments = initialValue.argumentList?.expressions ?: return@getCachedValue null
        val types = initialValue.argumentList?.expressionTypes ?: return@getCachedValue null
        val argument1type = types.firstOrNull() ?: return@getCachedValue null

        if (arguments.size == 1 && argument1type == PsiType.INT) {
            // new Color(int)
            arguments.firstOrNull()?.let { expr ->
                val arg = expr.findRoot()
                arg.javaInt()?.let { int ->
                    return@getCachedValue rgbaToColor(int)
                }
            }
        } else if (argument1type.isStringType(this)) {
            arguments.firstOrNull()?.let { expr ->
                val arg = expr.findRoot()
                if (arg is KtStringTemplateExpression || arg is PsiLiteralExpression) {
                    (initialValue as? PsiMethodCallExpression)?.let { methodCallExpression ->
                        methodCallExpression.resolveCallToStrings()?.let { (clazz, method) ->
                            if (method == "valueOf") {
                                // Color.valueOf(String)
                                return@getCachedValue color(arg.text)
                            } else if (method == "getColor") {
                                // Skin.getColor(String)
                                methodCallExpression.getAssetFiles().let { (skinFiles, _) ->
                                    for (skinFile in skinFiles) {
                                        skinFile.getResources(COLOR_CLASS_NAME, StringUtil.unquoteString(arg.text))
                                            .firstOrNull()?.let {
                                                return@getCachedValue it.asColor(true)
                                            }
                                    }
                                }
                            } else if (clazz == COLORS_CLASS_NAME && method == "get") {
                                // Colors.get(String)
                                ((arg as? PsiLiteralExpression)?.asString()
                                    ?: (arg as? KtStringTemplateExpression)?.asPlainString())?.let { str ->
                                    return@getCachedValue initialValue.project.getColorsMap()[str]?.valueElement?.getColor()
                                }
                            } else if (clazz == OBJECT_MAP_CLASS_NAME && method == "get") {
                                // Colors.getColors().get(String)
                                (initialValue as? PsiMethodCallExpression)?.let { methodCall ->
                                    MethodCallUtils.getQualifierMethodCall(methodCall)?.resolveCallToStrings()
                                        ?.let { (clazz, method) ->
                                            if (clazz == COLORS_CLASS_NAME && method == "getColors") {
                                                ((arg as? PsiLiteralExpression)?.asString()
                                                    ?: (arg as? KtStringTemplateExpression)?.asPlainString())?.let { str ->
                                                    return@getCachedValue initialValue.project.getColorsMap()[str]?.valueElement?.getColor()
                                                }
                                            }
                                        }
                                }
                            } else if ((method == "get" || method == "optional") && arguments.size == 2) {
                                methodCallExpression.getAssetFiles().let { (skinFiles, _) ->
                                    for (skinFile in skinFiles) {
                                        skinFile.getResources(COLOR_CLASS_NAME, StringUtil.unquoteString(arg.text))
                                            .firstOrNull()?.let {
                                                return@getCachedValue it.asColor(true)
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
                return@getCachedValue expr.findRoot().getColor()
            }
        } else if (arguments.size == 4) {
            // new Color(float, float, float, float)
            val floats = arrayOf(0f, 0f, 0f, 0f)
            for (i in 0..3) {
                arguments[i]?.let { expr ->
                    @Suppress("CascadeIf")
                    if (expr.type == PsiType.FLOAT) {
                        val root = expr.findRoot()
                        val float = root.psiFloat() ?: return@getCachedValue null
                        floats[i] = float
                    } else if (expr.type == PsiType.INT) {
                        val arg = expr.findRoot()
                        val int = arg.javaInt() ?: return@getCachedValue null
                        floats[i] = int.toFloat()
                    } else {
                        return@getCachedValue null
                    }
                }
            }
            return@getCachedValue color(floats[0], floats[1], floats[2], floats[3])
        }

    }

    return@getCachedValue null
}

private fun PsiElement.isSpecialColorMethod(): Boolean {


    if (this is KtElement) {

        val actualElement = (this as? KtCallExpression)
            ?: ((this as? KtDotQualifiedExpression)?.selectorExpression as? KtCallExpression)

        actualElement?.calleeExpression?.references?.let { references ->
            for (reference in references) {
                reference.resolveForColor()?.getKotlinFqName()?.asString()?.let { fqName ->
                    if (fqName in COLOR_METHODS) {
                        return true
                    }
                }
            }
        }

    } else if (this is PsiMethodCallExpression) {

        resolveMethod()?.getKotlinFqName()?.asString()?.let { fqName ->
            if (fqName in COLOR_METHODS) {
                return true
            }
        }

    }

    return false

}

private fun PsiReference.resolveForColor(): PsiElement? {

    var origin: PsiElement? = null

    if (this is AbstractKtReference<*>) {

        val results = multiResolve(false)

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

        origin = resolve()

    }

    return origin

}

private fun PsiElement.findRoot(): PsiElement = getCachedValue(COLOR_ROOT_KEY) {

    if (this is KtNameReferenceExpression || this is PsiReferenceExpression) {

        for (reference in references) {

            if (reference is KtSimpleNameReference || reference is PsiReferenceExpression) {

                reference.resolveForColor()?.let { origin ->
                    origin.findInitializer()?.let { initializer ->
                        return@getCachedValue initializer.findRoot()
                    }
                }

            }

        }

    } else if (this is KtDotQualifiedExpression) {

        selectorExpression?.let { selector ->
            return@getCachedValue selector.findRoot()
        }

    } else if (this is KtQualifiedExpression) {

        callExpression?.let { callExpression ->
            return@getCachedValue callExpression.findRoot()
        }

    } else if (this is PsiMethodCallExpression) {

        resolveMethod()?.let { resolved ->
            if (resolved is KtLightMethod) {
                resolved.findInitializer()?.let {
                    return@getCachedValue it
                }
            }
        }

    }

    return@getCachedValue this

} ?: this

private fun PsiElement.findInitializer(): PsiElement? {

    var origin = (this as? ClsElementImpl)?.navigationElement ?: this

    if (origin is KtLightElement<*, *>) {
        origin = origin.navigationElement
    }

    if (origin is ClsElementImpl) {
        origin = origin.mirror ?: origin
    }

    if (origin is PsiField) {
        if (origin.modifierList?.hasModifierProperty(PsiModifier.FINAL) == true) {
            (origin.navigationElement as? PsiField)?.initializer?.let { initializer ->
                return initializer.findRoot()
            }
        }
    } else if (origin is PsiVariable) {
        if (origin.modifierList?.hasModifierProperty(PsiModifier.FINAL) == true) {
            origin.initializer?.let { initializer ->
                return initializer.findRoot()
            }
        }
    } else if (origin is KtProperty && !origin.isVar) {
        origin.initializer?.let { initializer ->
            return initializer.findRoot()
        }
    }


    return null

}

private fun PsiElement.ktInt(): Int? {

    if (this is KtConstantExpression) {

        getType(analyzePartial())?.let { type ->

            if (KotlinBuiltIns.isInt(type)) {

                return try {
                    text.toInt()
                } catch (e: NumberFormatException) {
                    null
                }

            }

        }

    }

    return null
}

private fun PsiElement.javaInt(): Long? {

    if (this is PsiExpression && type == PsiType.INT) {

        try {
            return if (text.startsWith("0x")) {
                java.lang.Long.valueOf(text.substring(2), 16)
            } else {
                java.lang.Long.valueOf(text)
            }
        } catch (_: NumberFormatException) {

        }

    }

    return null
}

private fun PsiElement.psiFloat(): Float? {

    val context = context

    if (context is KtDotQualifiedExpression) {
        if (context.receiverExpression.getType(context.analyzePartial())?.fqName() == COLOR_CLASS_NAME) {
            context.receiverExpression.getColor(ignoreContext = true)?.let { color ->
                return when (context.selectorExpression?.text) {
                    "r" -> color.red / 255f
                    "g" -> color.green / 255f
                    "b" -> color.blue / 255f
                    "a" -> color.alpha / 255f
                    else -> null
                }
            }
        }

    } else if (this is PsiReferenceExpression) {
        if ((qualifierExpression?.type as? PsiClassReferenceType)?.canonicalText == COLOR_CLASS_NAME) {
            qualifierExpression?.let { qualifierExpr ->
                qualifierExpr.getColor(ignoreContext = true)?.let { color ->
                    return when (referenceName) {
                        "r" -> color.red / 255f
                        "g" -> color.green / 255f
                        "b" -> color.blue / 255f
                        "a" -> color.alpha / 255f
                        else -> null
                    }
                }
            }
        }
    }

    val arg = findRoot()

    if (arg is PsiLiteralExpression || arg is KtConstantExpression) {

        try {
            return arg.text.toFloat()
        } catch (_: NumberFormatException) {

        }

    }

    return null

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
