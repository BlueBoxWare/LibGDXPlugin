package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
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
class SkinMissingPropertyInspection : SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.missing.property.description")

  override fun getID() = "LibGDXSkinMissingProperty"

  override fun getDisplayName() = message("skin.inspection.missing.property.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object : SkinElementVisitor() {

    override fun visitObject(skinObject: SkinObject) {

      val className = skinObject.resolveToTypeString()

      val mandatoryProperties = if (className == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
        setOf("file")
      } else if (className == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable") {
        listOf("name", "color")
      } else {
        null
      }

      mandatoryProperties?.forEach { property ->
        if (!skinObject.propertyNames.contains(property)) {
          holder.registerProblem(skinObject, message("skin.inspection.missing.property.message", property))
        }
      }


    }

  }

}