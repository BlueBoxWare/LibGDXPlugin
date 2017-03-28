package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.FileUtils
import com.gmail.blueboxware.libgdxplugin.utils.PsiUtils
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.PathUtil

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
) : PsiReferenceBase<PsiElement>(element) {

  override fun resolve(): PsiElement? = PsiUtils.getPsiFile(myElement.project, path)

  override fun getVariants(): Array<out Any> {

    val result = mutableListOf<LookupElement>()
    val searchScope = GlobalSearchScope.projectScope(myElement.project)
    val psiManager = PsiManager.getInstance(myElement.project)
    val baseDir = FileUtils.projectBaseDir(myElement.project) ?: return arrayOf()

    for (fileType in fileTypes) {
      FileTypeIndex.getFiles(fileType, searchScope).forEach { virtualFile ->

        psiManager.findFile(virtualFile)?.let { psiFile ->

          val relativePath = PathUtil.toSystemDependentName(VfsUtil.getRelativePath(virtualFile, baseDir))
          val prioritized = LanguageUtil.getLanguageForPsi(element.project, virtualFile) in preferableLanguages

          PrioritizedLookupElement.withPriority(
                  LookupElementBuilder
                          .create(psiFile, relativePath.replace("\\", "\\\\", false))
                          .withPresentableText(relativePath)
                          ?.withIcon(psiFile.getIcon(0))
                          ?.withBoldness(prioritized),
                  if (prioritized) Double.MAX_VALUE else 0.0
          )
                          ?.let {
            result.add(it)
          }

        }

      }
    }

    return result.toTypedArray()

  }

  override fun handleElementRename(newElementName: String?): PsiElement {
    val newPath = PathUtil.toSystemDependentName(path.dropLastWhile { it != '/' } + newElementName)
    return super.handleElementRename(newPath)
  }
}