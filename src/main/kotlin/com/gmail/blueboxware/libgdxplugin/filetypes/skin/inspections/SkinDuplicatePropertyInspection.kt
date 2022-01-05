package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

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
class SkinDuplicatePropertyInspection : SkinBaseInspection() {

    override fun getStaticDescription() = message("skin.inspection.duplicate.property.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : SkinElementVisitor() {

            override fun visitObject(o: SkinObject) {

                val seenPropertyNames = mutableSetOf<String>()

                o.propertyList.forEach { property ->
                    if (seenPropertyNames.contains(property.name)) {
                        holder.registerProblem(
                            property.propertyName,
                            message("skin.inspection.duplicate.property.message", property.name)
                        )
                    } else {
                        seenPropertyNames.add(property.name)
                    }
                }

            }

        }

}
