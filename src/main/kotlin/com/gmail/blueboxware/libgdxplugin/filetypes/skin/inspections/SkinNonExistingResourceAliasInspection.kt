package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.putDollarInInnerClassName
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiClassType

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
class SkinNonExistingResourceAliasInspection : SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.resource.alias.description")

  override fun getID() = "LibGDXSkinNonExistingResourceInAlias"

  override fun getDisplayName() = message("skin.inspection.non.existing.resource.alias.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = if (ApplicationManager.getApplication().isUnitTestMode) HighlightDisplayLevel.ERROR else HighlightDisplayLevel.WARNING

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

    override fun visitStringLiteral(stringLiteral: SkinStringLiteral) {

      if (stringLiteral.parent is SkinPropertyName
              || stringLiteral.parent is SkinClassName
              || stringLiteral.parent is SkinResourceName
              ) {
        return
      }

      val type = stringLiteral.resolveToType() as? PsiClassType ?: return
      val typeString = type.canonicalText
      val clazz = type.resolve() ?: return

      if (typeString == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" || typeString == "java.lang.String") {
        return
      }

      (stringLiteral.containingFile as? SkinFile)?.let { skinFile ->
        if (skinFile.getResources(clazz.putDollarInInnerClassName(), stringLiteral.value, beforeElement = stringLiteral).isEmpty()) {
          holder.registerProblem(stringLiteral, message("skin.inspection.non.existing.resource.alias.message", stringLiteral.value))
        }
      }
    }

  }

}