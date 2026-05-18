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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeImport
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

internal class TreeUnknownAttributeInspection : TreeBaseInspection() {

    override fun getStaticDescription() = message("tree.inspection.unknown.attribute.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : PsiTreeElementVisitor() {
            override fun visitAttributeName(o: PsiTreeAttributeName) {
                if (o.parent.parent !is PsiTreeImport && o.reference?.resolve() == null) {
                    holder.registerProblem(o, message("tree.inspection.unknown.attribute.message", o.name ?: ""))
                }
            }
        }

}
