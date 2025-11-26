package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.intellij.codeInspection.ProblemsHolder

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
val colorRegex = Regex("""#?([0-9a-fA-F]{2}){3,4}""")

internal class SkinMalformedColorStringInspection : SkinBaseInspection() {
    override fun getStaticDescription() = message("skin.inspection.malformed.color.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

        override fun visitPropertyValue(o: SkinPropertyValue) {
            if (o.getProperty()?.getContainingObject()
                    ?.resolveToTypeString() == COLOR_CLASS_NAME && o.getProperty()?.name == "hex"
            ) {
                (o.value as? SkinStringLiteral)?.getValue()?.let { str ->
                    if (!colorRegex.matches(str)) {
                        holder.registerProblem(o, message("skin.inspection.malformed.color.display.name"))
                    }
                }
            }
        }
    }

}

