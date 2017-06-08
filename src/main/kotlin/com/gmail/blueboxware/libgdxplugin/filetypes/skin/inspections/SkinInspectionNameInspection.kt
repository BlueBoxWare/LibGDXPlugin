package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations.ANNOTATION_REGEX
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment

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
class SkinInspectionNameInspection: SkinFileInspection() {

  override fun getStaticDescription() = message("skin.inspection.inspection.name.description")

  override fun getID() = "LibGDXSkinNonExistingInspection"

  override fun getDisplayName() = message("skin.inspection.inspection.name.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : SkinElementVisitor() {

    override fun visitComment(comment: PsiComment?) {

      if (comment == null) return

      ANNOTATION_REGEX.findAll(comment.text ?: "").forEach { matchResult ->
        if (matchResult.groupValues.getOrNull(1)?.toLowerCase() == "suppress") {
          matchResult.groupValues.getOrNull(2)?.let {
            val inspectionName = StringUtil.stripQuotesAroundValue(it.trim()).trim()
            if (!SkinFileInspection.INSPECTION_NAMES.map(String::toLowerCase).contains(inspectionName.toLowerCase())) {
              matchResult.groups[2]?.range?.let { range ->
                val textRange = TextRange(range.start, range.endInclusive + 1)
                holder.registerProblem(comment, textRange, message("skin.inspection.inspection.name.message",
                        inspectionName,
                        SkinFileInspection.INSPECTION_NAMES.joinToString(", ")
                ))
              }
            }
          }
        }
      }
    }
  }

}