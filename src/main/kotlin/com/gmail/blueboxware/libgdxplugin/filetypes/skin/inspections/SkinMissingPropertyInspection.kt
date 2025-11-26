package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
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
internal class SkinMissingPropertyInspection : SkinBaseInspection() {

    override fun getStaticDescription() = message("skin.inspection.missing.property.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : SkinElementVisitor() {

            override fun visitObject(skinObject: SkinObject) {

                val mandatoryProperties =
                    when (skinObject.resolveToTypeString()) {
                        BITMAPFONT_CLASS_NAME -> setOf(PROPERTY_NAME_FONT_FILE)
                        TINTED_DRAWABLE_CLASS_NAME -> listOf(
                            PROPERTY_NAME_TINTED_DRAWABLE_NAME, PROPERTY_NAME_TINTED_DRAWABLE_COLOR
                        )

                        else -> null
                    }

                mandatoryProperties?.forEach { property ->
                    if (!skinObject.getPropertyNames().contains(property)) {
                        holder.registerProblem(
                            skinObject,
                            message("skin.inspection.missing.property.message", property)
                        )
                    }
                }


            }

        }

}
