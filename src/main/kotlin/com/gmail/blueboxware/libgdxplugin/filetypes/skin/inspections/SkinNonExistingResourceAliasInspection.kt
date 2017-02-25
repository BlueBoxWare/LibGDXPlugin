package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
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
class SkinNonExistingResourceAliasInspection : SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.resource.alias.description")

  override fun getID() = "LibGDXSkinNonExistingResourceInAlias"

  override fun getDisplayName() = message("skin.inspection.non.existing.resource.alias.display.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

    override fun visitResource(resource: SkinResource) {
      (resource.value as? SkinStringLiteral)?.let { stringLiteral ->
        if (stringLiteral.reference?.resolve() == null) {
          val type = resource.classSpecification?.classNameAsString ?: "<unknown>"
          holder.registerProblem(stringLiteral, message("skin.inspection.non.existing.resource.alias.message", stringLiteral.value, type))
        }
      }
    }
  }

}