package com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForObjectFix
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForPropertyFix
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement


/*
 * Copyright 2019 Blue Box Ware
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
internal class LibGDXDuplicatePropertyInspection : GdxJsonBaseInspection() {

    override fun getStaticDescription() = message("json.inspection.duplicate.property.description")

    override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
        arrayOf(
            SuppressForFileFix(getShortID()),
            SuppressForObjectFix(getShortID()),
            SuppressForPropertyFix(getShortID())
        )

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : GdxJsonElementVisitor() {

        override fun visitJobject(o: GdxJsonJobject) {

            val properties = mutableMapOf<String, MutableList<GdxJsonPropertyName>>()

            o.propertyList.forEach { property ->
                property.name?.let { name ->
                    properties.getOrPut(name, ::mutableListOf).add(property.propertyName)
                }
            }

            properties.forEach { (name, propertyNames) ->
                if (propertyNames.size > 1) {
                    propertyNames.forEach { propertyName ->
                        holder.registerProblem(
                            propertyName,
                            message("json.inspection.duplicate.property.message", name)
                        )
                    }
                }
            }

        }

    }

}
