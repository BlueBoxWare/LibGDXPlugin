package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.message
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
class SkinNonExistingFieldInspection : SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.field.description")

  override fun getID() = "LibGDXSkinNonExistingField"

  override fun getDisplayName() = message("skin.inspection.non.existing.field.display.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

    override fun visitPropertyName(o: SkinPropertyName) {
      val property = o.property ?: return
      val classSpec = property.containingClassSpecification ?: return
      val className = classSpec.classNameAsString

      classSpec.resolveClass() ?: return

      if (className == "com.badlogic.gdx.graphics.Color") {
        if (o.value == "hex") {
          return
        }
      } else if (className == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
        if (!listOf("file", "scaledSize", "flip", "markupEnabled").contains(o.value)) {
          holder.registerProblem(o, message("skin.inspection.non.existing.field.message.BitmapFont", o.value))
        }
        return
      }

      if (property.resolveToField() == null) {
        holder.registerProblem(o, message("skin.inspection.non.existing.field.message", classSpec.classNameAsString, o.value))
      }
    }
  }

}