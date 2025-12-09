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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

internal class TreeBackspaceHandlerDelegate : BackspaceHandlerDelegate() {
    override fun beforeCharDeleted(
        c: Char, file: PsiFile, editor: Editor
    ) {
        if (file.fileType != TreeFileType) {
            return
        }
    }

    override fun charDeleted(
        c: Char, file: PsiFile, editor: Editor
    ): Boolean {
        if (file.fileType != TreeFileType || (c != ' ' && c != '\t')) {
            return false
        }

        val document = editor.document
        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset
        val currentColumn = caret.logicalPosition.column
        val currentLine = caret.logicalPosition.line
        if (document.getText(TextRange(document.getLineStartOffset(currentLine), offset))
                .any { it != ' ' && it != '\t' }
        ) {
            return false
        }
        for (line in currentLine - 1 downTo 0) {
            val range = TextRange(document.getLineStartOffset(line), document.getLineEndOffset(line))
            val text = document.getText(range)

            val pos = text.indexOfFirst { it != ' ' && it != '\t' }
            if (pos == -1 || text[pos] == '#') {
                continue
            }

            if (pos < currentColumn) {
                editor.document.deleteString(offset - (currentColumn - pos), offset)
                break
            }
        }

        return false
    }
}
