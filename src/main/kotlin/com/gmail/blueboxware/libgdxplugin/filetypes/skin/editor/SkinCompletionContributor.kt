package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.UIUtil

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
                    .withSuperParent(2, SkinResource::class.java),
            object: CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                resourceAliasNameCompletion(parameters, result)
              }
            }
    )

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinResourceName::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                resourceNameCompletion(parameters, result)
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

    extend(CompletionType.BASIC,
            PlatformPatterns.psiElement()
                    .withParent(SkinStringLiteral::class.java)
                    .withSuperParent(2, SkinArray::class.java),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                propertyValueCompletion(parameters, result)
              }
            }
    )

  }

  private fun resourceAliasNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

    val resource = PsiTreeUtil.findFirstParent(parameters.position, { it is SkinResource }) as? SkinResource ?: return
    val classSpec = resource.classSpecification ?: return
    val originalClassSpec = PsiTreeUtil.findFirstParent(parameters.originalPosition, {it is SkinClassSpecification})

    (parameters.originalFile as? SkinFile)?.getClassSpecifications(classSpec.classNameAsString)?.forEach { cs ->
      cs.resourcesAsList.forEach { res ->
        if (res.name != resource.name || cs != originalClassSpec) {
          result.addElement(LookupElementBuilder.create(res.name))
        }
      }
    }

  }

  private fun resourceNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val resource = PsiTreeUtil.findFirstParent(parameters.position, { it is SkinResource }) as? SkinResource ?: return
    val classSpec = resource.classSpecification ?: return
    val usedResourceNames = (resource.containingFile as? SkinFile)?.getResources(classSpec.classNameAsString)?.map { it.name } ?: listOf()

    if (!usedResourceNames.contains("default")) {
      result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create("default").withBoldness(true), 1.0))
    }

    val strings = mutableSetOf<String>()

    (resource.containingFile as? SkinFile)?.getClassSpecifications()?.forEach {
      if (it != classSpec) {
        it.resourceNames.forEach { resourceName ->
          if (!usedResourceNames.contains(resourceName)) {
            strings.add(resourceName)
          }
        }
      }
    }

    strings.remove("default")

    strings.forEach {
      result.addElement(LookupElementBuilder.create(it))
    }

  }

  private fun propertyValueCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

    val stringLiteral = parameters.position.parent as? SkinStringLiteral ?: return
    val property = stringLiteral.property ?: return
    val objectType = property.containingObject?.resolveToTypeString()

    if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
      if (property.name == "file") {
        parameters.originalFile.virtualFile?.let { virtualFile ->
          for (file in AssetUtils.getAssociatedFiles(virtualFile)) {
            if (file.extension == "fnt" || file.fileType == BitmapFontFileType.INSTANCE) {
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

    val skinFile = parameters.originalFile as? SkinFile ?: return
    val elementType = stringLiteral.resolveToType()
    val elementClass = (elementType as? PsiClassType)?.resolve()
    val elementClassName = elementClass?.let { SkinClassSpecificationMixin.putDollarInInnerClassName(it) }

    if (elementClassName != null && elementClassName != "java.lang.Boolean") {

      skinFile.getClassSpecifications(elementClassName).forEach { classSpec ->
        classSpec.resourcesAsList.forEach { resource ->

          val icon = if (elementClassName == "com.badlogic.gdx.graphics.Color") {
            resource.asColor(true)?.let { ColorIcon(if (UIUtil.isRetina()) 24 else 12, it, true) }
          } else {
            null
          }

          result.addElement(LookupElementBuilder.create(resource.name).withIcon(icon ?: ICON_RESOURCE))

        }
      }

      if (elementClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable") {

        skinFile.getClassSpecifications("com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable").forEach { classSpec ->
          classSpec.resourcesAsList.forEach { resource ->
            result.addElement(LookupElementBuilder.create(resource.name).withIcon(ICON_TINTED_DRAWABLE))
          }
        }

      }
    } else if (elementType == PsiType.BOOLEAN || elementClassName == "java.lang.Boolean") {
      result.addElement(LookupElementBuilder.create("true"))
      result.addElement(LookupElementBuilder.create("false"))
    }

    if (elementClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable"
            || (objectType == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable" && property.name == "name")
    ) {

      skinFile.virtualFile?.let { virtualFile ->
        AssetUtils.getAssociatedAtlas(virtualFile)?.let { atlas ->
          AssetUtils.readImageNamesFromAtlas(atlas).forEach {
            result.addElement(LookupElementBuilder.create(it).withIcon(ICON_ATLAS))
          }
        }
      }

    }

  }

  private fun propertyNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

    val stringLiteral = parameters.position.parent as? SkinStringLiteral ?: return
    val property = stringLiteral.property ?: return
    val containingObject = property.containingObject ?: return
    val objectType = containingObject.resolveToTypeString()
    val usedPropertyNames = containingObject.propertyNames

    if (objectType == "com.badlogic.gdx.graphics.Color") {


      if (!usedPropertyNames.contains("hex")) {
        var addHex = true
        listOf("r", "g", "b", "a").forEach {
          if (!usedPropertyNames.contains(it)) {
            result.addElement(LookupElementBuilder.create(it).withIcon(ICON_FIELD))
          } else {
            addHex = false
          }
        }
        if (addHex) {
          result.addElement(LookupElementBuilder.create("hex").withIcon(ICON_FIELD))
        }
      }

    } else if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {

      listOf("file", "scaledSize", "flip", "markupEnabled").forEach {
        if (!usedPropertyNames.contains(it)) {
          result.addElement(LookupElementBuilder.create(it).withIcon(ICON_FIELD))
        }
      }

    } else {

      val clazz = containingObject.resolveToClass() ?: return

      for (field in clazz.allFields) {
        if (field.hasModifierProperty(PsiModifier.STATIC)) continue
        field.name?.let { name ->
          if (!usedPropertyNames.contains(name)) {
            result.addElement(LookupElementBuilder.create(field, name).withIcon(ICON_FIELD))
          }
        }
      }

    }

  }

  private fun classNameCompletion(parameters : CompletionParameters, result : CompletionResultSet) {
    val prefix = result.prefixMatcher.prefix.dropLastWhile { it != '.' }.dropLastWhile { it == '.' }
    val project = parameters.position.project
    val psiFacade = JavaPsiFacade.getInstance(project)
    val rootPackage = psiFacade.findPackage(prefix) ?: return

    for (subpackage in rootPackage.subPackages) {
      val priority = packagePriority(subpackage.qualifiedName)
      result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(subpackage, subpackage.qualifiedName)
              .withIcon(ICON_PACKAGE)
              .withBoldness(priority > 0.0),
              priority
      ))
    }

    val currentPackage = psiFacade.findPackage(prefix)
    if (currentPackage == null || currentPackage.name == null) {
      AllClassesGetter.processJavaClasses(result.prefixMatcher, project, GlobalSearchScope.allScope(project), { psiClass ->
        SkinClassSpecificationMixin.putDollarInInnerClassName(psiClass)?.let { name ->
          val priority = classPriority(name)
          result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(psiClass, name)
                  .withPresentableText(name)
                  .withLookupString(psiClass.name ?: "")
                  .withIcon(ICON_CLASS)
                  .withBoldness(priority > 0.0),
                  priority
          ))
        }
        true
      })

      return
    }

    for (clazz in currentPackage.classes) {
      if (!clazz.isAnnotationType && !clazz.isInterface && !clazz.hasModifierProperty(PsiModifier.ABSTRACT) && clazz.containingClass == null) {
        clazz.qualifiedName?.let { fqName ->
          val priority = classPriority(fqName)
          result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(clazz, fqName)
                  .withIcon(ICON_CLASS)
                  .withBoldness(priority > 0.0)
                  , priority))
        }
      }

      for (innerClass in clazz.innerClasses) {
        val fqName = clazz.qualifiedName + "$" + innerClass.name
        val priority = classPriority(fqName)
        if (innerClass.hasModifierProperty(PsiModifier.PUBLIC)) {
          result.addElement(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(innerClass, fqName)
                  .withBoldness(priority > 0.0)
                  .withIcon(ICON_CLASS)
                  , priority))
        }
      }

    }

  }

  private fun packagePriority(packageName: String): Double {
    if ("com.badlogic.gdx.scenes.scene2d.ui".contains(packageName) || "com.badlogic.gdx.graphics".contains(packageName)) {
      return 1.0
    }
    return 0.0
  }

  private fun classPriority(className: String): Double {
    if (prioritizedClasses.contains(className)) {
      return 1.0
    } else if (className.contains("com.badlogic.gdx.scenes.scene2d.ui") && className.endsWith("Style")) {
      return 1.0
    }
    return 0.0
  }

  companion object {
    val prioritizedClasses = listOf(
            "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable",
            "com.badlogic.gdx.graphics.Color",
            "com.badlogic.gdx.graphics.g2d.BitmapFont"
    )
    val ICON_CLASS = PlatformIcons.CLASS_ICON
    val ICON_PACKAGE = PlatformIcons.PACKAGE_ICON
    val ICON_RESOURCE = AllIcons.Nodes.KeymapOther
    val ICON_ATLAS = AllIcons.Nodes.ModuleGroup
    val ICON_BITMAP_FONT = AllIcons.Nodes.ExtractedFolder
    val ICON_TINTED_DRAWABLE = AllIcons.Nodes.KeymapOther
    val ICON_FIELD = PlatformIcons.FIELD_ICON
  }
}