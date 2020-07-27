package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasValue
import com.gmail.blueboxware.libgdxplugin.utils.getProjectBaseDir
import com.gmail.blueboxware.libgdxplugin.utils.getPsiFile
import com.gmail.blueboxware.libgdxplugin.utils.isDefaultFile
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.*
import com.intellij.psi.search.FileTypeIndex
import com.intellij.util.IncorrectOperationException
import com.intellij.util.PathUtil
import org.jetbrains.kotlin.idea.search.projectScope
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

/*
 * Copyright 2017 Blue Box Ware
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
class FileReference(
        element: PsiElement,
        val path: String,
        val fileTypes: List<FileType>,
        val preferableLanguages: List<Language> = listOf()
): PsiReferenceBase<PsiElement>(element) {

  override fun resolve(): PsiElement? = myElement.project.getPsiFile(path)

  override fun getVariants(): Array<out Any> {

    val result = mutableListOf<LookupElement>()
    val searchScope = myElement.project.projectScope()
    val psiManager = PsiManager.getInstance(myElement.project)
    val baseDir = myElement.project.getProjectBaseDir() ?: return arrayOf()

    for (fileType in fileTypes) {
      FileTypeIndex.getFiles(fileType, searchScope).forEach { virtualFile ->

        psiManager.findFile(virtualFile)?.let { psiFile ->

          if (psiFile !is PropertiesFileImpl || psiFile.isDefaultFile()) {

            val relativePath = PathUtil.toSystemDependentName(VfsUtil.getRelativePath(virtualFile, baseDir))
            val prioritized = LanguageUtil.getLanguageForPsi(element.project, virtualFile) in preferableLanguages

            PrioritizedLookupElement.withPriority(
                    LookupElementBuilder
                            .create(psiFile, relativePath.replace("\\", "\\\\", false))
                            .withPresentableText(relativePath)
                            ?.withIcon(psiFile.getIcon(0))
                            ?.withBoldness(prioritized),
                    if (prioritized) Double.MAX_VALUE else 0.0
            )?.let {
              result.add(it)
            }

          }

        }

      }
    }

    return result.toTypedArray()

  }

  override fun handleElementRename(newElementName: String): PsiElement {

    return if (element is AtlasValue) {

      // we don't handle renaming of the image file in Atlas files
      element

    } else if (element is KtStringTemplateExpression || element is PsiLiteralExpression) {

      val newPath = PathUtil.toSystemDependentName(path.dropLastWhile { it != '/' } + newElementName)
      super.handleElementRename(newPath)

    } else {

      throw IncorrectOperationException()

    }

  }

  override fun getRangeInElement(): TextRange = ElementManipulators.getValueTextRange(element)

}