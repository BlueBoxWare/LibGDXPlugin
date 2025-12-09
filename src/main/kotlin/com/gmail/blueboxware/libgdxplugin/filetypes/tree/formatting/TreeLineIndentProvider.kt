/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.codeStyle.lineIndent.LineIndentProvider

internal class TreeLineIndentProvider : LineIndentProvider {
    override fun getLineIndent(
        project: Project, editor: Editor, language: Language?, offset: Int
    ): String {
        val document = editor.document
        val currentLine = document.getLineNumber(offset)
        for (line in currentLine - 1 downTo 0) {
            val range = TextRange(document.getLineStartOffset(line), document.getLineEndOffset(line))
            val text = document.getText(range)

            val pos = text.indexOfFirst { it != ' ' && it != '\t' }
            if (pos == -1 || text[pos] == '#') {
                continue
            }

            return text.take(pos)
        }

        return ""
    }

    override fun isSuitableFor(language: Language?): Boolean = language == TreeLanguage
}
