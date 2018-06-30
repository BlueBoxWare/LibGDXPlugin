package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinFileInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import javax.swing.Icon

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
            PlatformPatterns.psiComment(),
            object : CompletionProvider<CompletionParameters>() {
              override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
                annotationCompletion(parameters, result)
              }
            }
    )

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

  override fun beforeCompletion(context: CompletionInitializationContext) {
    if (context.file is SkinFile) {
      context.dummyIdentifier = CompletionUtil.DUMMY_IDENTIFIER_TRIMMED
    }

    val element = context.file.findElementAt(context.caret.offset)
    if ((element?.context as? SkinStringLiteral)?.isQuoted == true) {
      context.replacementOffset = context.replacementOffset - 1
    }
  }

  private fun annotationCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val commentStartOffset = parameters.originalPosition?.startOffset ?: return
    val startingText = parameters.originalFile.text.substring(commentStartOffset, parameters.offset).trimEnd()

    if (Regex("""@\w*$""").containsMatchIn(startingText)) {
      result.addElement(LookupElementBuilder.create("Suppress").withIcon(AllIcons.Nodes.Annotationtype))
    } else if (Regex("""@\s*Suppress\s*\(\s*["']?\s*\w*$""", RegexOption.IGNORE_CASE).containsMatchIn(startingText)) {
      SkinFileInspection.INSPECTION_NAMES.forEach {
        result.addElement(LookupElementBuilder.create(it).withIcon(AllIcons.Nodes.Annotationtype))
      }
    }
  }

  private fun resourceAliasNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

    val resource = PsiTreeUtil.findFirstParent(parameters.position) { it is SkinResource } as? SkinResource ?: return
    val classSpec = resource.classSpecification ?: return
    val originalClassSpec = PsiTreeUtil.findFirstParent(parameters.originalPosition) {it is SkinClassSpecification}

    if (classSpec.getRealClassNamesAsString().none { it != "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable" }) {
      // Aliases for TintedDrawables are not allowed
      return
    }

    (parameters.originalFile as? SkinFile)?.getClassSpecifications(classSpec.getRealClassNamesAsString())?.forEach { cs ->
      cs.getResourcesAsList(resource).forEach { res ->
        if (res.name != resource.name || cs != originalClassSpec) {
          val icon = res
                  .takeIf { cs.getRealClassNamesAsString().contains("com.badlogic.gdx.graphics.Color") }
                  ?.asColor(true)
                  ?.let { createColorIcon(it) }
                  ?: ICON_RESOURCE

          doAdd(LookupElementBuilder.create(res.name.makeSafe()).withIcon(icon).withPresentableText(res.name.escape()), parameters, result)
        }
      }
    }

  }

  private fun resourceNameCompletion(parameters: CompletionParameters, result: CompletionResultSet) {
    val resource = PsiTreeUtil.findFirstParent(parameters.position) { it is SkinResource } as? SkinResource ?: return
    val classSpec = resource.classSpecification ?: return
    val usedResourceNames = (resource.containingFile as? SkinFile)?.getResources(classSpec.getRealClassNamesAsString())?.map { it.name } ?: listOf()

    if (!usedResourceNames.contains("default")) {
      doAdd(PrioritizedLookupElement.withPriority(LookupElementBuilder.create("default").withBoldness(true), 2.0), parameters, result)
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
      doAdd(LookupElementBuilder.create(it), parameters, result)
    }

  }

  private fun propertyValueCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

    val stringLiteral = parameters.position.parent as? SkinStringLiteral ?: return
    val property = stringLiteral.property ?: return
    val objectType = property.containingObject?.resolveToTypeString()

    if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
      if (property.name == PROPERTY_NAME_FONT_FILE) {
        parameters.originalFile.virtualFile?.let { virtualFile ->
          for (file in virtualFile.getAssociatedFiles()) {
            if (file.extension == "fnt" || file.fileType == BitmapFontFileType.INSTANCE) {
              VfsUtilCore.getRelativeLocation(file, virtualFile.parent)?.let { relativePath ->
                doAdd(LookupElementBuilder.create(relativePath.makeSafe()).withPresentableText(relativePath).withIcon(ICON_BITMAP_FONT), parameters, result)
              }
            }
          }
        }
      } else if (property.name == PROPERTY_NAME_FONT_MARKUP || property.name == PROPERTY_NAME_FONT_FLIP) {
        doAdd(LookupElementBuilder.create("true"), parameters, result)
        doAdd(LookupElementBuilder.create("false"), parameters, result)
      }
      return
    }

    val skinFile = parameters.originalFile as? SkinFile ?: return
    val elementType = stringLiteral.resolveToType()
    val elementClass = (elementType as? PsiClassType)?.resolve()
    val isParentProperty = stringLiteral.property?.name == PROPERTY_NAME_PARENT && parameters.position.project.isLibGDX199()
    val elementClassName = elementClass?.qualifiedName

    if (elementClass != null && elementClassName != "java.lang.Boolean") {

      skinFile.getResources(elementClass, null, stringLiteral, isParentProperty, isParentProperty).forEach { resource ->

        val icon =
                resource.takeIf { elementClassName == "com.badlogic.gdx.graphics.Color" }?.asColor(true)?.let { createColorIcon(it) }
                        ?: ICON_RESOURCE

        doAdd(LookupElementBuilder.create(resource.name).withIcon(icon), parameters, result)

      }

      if (elementClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable") {

        skinFile.getClassSpecifications("com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable").forEach { classSpec ->
          classSpec.getResourcesAsList(property).forEach { resource ->
            doAdd(LookupElementBuilder.create(resource.name).withIcon(ICON_TINTED_DRAWABLE), parameters, result)
          }
        }

      }
    } else if (elementType == PsiType.BOOLEAN || elementClassName == "java.lang.Boolean") {
      doAdd(LookupElementBuilder.create("true"), parameters, result)
      doAdd(LookupElementBuilder.create("false"), parameters, result)
    }

    if (elementClassName == "com.badlogic.gdx.scenes.scene2d.utils.Drawable"
            || (objectType == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable" && property.name == PROPERTY_NAME_TINTED_DRAWABLE_NAME)
    ) {

      skinFile.virtualFile?.let { virtualFile ->
        virtualFile.getAssociatedAtlas()?.let { atlas ->
          atlas.readImageNamesFromAtlas().forEach {
            doAdd(LookupElementBuilder.create(it).withIcon(ICON_ATLAS), parameters, result)
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

    if (!usedPropertyNames.contains(PROPERTY_NAME_PARENT) && parameters.position.project.isLibGDX199()) {
      val important = objectType !in listOf(
              "com.badlogic.gdx.graphics.Color",
              "com.badlogic.gdx.graphics.g2d.BitmapFont",
              "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable"
      )
      doAdd(
              PrioritizedLookupElement.withPriority(
                      LookupElementBuilder.create(PROPERTY_NAME_PARENT).withBoldness(important).withIcon(ICON_PARENT),
                      if (important) 2.0 else 0.0
              ),
              parameters,
              result
      )
    }

    if (objectType == "com.badlogic.gdx.graphics.Color") {

      if (!usedPropertyNames.contains("hex")) {
        var addHex = true
        listOf("r", "g", "b", "a").forEach {
          if (!usedPropertyNames.contains(it)) {
            doAdd(LookupElementBuilder.create(it).withIcon(ICON_FIELD), parameters, result)
          } else {
            addHex = false
          }
        }
        if (addHex) {
          doAdd(LookupElementBuilder.create("hex").withIcon(ICON_FIELD), parameters, result)
        }
      }

    } else if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {

      listOf(PROPERTY_NAME_FONT_FILE, PROPERTY_NAME_FONT_SCALED_SIZE, PROPERTY_NAME_FONT_FLIP, PROPERTY_NAME_FONT_MARKUP).forEach {
        if (!usedPropertyNames.contains(it)) {
          doAdd(LookupElementBuilder.create(it).withIcon(ICON_FIELD), parameters, result)
        }
      }

    } else {

      val clazz = containingObject.resolveToClass() ?: return

      for (field: PsiField? in clazz.allFields) {
        if (field?.hasModifierProperty(PsiModifier.STATIC) == true) continue
        field?.name?.let { name ->
          if (!usedPropertyNames.contains(name)) {
            doAdd(LookupElementBuilder.create(field, name).withIcon(ICON_FIELD), parameters, result)
          }
        }
      }

    }

  }

  private fun classNameCompletion(parameters : CompletionParameters, result : CompletionResultSet) {
    val prefix = result.prefixMatcher.prefix.dropLastWhile { it != '.' }.dropLastWhile { it == '.' }.let { prefix ->
      if (prefix.firstOrNull() == '"') {
        prefix.substring(1)
      } else {
        prefix
      }
    }
    val project = parameters.position.project
    val psiFacade = JavaPsiFacade.getInstance(project)
    val rootPackage = psiFacade.findPackage(prefix) ?: return

    project.getSkinTag2ClassMap()?.getTags()?.forEach { tag ->
      doAdd(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(tag)
              .withIcon(ICON_TAG)
              .withBoldness(true),
              2.0
      ), parameters, result)
    }

    for (subpackage in rootPackage.subPackages) {
      val priority = packagePriority(subpackage.qualifiedName)
      doAdd(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(subpackage, subpackage.qualifiedName)
              .withIcon(ICON_PACKAGE)
              .withBoldness(priority > 0.0),
              priority
      ), parameters, result)
    }

    val dummyText = parameters.position.text
    val currentPackage = psiFacade.findPackage(prefix)

    val scope = ModuleUtilCore.findModuleForPsiElement(parameters.position)?.let {
      GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(it)
    } ?: return

    if (currentPackage == null || currentPackage.name == null) {

      val prefixMatcher = CamelHumpMatcher(result.prefixMatcher.prefix.substring(if (dummyText.firstOrNull() == '"') 1 else 0).takeWhile { it != '.' && it != '$' })

      AllClassesGetter.processJavaClasses(prefixMatcher, project, scope) { psiClass ->

        if ((psiClass.containingClass == null || psiClass.hasModifierProperty(PsiModifier.STATIC)
                        && (psiClass !is KtLightClass || psiClass.kotlinOrigin !is KtObjectDeclaration)
                        )) {

          for (innerClass in psiClass.findAllStaticInnerClasses()) {

            if (!innerClass.isAnnotationType && !innerClass.isInterface && !innerClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
              val fqName = innerClass.putDollarInInnerClassName()
              val priority = classPriority(fqName)
              doAdd(PrioritizedLookupElement.withPriority(LookupElementBuilder.create(psiClass, fqName)
                      .withPresentableText(fqName)
                      .withLookupString(StringUtil.getShortName(fqName))
                      .withIcon(ICON_CLASS)
                      .withBoldness(priority > 0.0),
                      priority
              ), parameters, result)
            }

          }

        }

        true

      }

      return
    }

    for (clazz in currentPackage.getClasses(scope)) {

      for (innerClass in clazz.findAllStaticInnerClasses()) {

        if (!innerClass.isAnnotationType && !innerClass.isInterface && !innerClass.hasModifierProperty(PsiModifier.ABSTRACT)) {

          innerClass.putDollarInInnerClassName().let { fqName ->

            val priority = classPriority(fqName)
            doAdd(
                    PrioritizedLookupElement.withPriority(
                            LookupElementBuilder.create(innerClass, fqName).withIcon(ICON_CLASS).withBoldness(priority > 0.0),
                            priority
                    ),
                    parameters,
                    result
            )

          }

        }
      }

    }

  }

  private fun doAdd(element: LookupElement, parameters: CompletionParameters, result: CompletionResultSet) {
    val dummyText = parameters.position.text
    if (dummyText.firstOrNull() == '"') {
      result.withPrefixMatcher(result.prefixMatcher.prefix.substring(1)).addElement(element)
      return
    }
    result.addElement(element)
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

    val ICON_TAG: Icon = AllIcons.Ide.Link
    val ICON_CLASS: Icon = PlatformIcons.CLASS_ICON
    val ICON_PACKAGE: Icon = PlatformIcons.PACKAGE_ICON
    val ICON_RESOURCE: Icon = AllIcons.Nodes.KeymapOther
    val ICON_ATLAS: Icon = AllIcons.Nodes.ModuleGroup
    val ICON_BITMAP_FONT: Icon = AllIcons.Nodes.ExtractedFolder
    val ICON_TINTED_DRAWABLE: Icon = AllIcons.Nodes.KeymapOther
    val ICON_FIELD: Icon = PlatformIcons.FIELD_ICON
    val ICON_PARENT: Icon = AllIcons.Nodes.UpLevel
  }
}