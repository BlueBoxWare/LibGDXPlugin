package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.*
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
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
import java.awt.Color

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
    val classNamesToSearch = classNames?.toMutableSet()

    if (classNamesToSearch != null) {

      if (classNamesToSearch.contains(FREETYPE_FONT_PARAMETER_CLASS_NAME) || classNamesToSearch.contains(BITMAPFONT_CLASS_NAME)) {
        classNamesToSearch.add(FREETYPE_GENERATOR_CLASS_NAME)
      }

      return classSpecs.filter { classSpec ->
        classSpec.getRealClassNamesAsString().any { classNamesToSearch.contains(it) }
      }
    }

    return classSpecs
  }

  override fun getClassSpecifications(className: String): Collection<SkinClassSpecification> =
          getClassSpecifications(listOf(className))

  override fun getResources(classNames: Collection<String>?, resourceName: String?, beforeElement: PsiElement?): Collection<SkinResource> =
          getClassSpecifications(classNames)
                  .flatMap { skinClassSpecification ->
                    skinClassSpecification.resourcesAsList.filter { resourceName == null || resourceName == it.name }
                  }.filter {
                    beforeElement == null || it.endOffset < beforeElement.startOffset
                  }

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

      if (!includeAll && foundResources.isNotEmpty()) {
        return foundResources
      }

    }

    return foundResources

  }

  override fun getActiveAnnotations(annotation: SkinAnnotations?): List<SkinAnnotation> =
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

  @Suppress("unused")
  fun getOpeningBrace(): PsiElement? =
          allChildren.firstOrNull { it.isLeaf(SkinElementTypes.L_CURLY) }

  @Suppress("MemberVisibilityCanBePrivate")
  fun getClosingBrace(): PsiElement? =
          allChildren.lastOrNull { it.isLeaf(SkinElementTypes.R_CURLY) }

  private fun isEmpty(): Boolean =
          allChildren.none { it.isLeaf(SkinElementTypes.L_CURLY, SkinElementTypes.R_CURLY) }

  private fun insertBraces() =
          factory.let {
            it.createElementsOrNull(it::createLeftBrace, it::createNewLine, it::createRightBrace)
                    ?.let { (lbrace, newline, rbrache) ->
                      add(lbrace)
                      add(newline)
                      add(rbrache)
                    }
          }

  private fun addClassSpec(className: DollarClassName, cause: SkinElement? = null): SkinClassSpecification? {
    if (isEmpty()) {
      insertBraces()
    }

    val actualClassName =
            if (project.isLibGDX199()) {
              DEFAULT_TAGGED_CLASSES_NAMES.getKey(className.plainName) ?: className.dollarName
            } else {
              className.dollarName
            }

    val addBefore = cause?.firstParent<SkinClassSpecification>()
            ?: getClosingBrace() ?: return null

    val result = factory.createClassSpec(actualClassName)?.let { classSpec ->
      addBefore(classSpec, addBefore) as? SkinClassSpecification
    }

    result?.let { actualResult ->
      if (!actualResult.isPrecededByNewline()) {
        factory()?.createNewLine()?.let { newline ->
          addBefore(newline, actualResult)
        }
      }
      if (!actualResult.isFollowByNewLine()) {
        factory()?.createNewLine()?.let { newline ->
          addAfter(newline, actualResult)
        }
      }
    }

    return result

  }

  private fun getClassSpecToInsertInto(className: DollarClassName, cause: SkinElement?): SkinClassSpecification? {
    val classSpecs =
            getClassSpecifications(className.plainName).takeIf { !it.isEmpty() }
                    ?: addClassSpec(className, cause)?.let(::listOf) ?: return null

    return cause?.firstParent<SkinClassSpecification>()?.let { causingClassSpec ->
      classSpecs.find { it == causingClassSpec }
    } ?: classSpecs.lastOrNull()

  }

  fun addResource(className: DollarClassName, resourceName: String, cause: SkinElement? = null): Pair<SkinResource, Int>? =
          getClassSpecToInsertInto(className, cause)?.addResource(resourceName, cause)

  private fun addResource(className: DollarClassName, resource: SkinResource, cause: SkinElement? = null): SkinResource? =
          getClassSpecToInsertInto(className, cause)?.addResource(resource, cause)

  fun addColor(name: String, cause: SkinElement? = null, color: Color? = null, useComponents: Boolean = false): Pair<SkinResource, Int>? =
          factory.createColorResource(name, color, useComponents)?.let { (resource, position) ->
            addResource(DollarClassName(COLOR_CLASS_NAME), resource, cause)?.let {
              Pair(it, it.startOffset + position)
            }
          }

  fun addTintedDrawable(name: String, cause: SkinElement? = null): Pair<SkinResource, Int>? =
          factory.createTintedDrawableResource(name)?.let { (resource, position) ->
            addResource(DollarClassName("com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"), resource, cause)?.let {
              Pair(it, it.startOffset + position)
            }
          }

  override fun getPresentation() = FilePresentation(project, virtualFile, name, Icons.SKIN_FILETYPE)

}