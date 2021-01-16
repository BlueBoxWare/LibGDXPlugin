package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.BITMAPFONT_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.PROPERTY_NAME_FONT_FILE
import com.intellij.codeHighlighting.HighlightDisplayLevel
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
class SkinNonExistingFontFileInspection: SkinBaseInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.file.description")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: SkinElementVisitor() {

    override fun visitPropertyValue(propertyValue: SkinPropertyValue) {

      val string = (propertyValue.value as? SkinStringLiteral) ?: return
      val property = propertyValue.property ?: return
      val className = property.containingObject?.resolveToTypeString() ?: return

      if (className == BITMAPFONT_CLASS_NAME && property.name == PROPERTY_NAME_FONT_FILE) {
        if (string.reference?.resolve() == null) {
          holder.registerProblem(propertyValue, message("skin.inspection.non.existing.file.message", string.value))
        }
      }

    }

  }

}