package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
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
class SkinNonExistingFieldInspection: SkinBaseInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.field.description")

  override fun getID() = "LibGDXSkinNonExistingField"

  override fun getDisplayName() = message("skin.inspection.non.existing.field.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: SkinElementVisitor() {

    override fun visitPropertyName(propertyName: SkinPropertyName) {
      val name = propertyName.value

      if (name == PROPERTY_NAME_PARENT && propertyName.project.isLibGDX199()) {
        return
      }

      val property = propertyName.property ?: return
      val typeString = propertyName.property?.containingObject?.resolveToTypeString() ?: return

      if (typeString == COLOR_CLASS_NAME && name == "hex") {
        return
      } else if (typeString == BITMAPFONT_CLASS_NAME) {
        if (!listOf(PROPERTY_NAME_FONT_FILE, PROPERTY_NAME_FONT_SCALED_SIZE, PROPERTY_NAME_FONT_FLIP, PROPERTY_NAME_FONT_MARKUP).contains(name)) {
          holder.registerProblem(propertyName, message("skin.inspection.non.existing.field.message.BitmapFont", name))
        }
        return
      } else if (typeString == FREETYPE_FONT_PARAMETER_CLASS_NAME && name == "font") {
        return
      }

      if (property.resolveToField() == null) {
        holder.registerProblem(propertyName, message("skin.inspection.non.existing.field.message", typeString, name))
      }

    }

  }

}