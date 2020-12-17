package com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForObjectFix
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForPropertyFix
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.SuppressForStringFix
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import kotlin.math.min


/*
 * Copyright 2019 Blue Box Ware
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
class LibGDXJsonStringProblemsInspector: GdxJsonBaseInspection() {

  override fun getStaticDescription() = message("json.inspection.invalid.escape.description")

  override fun getID() = "LibGDXJsonInvalidEscape"

  override fun getDisplayName() = message("json.inspection.invalid.escape.display.name")

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
          arrayOf(
                  SuppressForFileFix(getShortID()),
                  SuppressForObjectFix(getShortID()),
                  SuppressForPropertyFix(getShortID()),
                  SuppressForStringFix(getShortID())
          )

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =

          object: GdxJsonElementVisitor() {

            override fun visitFile(file: PsiFile) {
              file.children.filter {
                (it as? LeafPsiElement)?.elementType == GdxJsonElementTypes.DOUBLE_QUOTED_STRING
              }.forEach {
                if (it.text.firstOrNull() == '"') {
                  if (it.text.length == 1 || it.text.lastOrNull() != '"') {
                    holder.registerProblem(
                            it, message("json.inspection.invalid.escape.missing.quote")
                    )
                  }
                }
              }
            }

            override fun visitString(o: GdxJsonString) {

              var i = 0

              while (i < o.text.length - 1) {

                if (o.text[i] != '\\') {
                  i++
                  continue
                }

                val c = o.text[i + 1]

                if (c == 'u') {
                  try {
                    Character.toChars(Integer.parseInt(o.text.substring(i + 2, i + 6), 16))
                  } catch (e: Exception) {
                    val maxlen = if (o.isQuoted) o.text.length - 1 else o.text.length
                    holder.registerProblem(
                            o, TextRange(i, min(i + 6, maxlen)), message("json.inspection.invalid.escape.message")
                    )
                  }
                  i += 4
                  continue
                } else {
                  i++
                }

                if (c !in ESCAPABLE_CHARS) {
                  holder.registerProblem(
                          o, TextRange(i - 1, min(i + 1, o.text.length)),
                          message("json.inspection.invalid.escape.message")
                  )
                }

              }

            }

          }

  companion object {
    val ESCAPABLE_CHARS = setOf('"', '\\', '/', 'b', 'f', 'n', 'r', 't')
  }

}