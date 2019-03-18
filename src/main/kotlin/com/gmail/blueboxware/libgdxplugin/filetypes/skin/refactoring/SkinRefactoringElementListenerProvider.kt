package com.gmail.blueboxware.libgdxplugin.filetypes.skin.refactoring

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaClassReference
import com.gmail.blueboxware.libgdxplugin.utils.findAllInnerClasses
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPackage
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.psi.KtClass

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
class SkinRefactoringElementListenerProvider: RefactoringElementListenerProvider {

  override fun getListener(element: PsiElement?): RefactoringElementListener? {
    val classes = if (element is PsiClass || element is KtClass) {
      arrayOf(element)
    } else if (element is PsiPackage) {
      element.classes
    } else {
      return null
    }

    val refToClassMap = mutableMapOf<SkinJavaClassReference, PsiClass>()

    for (clazz in classes) {
      (((clazz as? KtClass)?.toLightClass() ?: clazz) as? PsiClass)?.let { psiClass ->

        for (innerClass in psiClass.findAllInnerClasses()) {
          ReferencesSearch.search(innerClass).forEach { reference ->
            if (reference is SkinJavaClassReference) {
              refToClassMap[reference] = innerClass
            }
          }
        }
      }

    }

    if (refToClassMap.isNotEmpty()) {
      return MyRefactoringElementListener(refToClassMap)
    } else {
      return null
    }

  }

  class MyRefactoringElementListener(private val refToClassMap: Map<SkinJavaClassReference, PsiClass>): RefactoringElementListener {

    override fun elementRenamed(newElement: PsiElement) = refactored()

    override fun elementMoved(newElement: PsiElement) = refactored()

    private fun refactored() {

      ApplicationManager.getApplication().runWriteAction {
        refToClassMap.forEach { reference, clazz ->
          reference.bindToElement(clazz)
        }
      }

    }

  }

}