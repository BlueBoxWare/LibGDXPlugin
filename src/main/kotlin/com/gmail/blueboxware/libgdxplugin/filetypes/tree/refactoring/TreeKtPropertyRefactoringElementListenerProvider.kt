/*
 * Copyright 2026 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.refactoring

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttributeName
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider
import com.intellij.refactoring.listeners.UndoRefactoringElementAdapter
import org.jetbrains.kotlin.idea.base.util.projectScope
import org.jetbrains.kotlin.psi.KtProperty

internal class TreeKtPropertyRefactoringElementListenerProvider : RefactoringElementListenerProvider {

    override fun getListener(element: PsiElement?): RefactoringElementListener? {

        if (element is KtProperty && element.project.isLibGDXProject()) {

            val scope = GlobalSearchScope.getScopeRestrictedByFileTypes(element.project.projectScope(), TreeFileType)
            val references = ReferencesSearch.search(element, scope).filterIsInstance<PsiTreeAttributeName>()

            if (references.isNotEmpty()) {
                return object : UndoRefactoringElementAdapter() {
                    override fun refactored(element: PsiElement, oldQualifiedName: String?) {
                        ApplicationManager.getApplication().runWriteAction {
                            references.forEach { reference ->
                                reference.bindToElement(element)
                            }
                        }
                    }

                }
            }

        }

        return null

    }

}
