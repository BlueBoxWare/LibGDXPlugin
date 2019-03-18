package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor


/*
 * Copyright 2018 Blue Box Ware
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
class SkinDeprecatedInspection: SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.deprecated.description")

  override fun getID() = "LibGDXSkinDeprecated"

  override fun getDisplayName() = message("skin.inspection.deprecated.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object: SkinElementVisitor() {

    override fun visitClassName(className: SkinClassName) {

      if (className.resolve()?.isDeprecated == true) {
        holder.registerProblem(className, message("skin.inspection.deprecated.message", className.value.plainName), ProblemHighlightType.LIKE_DEPRECATED)
      }

    }

    override fun visitPropertyName(propertyName: SkinPropertyName) {

      propertyName.property?.let { property ->
        if (property.resolveToField()?.isDeprecated == true) {
          holder.registerProblem(propertyName, message("skin.inspection.deprecated.message", propertyName.value), ProblemHighlightType.LIKE_DEPRECATED)
        }
      }

    }

  }

}