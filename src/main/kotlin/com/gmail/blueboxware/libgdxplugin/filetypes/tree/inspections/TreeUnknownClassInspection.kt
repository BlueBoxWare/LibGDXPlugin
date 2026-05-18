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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiReference

internal class TreeUnknownClassInspection : TreeBaseInspection() {

    override fun getStaticDescription() = message("tree.inspection.unknown.class.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : PsiTreeElementVisitor() {
            override fun visitVstring(o: PsiTreeVstring) {
                if (o.parent is PsiTreeValue && o.parent?.parent is PsiTreeAttribute && o.parent?.parent?.parent is PsiTreeImport) {
                    check(holder, o.references, o)
                }
            }

            override fun visitTaskname(o: PsiTreeTaskname) {
                check(holder, o.references, o)
            }

            private fun check(
                holder: ProblemsHolder,
                references: Array<out PsiReference?>,
                element: NavigatablePsiElement
            ) {
                if (references.map { it?.resolve() }.none { it is PsiClass || it is PsiTreeAttribute }) {
                    holder.registerProblem(
                        element,
                        message("tree.inspection.unknown.class.message", element.name ?: "")
                    )
                }
            }
        }

}
