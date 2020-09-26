package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.gmail.blueboxware.libgdxplugin.utils.compat.FindUsagesHandlerBaseCompat
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiCallExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
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
class ColorsFindUsagesHandler private constructor(element: PsiElement): FindUsagesHandlerBaseCompat(element) {

  constructor(element: PsiLiteralExpression): this(element as PsiElement)

  constructor(element: KtStringTemplateExpression): this(element as PsiElement)

  override fun doProcessElementUsages(
          element: PsiElement,
          processor: Processor<UsageInfo>,
          options: FindUsagesOptions
  ): Boolean {

    val colorNameToFind = ReadAction.compute<String, Throwable> {
      (element as? PsiLiteralExpression)?.asString()
              ?: (element as? KtStringTemplateExpression)?.asPlainString()
    } ?: return true

    ReadAction.run<Throwable> {

      val elements =
              CachedValuesManager.getManager(element.project).getCachedValue(
                      element,
                      MyCachedValueProvider(
                              project,
                              colorNameToFind
                      )
              )

      elements?.forEach { element ->
        processor.process(UsageInfo(element))
      }

    }

    return true

  }

}


private class MyCachedValueProvider(
        val project: Project,
        val colorNameToFind: String
): CachedValueProvider<Collection<PsiElement>> {

  override fun compute(): CachedValueProvider.Result<Collection<PsiElement>>? {

    val result = mutableListOf<PsiElement>()

    fun process(element: PsiElement) {

      ProgressManager.checkCanceled()

      val call =
              element.getParentOfType<PsiMethodCallExpression>(false)
                      ?: element.getParentOfType<KtCallExpression>(false)
                      ?: return

      getColorNameFromArgs(call)?.let { (argumentElement, name) ->
        if (name == colorNameToFind) {
          result += argumentElement
        }
      }

    }

    val allScope = project.allScope()
    val colorsClasses = project.psiFacade().findClasses(COLORS_CLASS_NAME, allScope)

    // Colors.get(String)
    colorsClasses.mapNotNull { it.findMethodsByName("get", false).firstOrNull() }.forEach { method ->
      MethodReferencesSearch.search(method, allScope, true).forEach { reference ->
        process(reference.element)
      }
    }

    // Colors.getColors().get(String)
    colorsClasses.mapNotNull { it.findMethodsByName("getColors", false).firstOrNull() }.forEach { method ->
      MethodReferencesSearch.search(method, allScope, true).forEach { reference ->
        reference
                .element
                .getParentOfType<KtDotQualifiedExpression>()
                ?.getParentOfType<KtDotQualifiedExpression>()
                ?.callExpression
                ?.let { call ->
                  call.resolveCallToStrings()?.let { (_, methodName) ->
                    if (methodName == "get") {
                      process(call)
                    }
                  }
                }
        reference
                .element
                .getParentOfType<PsiCallExpression>()
                ?.getParentOfType<PsiCallExpression>()
                ?.let { call ->
                  if (call.resolveMethod()?.name == "get") {
                    process(call)
                  }
                }
      }
    }

    return CachedValueProvider.Result.create(result, PsiModificationTracker.MODIFICATION_COUNT)

  }

}