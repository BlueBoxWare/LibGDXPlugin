package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotation
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.getSkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.addCommentExt
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiClass
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

  override fun getClassSpecifications(classNames: Collection<String>?): Collection<SkinClassSpecification> {
    val classSpecs = PsiTreeUtil.findChildrenOfType(this, SkinClassSpecification::class.java)

    if (classNames != null) {
      return classSpecs.filter { it.getRealClassNamesAsString().any { classNames.contains(it) } }
    }

    return classSpecs
  }

  override fun getClassSpecifications(className: String): Collection<SkinClassSpecification> =
          getClassSpecifications(listOf(className))

  override fun getResources(classNames: Collection<String>?, resourceName: String?, beforeElement: PsiElement?): Collection<SkinResource> =
          getClassSpecifications(classNames)
                  .flatMap { it.resourcesAsList.filter { resourceName == null || resourceName == it.name } }
                  .filter { beforeElement == null || it.endOffset < beforeElement.startOffset }

  override fun getResources(className: String, resourceName: String?, beforeElement: PsiElement?): Collection<SkinResource> =
          getResources(listOf(className), resourceName, beforeElement)

  override fun getResources(
          resourceClass: PsiClass,
          resourceName: String?,
          beforeElement: PsiElement?,
          includingSuperClasses: Boolean,
          includeAll: Boolean // keep looking up the superclass list after finding resources
  ): Collection<SkinResource> {

    if (!includingSuperClasses) {
      return resourceClass.qualifiedName?.let { getResources(it, resourceName, beforeElement) } ?: listOf()
    }

    val classesToSearch = generateSequence(resourceClass) { it.superClass }
    val classSpecs = getClassSpecifications()
    val foundResources = mutableListOf<SkinResource>()

    classesToSearch.forEach { clazz ->

      for (classSpec in classSpecs) {

        val realClassNames = classSpec.getRealClassNamesAsString()

        if (realClassNames.contains(clazz.qualifiedName)) {
          classSpec.getResourcesAsList(beforeElement).forEach { resource ->
            if (resourceName == null || resource.name == resourceName) {
              foundResources.add(resource)
            }
          }
        }

      }

      if (!includeAll && !foundResources.isEmpty()) {
        return foundResources
      }

    }

    return foundResources

  }

  override fun getActiveAnnotations(annotation: SkinAnnotations?): List<SkinAnnotation>  =
          children
                  .flatMap { (it as? PsiComment)?.getSkinAnnotations() ?: listOf() }
                  .filter { if (annotation != null) it.first == annotation else true }

  override fun isInspectionSuppressed(inspectionId: String): Boolean =
          getActiveAnnotations(SkinAnnotations.SUPPRESS).any { it.second == inspectionId }

  override fun getUseScope() = GlobalSearchScope.allScope(project)

  override fun addComment(comment: PsiComment) = addCommentExt(comment)

  override fun replacePackage(className: String, oldPackage: String, newPackage: String) {

    val oldFqName = if (oldPackage == "") className else "$oldPackage.$className"
    val newFqName = if (newPackage == "") className else "$newPackage.$className"

    for (classSpecification in getClassSpecifications()) {
      if (classSpecification.name == oldFqName) {
        classSpecification.setName(newFqName)
      } else if (classSpecification.name?.startsWith("$oldFqName$") == true) {
        classSpecification.name?.replaceFirst(oldFqName, newFqName)?.let {
          classSpecification.setName(it)
        }
      }
    }

  }

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