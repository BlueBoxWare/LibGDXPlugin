package com.gmail.blueboxware.libgdxplugin.filetypes.skin.refactoring

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.JavaDummyHolder
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.refactoring.toPsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPackageDirective

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
class ChangeKotlinPackageListener(val project: Project) : PsiTreeChangeAdapter(), RefactoringElementListenerProvider {

  companion object {
    val oldPackageKey = key<String>("oldPackage")
    val newPackageKey = key<String>("newPackage")
  }

  override fun childRemoved(event: PsiTreeChangeEvent) {
    (event.oldChild as? KtPackageDirective)?.fqName?.asString()?.let { oldPackage ->
      (event.file as? KtFile)?.let { ktFile ->
        ktFile.putUserData(oldPackageKey, oldPackage)
        ktFile.putUserData(newPackageKey, "")
      }
    }
  }

  override fun childReplaced(event: PsiTreeChangeEvent) {

    if (event.oldChild.context !is JavaDummyHolder || event.newChild is PsiWhiteSpace) {
      return
    }

    ((event.newChild.context as? KtPackageDirective) ?: (event.newChild as? KtPackageDirective))?.fqName?.asString()?.let { newPackage ->

      val ktFile = event.file as? KtFile ?: return
      val oldPackage = event.oldChild?.text ?: return

      ktFile.putUserData(oldPackageKey, oldPackage)
      ktFile.putUserData(newPackageKey, newPackage)

    }

  }

  override fun getListener(element: PsiElement?): RefactoringElementListener? {

    if (element !is KtClass) {
      return null
    }

    val ktFile = element.containingFile as? KtFile ?: return null
    val currentPackage = ktFile.packageDirective?.fqName?.asString() ?: ""
    val oldPackage = ktFile.getUserData(oldPackageKey) ?: return null
    val newPackage = ktFile.getUserData(newPackageKey) ?: return null
    val className = element.getKotlinFqName()?.shortName()?.asString() ?: return null

    if (newPackage == currentPackage) {
      return object: RefactoringElementListener {

        override fun elementRenamed(newElement: PsiElement) = refactored()

        override fun elementMoved(newElement: PsiElement) = refactored()

        fun refactored() {

          FileTypeIndex.getFiles(LibGDXSkinFileType.INSTANCE, GlobalSearchScope.projectScope(project)).forEach {

            (it.toPsiFile(project) as? SkinFile)?.replacePackage(className, oldPackage, newPackage)

          }

        }

      }
    }

    return null

  }

}