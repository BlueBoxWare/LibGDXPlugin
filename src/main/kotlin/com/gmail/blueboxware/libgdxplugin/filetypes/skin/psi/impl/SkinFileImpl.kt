package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotation
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.getSkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import icons.Icons
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.io.File

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
class SkinFileImpl(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, LibGDXSkinLanguage.INSTANCE), SkinFile {

  override fun getFileType(): FileType = viewProvider.fileType

  override fun toString() = "SkinFile: " + (virtualFile?.name ?: "<unknown>")

  override fun getClassSpecifications(className: String?): Collection<SkinClassSpecification> {
    val classSpecs = PsiTreeUtil.findChildrenOfType(this, SkinClassSpecification::class.java)

    if (className != null) {
      return classSpecs.filter { it.classNameAsString == className }
    }

    return classSpecs
  }

  override fun getResources(className: String?, resourceName: String?, beforeElement: PsiElement?) =
          getClassSpecifications(className)
                  .flatMap { it.resourcesAsList.filter { resourceName == null || resourceName == it.name } }
                  .filter { beforeElement == null || it.endOffset < beforeElement.startOffset }

  override fun getActiveAnnotations(annotation: SkinAnnotations?): List<SkinAnnotation>  =
          children.flatMap { (it as? PsiComment)?.getSkinAnnotations() ?: listOf() }.filter { if (annotation != null) it.first == annotation else true }

  override fun isInspectionSuppressed(inspectionId: String): Boolean =
          getActiveAnnotations(SkinAnnotations.SUPPRESS).filter { it.second == inspectionId }.isNotEmpty()

  override fun getUseScope() = GlobalSearchScope.allScope(project)

  override fun getPresentation() = object: ItemPresentation {

    override fun getLocationString(): String {
      project.baseDir?.let { baseDir ->
        virtualFile?.let { virtualFile ->
          return VfsUtil.getPath(baseDir, virtualFile, File.separatorChar) ?: ""
        }
      }

      return ""
    }

    override fun getIcon(unused: Boolean) = Icons.SKIN_FILETYPE

    override fun getPresentableText() = name
  }
}