package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiType

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
class SkinTypeInspection: SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.types.description")

  override fun getID() = "LibGDXSkinTypeError"

  override fun getDisplayName() = message("skin.inspection.types.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object: SkinElementVisitor() {

    override fun visitValue(skinValue: SkinValue) {

      if (skinValue.parent is SkinPropertyName
              || skinValue.parent is SkinClassName
              || skinValue.parent is SkinResourceName
              ) {
        return
      }

      val expectedType = skinValue.resolveToType()
      val property = skinValue.property
      val containingClassName = property?.containingObject?.resolveToClass()?.qualifiedName
      val propertyName = property?.propertyName?.value

      if (expectedType is PsiArrayType) {
        if (skinValue !is SkinArray) {
          holder.registerProblem(skinValue, message("skin.inspection.types.array.expected"))
        }
      } else if (expectedType == PsiType.BOOLEAN
              || (containingClassName == "com.badlogic.gdx.graphics.g2d.BitmapFont" && listOf("flip", "markupEnabled").contains(propertyName))
                     ) {
        if (skinValue !is SkinBooleanLiteral) {
          holder.registerProblem(skinValue, message("skin.inspection.types.boolean.expected"))
        }
      } else if (expectedType == PsiType.INT
        || (containingClassName == "com.badlogic.gdx.graphics.g2d.BitmapFont" && propertyName == "scaledSize")
                                            ) {
        if (SkinPsiUtil.stripQuotes(skinValue.text).toIntOrNull() == null) {
          holder.registerProblem(skinValue, message("skin.inspection.types.int.expected"))
        }
      } else if (expectedType is PsiClassType && expectedType.canonicalText != "java.lang.String") {
        if (skinValue !is SkinStringLiteral && skinValue !is SkinObject) {
          holder.registerProblem(skinValue, message("skin.inspection.types.resource.expected"))
        }
      }

    }

  }

}