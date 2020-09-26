package com.gmail.blueboxware.libgdxplugin.filetypes.skin.refactoring

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.getSkinFiles
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.gmail.blueboxware.libgdxplugin.utils.toPsiFile
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.JavaDummyHolder
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
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
class ChangeKotlinPackageListener(val project: Project): PsiTreeChangeAdapter(), RefactoringElementListenerProvider {

  companion object {
    val oldPackageKey = key<String>("oldPackage")
    val newPackageKey = key<String>("newPackage")
  }

  override fun childRemoved(event: PsiTreeChangeEvent) {
    (event.child as? KtPackageDirective)?.fqName?.asString()?.let { oldPackage ->
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

    ((event.newChild.context as? KtPackageDirective)
            ?: (event.newChild as? KtPackageDirective))?.fqName?.asString()?.let { newPackage ->

      val ktFile = event.file as? KtFile ?: return
      val oldPackage =
              (event.oldChild as? KtPackageDirective)?.qualifiedName
                      ?: (event.oldChild as? KtDotQualifiedExpression)?.text
                      ?: return

      ktFile.putUserData(oldPackageKey, oldPackage)
      ktFile.putUserData(newPackageKey, newPackage)

    }

  }

  override fun getListener(element: PsiElement?): RefactoringElementListener? {

    if (element !is KtClass && element !is KtFile) {
      return null
    }

    return object: RefactoringElementListener {

      override fun elementRenamed(newElement: PsiElement) = refactored(newElement)

      override fun elementMoved(newElement: PsiElement) = refactored(newElement)

      fun refactored(element: PsiElement) {

        val ktFile = element.containingFile as? KtFile ?: element as? KtFile ?: return
        val oldPackage = ktFile.getUserData(oldPackageKey) ?: return
        val newPackage = ktFile.getUserData(newPackageKey) ?: return
        val currentPackage = ktFile.packageFqName.asString()

        if (currentPackage == newPackage) {

          val fqNames = if (element is PsiClass || element is KtClass) {
            element.getKotlinFqName()?.let { listOf(it) } ?: return
          } else if (element is KtFile) {
            element.classes.mapNotNull { it.getKotlinFqName() }.toList()
          } else {
            return
          }

          ApplicationManager.getApplication().runWriteAction {
            for (fqName in fqNames) {

              getSkinFiles(project).forEach {

                (it.toPsiFile(project) as? SkinFile)?.replacePackage(
                        fqName.shortName().asString(),
                        oldPackage,
                        newPackage
                )

              }
            }
          }

        }

      }

    }

  }

}