package com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull


/*
 * Copyright 2020 Blue Box Ware
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
internal class LibGDXTopLevelValueInspection : GdxJsonBaseInspection() {

    override fun getStaticDescription() = message("json.inspection.toplevel.value.description")

    override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
        arrayOf(
            SuppressForFileFix(getShortID()),
        )

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : GdxJsonElementVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file.children.filterIsInstance<GdxJsonValue>().size != 1) {
                    holder.registerProblem(file, message("json.inspection.toplevel.value.message"))
                } else {
                    file.children.firstIsInstanceOrNull<GdxJsonValue>()?.let { element ->
                        if (!element.isObject) {
                            holder.registerProblem(element, message("json.inspection.toplevel.value.message"))
                        }
                    }
                }
            }
        }

}
