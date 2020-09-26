package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.quickfixes.CreateAssetQuickFix
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinResourceReference
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName
import com.gmail.blueboxware.libgdxplugin.utils.TINTED_DRAWABLE_CLASS_NAME
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.application.ApplicationManager

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
class SkinNonExistingResourceAliasInspection: SkinBaseInspection() {

  override fun getStaticDescription() = message("skin.inspection.non.existing.resource.alias.description")

  override fun getID() = "LibGDXSkinNonExistingResourceInAlias"

  override fun getDisplayName() = message("skin.inspection.non.existing.resource.alias.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel =
          if (ApplicationManager.getApplication().isUnitTestMode) {
            HighlightDisplayLevel.ERROR
          } else {
            HighlightDisplayLevel.WARNING
          }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
          object: SkinElementVisitor() {

            override fun visitStringLiteral(stringLiteral: SkinStringLiteral) {

              if (stringLiteral.parent is SkinPropertyName
                      || stringLiteral.parent is SkinClassName
                      || stringLiteral.parent is SkinResourceName
              ) {
                return
              }

              val reference = stringLiteral.reference

              if (reference is SkinResourceReference) {
                val referent = reference.resolve()
                if (referent == null) {
                  val quickfix = stringLiteral.resolveToClass()?.let { clazz ->

                    if (clazz.qualifiedName == "java.lang.String") {
                      null
                    } else if (clazz.qualifiedName == TINTED_DRAWABLE_CLASS_NAME && stringLiteral.context is SkinResource) {
                      null
                    } else {
                      CreateAssetQuickFix(stringLiteral, stringLiteral.value, DollarClassName(clazz))
                    }

                  }
                  holder.registerProblem(
                          stringLiteral,
                          message("skin.inspection.non.existing.resource.alias.message", stringLiteral.value),
                          quickfix
                  )
                }
              }

            }

          }

}