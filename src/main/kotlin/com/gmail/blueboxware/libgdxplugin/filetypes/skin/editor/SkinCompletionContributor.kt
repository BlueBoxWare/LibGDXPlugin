package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext

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
class SkinCompletionContributor : CompletionContributor() {

  init {

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinResourceName::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                resoureNameCompletion(parameters, result)
              }
            }
    )

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinClassName::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                classNameCompletion(parameters, result)
              }
            }
    )

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinPropertyName::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                propertyNameCompletion(parameters, result)
              }
            }
    )

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinPropertyValue::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                propertyValueCompletion(parameters, result)
              }
            }
    )

  }

  private fun resoureNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val resource = PsiTreeUtil.findFirstParent(parameters.position, { it is SkinResource }) as? SkinResource ?: return
    val classSpec = resource.classSpecification ?: return

    if (!classSpec.resourceNames.contains("default")) {
      result.addElement(LookupElementBuilder.create("default"))
    }
  }

  private fun propertyValueCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val propertyValue = PsiTreeUtil.findFirstParent(parameters.position, { it is SkinPropertyValue }) as? SkinPropertyValue ?: return
    val classSpecification = propertyValue.property?.containingClassSpecification ?: return
    val skinFile = propertyValue.containingFile as? SkinFile ?: return
    val property = propertyValue.property ?: return

    if (classSpecification.classNameAsString == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
      if (property.name == "file") {
        parameters.originalFile.virtualFile?.let { virtualFile ->
          for (file in AssetUtils.getAssociatedFiles(virtualFile)) {
            if (file.extension == "fnt") {
              VfsUtilCore.getRelativeLocation(file, virtualFile.parent)?.let { relativePath ->
                result.addElement(LookupElementBuilder.create(file, relativePath).withIcon(ICON_BITMAP_FONT))
              }
            }
          }
        }
      } else if (property.name == "markupEnabled" || property.name == "flip") {
        result.addElement(LookupElementBuilder.create("true"))
        result.addElement(LookupElementBuilder.create("false"))
      }
      return
    }

    val fieldClass = property.resolveToType() as? PsiClassType ?: return
    val fieldClassName = fieldClass.getCanonicalText(false)

    for (classSpec in skinFile.getClassSpecifications()) {
      if (SkinClassSpecificationMixin.removeDollarFromClassName(classSpec.classNameAsString) == fieldClassName) {
        for (resource in classSpec.resourcesAsList) {
          result.addElement(LookupElementBuilder.create(resource.name).withIcon(ICON_RESOURCE))
        }
      } else if (fieldClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" &&
              classSpec.classNameAsString == "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"
      ) {
        for (resource in classSpec.resourcesAsList) {
          result.addElement(LookupElementBuilder.create(resource.name).withIcon(ICON_TINTED_DRAWABLE))
        }
      }
    }

    if (fieldClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" ||
            (classSpecification.classNameAsString == "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable" && property.name == "name")) {
      parameters.originalFile.virtualFile?.let { file ->
        AssetUtils.getAssociatedAtlas(file)?.let { atlas ->
          AssetUtils.readImageNamesFromAtlas(atlas).forEach {
            result.addElement(LookupElementBuilder.create(it).withIcon(ICON_ATLAS))
          }
        }
      }
    }
  }

  private fun propertyNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val propertyName = (parameters.position.context as? SkinStringLiteral)?.asPropertyName() ?: return
    val containingObject = propertyName.property?.containingObject ?: return
    var isColor = false
    val usedPropertyNames = containingObject.propertyNames

    if (containingObject.parent !is SkinResource) {

      (containingObject.parent?.parent as? SkinProperty)?.let { property ->
        property.resolveToTypeString()?.let { type ->
          if (type == "com.badlogic.gdx.graphics.Color") {
            isColor = true
            listOf("r", "g", "b", "a").forEach {
              if (!usedPropertyNames.contains(it) && !usedPropertyNames.contains("hex")) {
                result.addElement(LookupElementBuilder.create(it).withIcon(ICON_FIELD))
              }
            }
          } else if (type == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
            listOf("file", "scaledSize", "flip", "markupEnabled").forEach {
              if (!usedPropertyNames.contains(it)) {
                result.addElement(LookupElementBuilder.create(it).withIcon(ICON_FIELD))
              }
            }
            return
          }
        }
      }

    } else {

      val resource = containingObject.asResource() ?: return
      val classSpec = resource.classSpecification ?: return

      if (classSpec.classNameAsString == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
        listOf("file", "scaledSize", "flip", "markupEnabled").forEach {
          result.addElement(LookupElementBuilder.create(it).withIcon(ICON_FIELD))
        }
        return
      }

      val psiClass = classSpec.resolveClass() ?: return

      if (psiClass.qualifiedName != "com.badlogic.gdx.graphics.Color" || !usedPropertyNames.contains("hex"))
      for (field in psiClass.allFields) {
        if (field.hasModifierProperty(PsiModifier.STATIC)) continue
        field.name?.let { name ->
          if (!usedPropertyNames.contains(name)) {
            result.addElement(LookupElementBuilder.create(field, name).withIcon(ICON_FIELD))
          }
        }
      }

      isColor = psiClass.qualifiedName == "com.badlogic.gdx.graphics.Color"
    }

    if (isColor && usedPropertyNames.none { listOf("r", "g", "b", "a", "hex").contains(it) } ) {
      result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create("hex").withIcon(ICON_FIELD), 1.0))
    }
  }

  private fun putDollarsInClassName(psiClass: PsiClass): String? {
    psiClass.containingClass.let { containingClass ->
      if (containingClass == null) {
        return psiClass.qualifiedName
      } else {
        return putDollarsInClassName(containingClass) + "$" + psiClass.name
      }
    }
  }

  private fun createLookupElement(psiClass: PsiClass): LookupElement? {
    putDollarsInClassName(psiClass)?.let { fqName ->
      return LookupElementBuilder.create(psiClass, fqName).withIcon(ICON_CLASS).withLookupString(psiClass.name ?: "")
    }

    return null
  }

  private fun classNameCompletion(parameters : CompletionParameters, result : CompletionResultSet) {

    val project = parameters.position.project
    val psiFacade = JavaPsiFacade.getInstance(project)
    val scene2duiPackage = psiFacade.findPackage("com.badlogic.gdx.scenes.scene2d.ui") ?: return
    val searchScope = GlobalSearchScope.allScope(project)

    for (psiClass in scene2duiPackage.classes) {
      for (innerClass in psiClass.innerClasses) {
        if (innerClass.qualifiedName?.endsWith("Style") != true) continue
        createLookupElement(innerClass)?.let { result.addElement(it) }
        for (subClass in ClassInheritorsSearch.search(innerClass, searchScope, true, true, false)) {
          createLookupElement(subClass)?.let { result.addElement(it) }
        }
      }
    }

    result.addElement(LookupElementBuilder.create("com.badlogic.gdx.graphics.Color").withIcon(ICON_CLASS).withLookupString("Color"))
    result.addElement(LookupElementBuilder.create("com.badlogic.gdx.graphics.g2d.BitmapFont").withIcon(ICON_CLASS).withLookupString("BitmapFont"))
    result.addElement(LookupElementBuilder.create("com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable").withIcon(ICON_CLASS).withLookupString("TintedDrawable"))
  }

  companion object {
    val ICON_CLASS = PlatformIcons.CLASS_ICON
    val ICON_RESOURCE = AllIcons.Nodes.KeymapOther
    val ICON_ATLAS = AllIcons.Nodes.ModuleGroup
    val ICON_BITMAP_FONT = AllIcons.Nodes.ExtractedFolder
    val ICON_TINTED_DRAWABLE = AllIcons.Nodes.KeymapOther
    val ICON_FIELD = PlatformIcons.FIELD_ICON
  }
}