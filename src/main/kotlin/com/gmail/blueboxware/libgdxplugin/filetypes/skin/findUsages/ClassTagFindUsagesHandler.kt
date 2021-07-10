package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.asPlainString
import com.gmail.blueboxware.libgdxplugin.utils.asString
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.kotlin.idea.search.allScope
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
class ClassTagFindUsagesHandler private constructor(element: PsiElement): FindUsagesHandler(element) {

  constructor(element: PsiLiteralExpression): this(element as PsiElement)

  constructor(element: KtStringTemplateExpression): this(element as PsiElement)

  override fun processElementUsages(
          element: PsiElement,
          processor: Processor<in UsageInfo>,
          options: FindUsagesOptions
  ): Boolean {

    ReadAction.run<Throwable> {

      val text = when (element) {
        is PsiLiteralExpression -> element.asString()
        is KtStringTemplateExpression -> element.asPlainString()
        else -> null
      }

      val usages =
              CachedValuesManager.getManager(element.project).getCachedValue(
                      element,
                      MyCachedValueProvider(
                              project,
                              text,
                              (options.searchScope as? GlobalSearchScope) ?: project.allScope()
                      )
              )

      usages?.forEach { usage ->
        processor.process(UsageInfo(usage))
      }

    }

    return true

  }

}

private class MyCachedValueProvider(
        val project: Project,
        val text: String?,
        val scope: GlobalSearchScope
): CachedValueProvider<Collection<SkinClassName>> {

  override fun compute(): CachedValueProvider.Result<Collection<SkinClassName>> {

    // Don't store UsageInfo: it leads to double smart pointer removal
    val usages = mutableListOf<SkinClassName>()

    text?.let {

      FileTypeIndex.processFiles(LibGDXSkinFileType.INSTANCE, { virtualFile ->

        (PsiManager.getInstance(project).findFile(virtualFile) as? SkinFile)?.let { skinFile ->

          skinFile.getClassSpecifications().forEach { classSpec ->
            classSpec.className.let { className ->
              if (className.value.plainName == text) {
                usages.add(className)
              }
            }
          }
        }

        true
      }, scope)

    }

    return CachedValueProvider.Result.create(usages, PsiModificationTracker.MODIFICATION_COUNT)

  }

}
