package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSkinFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.GutterColorRenderer
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPlainText
import java.awt.Color

/*
 * Copyright 2016 Blue Box Ware
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
class SkinColorPreviewLineMarkerProvider : LineMarkerProvider {

  private val number = """(?:(?:0|[1-9][0-9]*)(?:\.[0-9]+)?|\.[0-9]+)"""
  private val keyValueSeparator = """\s*:\s*"""
  private val colorPart = """[rgba]$keyValueSeparator$number"""
  private val nonStartPart = """\s*[,\n\r]+\s*($colorPart)"""

  private val keyValueSeparatorRegex = Regex(keyValueSeparator)
  private val colorRegex = Regex("""\{\s*($colorPart)$nonStartPart$nonStartPart(?:$nonStartPart)?\s*\}""")

  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiPlainText>? {
    return null
  }

  override fun collectSlowLineMarkers(elements: MutableList<PsiElement>, result: MutableCollection<LineMarkerInfo<PsiElement>>) {

    for (element in elements) {

      if (element !is PsiPlainText) return

      if (ServiceManager.getService(element.project, LibGDXPluginSettings::class.java)?.enableColorAnnotations != true) return

      element.containingFile?.virtualFile?.let { virtualFile ->
        if (element.project.getComponent(LibGDXProjectSkinFiles::class.java)?.contains(virtualFile) != true
                || !EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(virtualFile)
        ) {
          return
        }
      }

      val document = PsiDocumentManager.getInstance(element.project).getDocument(element.containingFile) ?: return
      val markupModel = DocumentMarkupModel.forDocument(document, element.project, false) ?: return

      ApplicationManager.getApplication().invokeLater {

        element.text?.let { text ->

          for (oldHightLighter in markupModel.allHighlighters) {
            if (oldHightLighter.gutterIconRenderer is GutterColorRenderer) {
              markupModel.removeHighlighter(oldHightLighter)
            }
          }

          val matches = colorRegex.findAll(element.text)

          for (match in matches) {

            var r = 0f
            var g = 0f
            var b = 0f
            var a = 1f

            for (part in match.groupValues.drop(1)) {
              val keyAndValue = part.split(keyValueSeparatorRegex)
              val key = keyAndValue.firstOrNull() ?: continue
              val value = keyAndValue.getOrNull(1) ?: continue

              val f = try {
                value.toFloat()
              } catch(e: NumberFormatException) {
                continue
              }

              when (key) {
                "r" -> r = f
                "g" -> g = f
                "b" -> b = f
                "a" -> a = f
              }

            }

            val color = try {
              Color(r, g, b, a)
            } catch (e: IllegalArgumentException) {
              continue
            }

            markupModel.addRangeHighlighter(
                    match.range.start,
                    match.range.endInclusive + 1,
                    HighlighterLayer.ADDITIONAL_SYNTAX,
                    null,
                    HighlighterTargetArea.LINES_IN_RANGE
            ).gutterIconRenderer = GutterColorRenderer(color)

          }

        }

      }

    }

  }

}