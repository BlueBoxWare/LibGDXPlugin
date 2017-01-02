package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiModifier
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
    extend(CompletionType.BASIC, PlatformPatterns.psiElement(SkinElementTypes.CLASSNAME_STRING), object: CompletionProvider<CompletionParameters>() {
      override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
        classNameCompletion(parameters, result)
      }
    })
  }

  override fun beforeCompletion(context: CompletionInitializationContext) {
    context.dummyIdentifier = "com.example.Class"
  }

  private fun classNameCompletion(parameters: CompletionParameters, result : CompletionResultSet) {
    val prefix = result.prefixMatcher.prefix.dropLastWhile { it != '.' }.dropLastWhile { it == '.' }

    val project = parameters.position.project
    val psiFacade = JavaPsiFacade.getInstance(project)
    val rootPackage = psiFacade.findPackage(prefix) ?: return

    for (subpackage in rootPackage.subPackages) {
      result.addElement(LookupElementBuilder.create(subpackage, subpackage.qualifiedName))
    }

    val currentPackage = psiFacade.findPackage(prefix) ?: return

    for (clazz in currentPackage.classes) {
      if (!clazz.isAnnotationType && !clazz.isInterface && !clazz.hasModifierProperty(PsiModifier.ABSTRACT))
      result.addElement(JavaClassNameCompletionContributor.createClassLookupItem(clazz, false).apply {
        forcedPresentableName = clazz.qualifiedName
        setInsertHandler(BasicInsertHandler())
      })

      for (innerClass in clazz.innerClasses) {
        val fqName = clazz.qualifiedName + "$" + innerClass.name
        if (innerClass.hasModifierProperty(PsiModifier.PUBLIC)) {
          result.addElement(JavaClassNameCompletionContributor.createClassLookupItem(innerClass, false).apply {
            forcedPresentableName = fqName
            setInsertHandler(BasicInsertHandler())
          })
        }
      }
    }

  }
}