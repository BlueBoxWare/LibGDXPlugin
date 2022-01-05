package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.CachedValue
import com.siyeh.ig.psiutils.MethodCallUtils
import org.jetbrains.kotlin.idea.intentions.callExpression
import org.jetbrains.kotlin.idea.search.allScope
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

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
internal fun PsiMethodCallExpression.isColorsGetCall(): Boolean = isColorsCall(false)

internal fun KtCallExpression.isColorsGetCall(): Boolean = isColorsCall(false)

internal fun PsiMethodCallExpression.isColorsPutCall(): Boolean = isColorsCall(true)

internal fun KtCallExpression.isColorsPutCall(): Boolean = isColorsCall(true)

private fun PsiMethodCallExpression.isColorsCall(isPut: Boolean): Boolean {

  val (clazz, method) = resolveCallToStrings() ?: return false

  val expectedMethodName = if (isPut) "put" else "get"

  if (clazz == COLORS_CLASS_NAME && method == expectedMethodName) {

    return true

  } else if (clazz == OBJECT_MAP_CLASS_NAME && method == expectedMethodName) {

    MethodCallUtils.getQualifierMethodCall(this)?.resolveCallToStrings()?.let { (clazz2, method2) ->
      if (clazz2 == COLORS_CLASS_NAME && method2 == "getColors") {
        return true
      }
    }

    ((methodExpression.qualifierExpression as? PsiReferenceExpression)?.resolve() as? PsiField)?.let { psiField ->
      if (psiField.containingClass?.qualifiedName == COLORS_CLASS_NAME && psiField.name == "map") {
        return true
      }
    }

  }

  return false

}

internal fun KtCallExpression.isColorsCall(isPut: Boolean): Boolean {

  val (clazz, method) = resolveCallToStrings() ?: return false

  val expectedMethodName = if (isPut) "put" else "get"

  if (clazz == COLORS_CLASS_NAME && method == expectedMethodName) {

    return true

  } else if (clazz == OBJECT_MAP_CLASS_NAME && method == expectedMethodName) {

    ((parent as? KtDotQualifiedExpression)?.receiverExpression as? KtDotQualifiedExpression)
            ?.resolveCallToStrings()
            ?.let { (clazz2, method2) ->
              if (clazz2 == COLORS_CLASS_NAME && method2 == "getColors") {
                return true
              }
            }

  }

  return false

}

internal class ColorsDefinition(
        nameElement: PsiElement,
        valueElement: PsiElement
) {

  private val nameElements = mutableSetOf(nameElement)

  var valueElement: PsiElement? = valueElement
    private set

  fun addNameElement(nameElement: PsiElement?) {
    nameElement?.let {
      nameElements.add(it)
    }
    valueElement = null
  }

  fun nameElements(): Set<PsiElement> = nameElements

}

private val KEY = key<CachedValue<Map<String, ColorsDefinition?>>>("colorsMap")

internal fun Project.getColorsMap(): Map<String, ColorsDefinition?> = getCachedValue(KEY, null) {

  if (!isLibGDXProject()) {
    mapOf<String, ColorsDefinition>()
  }

  val colorsClasses = psiFacade().findClasses(COLORS_CLASS_NAME, allScope())

  val callExpressions = mutableListOf<PsiElement>()

  // map.put(String, Color)
  colorsClasses.forEach { clazz ->
    clazz.findFieldByName("map", false)?.navigationElement?.let { map ->
      ReferencesSearch.search(map, allScope()).forEach { reference ->
        reference.element.getParentOfType<PsiMethodCallExpression>()?.let { call ->
          if (call.resolveMethod()?.name == "put") {
            callExpressions.add(call)
          }
        }
      }
    }
  }

  // getColors().put(String, Color)
  val getColorsMethods =
          colorsClasses.mapNotNull { it.findMethodsByName("getColors", false).firstOrNull() }

  getColorsMethods.forEach { method ->
    MethodReferencesSearch.search(method, allScope(), true).forEach { reference ->
      reference
              .element
              .getParentOfType<KtDotQualifiedExpression>()
              ?.getParentOfType<KtDotQualifiedExpression>()
              ?.callExpression
              ?.let { call ->
                call.resolveCallToStrings()?.let { (_, methodName) ->
                  if (methodName == "put") {
                    callExpressions.add(call)
                  }
                }
              }
      reference
              .element
              .getParentOfType<PsiCallExpression>()
              ?.getParentOfType<PsiCallExpression>()
              ?.let { call ->
                if (call.resolveMethod()?.name == "put") {
                  callExpressions.add(call)
                }
              }
    }
  }

  // Colors.put(String, Color)
  val putMethods =
          colorsClasses.mapNotNull { it.findMethodsByName("put", false).firstOrNull() }

  putMethods.forEach { method ->
    ReferencesSearch.search(method, allScope(), true).forEach { reference ->
      reference.element.getParentOfType<PsiCallExpression>()?.let {
        callExpressions.add(it)
      }
      reference.element.getParentOfType<KtCallExpression>()?.let {
        callExpressions.add(it)
      }
    }
  }

  val colors = mutableMapOf<String, ColorsDefinition?>()

  callExpressions.forEach { callExpression ->

    val colorName: String = getColorNameFromArgs(callExpression)?.second ?: return@forEach
    val colorDef = getColorDefFromArgs(callExpression)

    colors[colorName]?.let {
      it.addNameElement(colorDef?.first)
      return@forEach
    }

    if (colorDef != null) {
      colors[colorName] = ColorsDefinition(colorDef.first, colorDef.second)
    } else {
      colors[colorName] = null
    }

  }

  colors.toMap()

} ?: mapOf()


internal fun getColorNameFromArgs(callExpression: PsiElement): Pair<PsiElement, String?>? =
        (callExpression as? PsiCallExpression)?.let { psiCallExpression ->
          (psiCallExpression.argumentList?.expressions?.firstOrNull() as? PsiLiteralExpression)?.let {
            it to it.asString()
          }
        } ?: (callExpression as? KtCallExpression)?.let { ktCallExpression ->
          (ktCallExpression.valueArguments.firstOrNull()?.getArgumentExpression() as? KtStringTemplateExpression)?.let {
            it to it.asPlainString()
          }
        }


private fun getColorDefFromArgs(callExpression: PsiElement): Pair<PsiElement, PsiElement>? =
        (callExpression as? PsiCallExpression)?.let { psiCallExpression ->
          psiCallExpression.argumentList?.expressions?.let { args ->
            args.getOrNull(0)?.let { nameElement ->
              args.getOrNull(1)?.let { valueElement ->
                nameElement to valueElement
              }
            }
          }
        } ?: (callExpression as? KtCallExpression)?.let { ktCallExpression ->
          ktCallExpression.valueArguments.let { args ->
            args.getOrNull(0)?.getArgumentExpression()?.let { nameElement ->
              args.getOrNull(1)?.getArgumentExpression()?.let { valueElement ->
                nameElement to valueElement
              }
            }
          }
        }

