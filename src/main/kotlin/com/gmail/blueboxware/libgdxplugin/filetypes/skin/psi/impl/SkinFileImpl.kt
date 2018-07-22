package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotation
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.SkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.getSkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.addResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import icons.Icons
import org.jetbrains.kotlin.idea.search.allScope
import org.jetbrains.kotlin.psi.psiUtil.allChildren
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
class SkinFileImpl(fileViewProvider: FileViewProvider): PsiFileBase(fileViewProvider, LibGDXSkinLanguage.INSTANCE), SkinFile {

  val factory = SkinElementFactory(project)

  override fun getFileType(): FileType = viewProvider.fileType

  override fun toString() = "SkinFile: " + (virtualFile?.name ?: "<unknown>")

  override fun getClassSpecifications(classNames: Collection<String>?): Collection<SkinClassSpecification> {
    val classSpecs = childrenOfType<SkinClassSpecification>()

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

  override fun getUseScope() = project.allScope()

  override fun addComment(comment: PsiComment) {

    firstChild?.node?.let { firstNode ->

      TreeUtil.skipWhitespaceAndComments(firstNode, true)?.let { anchor ->
        factory.createNewLine()?.let { newLine ->
          addAfter(newLine, addBefore(comment, anchor.psi.findParentWhichIsChildOf(this)))
        }
      }

    }

  }

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

  fun getOpeningBrace(): PsiElement? =
          allChildren.firstOrNull { it.isLeaf(SkinElementTypes.L_CURLY) }

  fun getClosingBrace(): PsiElement? =
          allChildren.lastOrNull { it.isLeaf(SkinElementTypes.R_CURLY) }

  fun isEmpty(): Boolean =
          allChildren.none { it.isLeaf(SkinElementTypes.L_CURLY) || it.isLeaf(SkinElementTypes.R_CURLY) }

  fun insertBraces() =
          factory.let {
            it.createElementsOrNull(it::createLeftBrace, it::createNewLine, it::createRightBrace)
                    ?.let { (lbrace, newline, rbrache) ->
                      add(lbrace)
                      add(newline)
                      add(rbrache)
                    }
          }

  fun addClassSpec(classNameWithDollar: String): SkinClassSpecification? {
    if (isEmpty()) {
      insertBraces()
    }

    val closingBrace = getClosingBrace() ?: return null

    val actualClassName =
            if (project.isLibGDX199()) {
              DEFAULT_TAGGED_CLASSES_NAMES.getKey(classNameWithDollar.removeDollarFromClassName()) ?: classNameWithDollar
            } else {
              classNameWithDollar
            }

    return factory.createClassSpec(actualClassName)?.let { classSpec ->
      addBefore(classSpec, closingBrace) as? SkinClassSpecification
    }

  }

  fun addResource(classNameWithDollar: String, resourceName: String): SkinResource? =
          classNameWithDollar.removeDollarFromClassName().let { className ->
            getClassSpecifications(className).lastOrNull()?.addResource(resourceName)
                    ?: addClassSpec(classNameWithDollar)?.addResource(resourceName)
          }

  fun addResource(classNameWithDollar: String, resource: SkinResource): SkinResource? =
          getClassSpecifications(classNameWithDollar.removeDollarFromClassName()).lastOrNull()?.addResource(resource)
                  ?: addClassSpec(classNameWithDollar)?.addResource(resource)

  fun addColor(name: String): SkinResource? =
          factory.createColorResource(name)?.let { resource ->
            addResource(Assets.COLOR_CLASS_NAME, resource)
          }

  fun addTintedDrawable(name: String): SkinResource? =
          factory.createTintedDrawableResource(name)?.let { resource ->
            addResource("com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable", resource)
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