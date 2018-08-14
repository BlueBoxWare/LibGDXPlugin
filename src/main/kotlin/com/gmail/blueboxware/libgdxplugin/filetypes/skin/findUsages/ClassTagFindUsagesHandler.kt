package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.allScope
import com.gmail.blueboxware.libgdxplugin.utils.asString
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ReadAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.plainContent


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
class ClassTagFindUsagesHandler(element: PsiElement): FindUsagesHandler(element) {

  override fun processElementUsages(element: PsiElement, processor: Processor<UsageInfo>, options: FindUsagesOptions): Boolean {

    ReadAction.run<Throwable> {
      val text = if (element is PsiLiteralExpression) {
        element.asString()
      } else if (element is KtStringTemplateExpression) {
        element.plainContent
      } else {
        return@run
      }

      val psiManager = PsiManager.getInstance(psiElement.project)

      FileTypeIndex.processFiles(LibGDXSkinFileType.INSTANCE, { virtualFile ->

        (psiManager.findFile(virtualFile) as? SkinFile)?.let { skinFile ->

          skinFile.getClassSpecifications().forEach { classSpec ->
            classSpec.className.let { className ->
              if (className.value.plainName == text) {
                if (!processor.process(UsageInfo(className))) {
                  return@processFiles false
                }
              }
            }
          }
        }

        true
      }, (options.searchScope as? GlobalSearchScope) ?: element.allScope())

    }

    return true

  }

}