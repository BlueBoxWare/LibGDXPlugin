package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiModifier

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
class SkinNonExistingClassInspection : SkinBaseInspection() {

    override fun getStaticDescription() = message("skin.inspection.non.existing.class.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

        override fun visitClassName(o: SkinClassName) {

            val clazz = o.resolve()

            if (clazz == null) {
                holder.registerProblem(o, message("skin.inspection.non.existing.class.message", o.value.plainName))
            } else if (clazz.containingClass != null && !clazz.hasModifierProperty(PsiModifier.STATIC)) {
                holder.registerProblem(o, message("skin.inspection.non.static.class.message", o.value.plainName))
            }

        }
    }
}
