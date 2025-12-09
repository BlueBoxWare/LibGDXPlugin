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
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeLine
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.util.descendantsOfType

internal class TreeEmptyLinesPostFormatProcessor : PostFormatProcessor {

    override fun isWhitespaceOnly(): Boolean = true

    override fun processElement(
        source: PsiElement,
        settings: CodeStyleSettings
    ): PsiElement {
        if (source.language != TreeLanguage) return source

        process(source, settings, source.textRange)

        return source
    }

    override fun processText(
        source: PsiFile,
        rangeToReformat: TextRange,
        settings: CodeStyleSettings
    ): TextRange {
        if (source.language != TreeLanguage) return rangeToReformat

        process(source, settings, rangeToReformat)

        return rangeToReformat

    }

    private fun process(source: PsiElement, settings: CodeStyleSettings, rangeToReformat: TextRange) {

        var count = 0
        val linesToKeep = settings.getCommonSettings(TreeLanguage).KEEP_BLANK_LINES_IN_CODE

        val seq =
            source.descendantsOfType<TreeLine>().filter { line -> rangeToReformat.intersects(line.textRange) }.toList()
        seq.forEach { line ->
            if (line.isEmpty() && !line.hasComment()) {
                count++
                if (count > linesToKeep) {
                    line.delete()
                }
            } else {
                count = 0
            }
        }

    }
}
