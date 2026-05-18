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

import andel.intervals.toReversedList
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeFile
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeIndent
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.impl.source.codeStyle.PostFormatProcessor
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.parentOfType

internal class TreeIndentPostFormatProcessor : PostFormatProcessor {
    override fun processElement(
        source: PsiElement, settings: CodeStyleSettings
    ): PsiElement {
        if (source.language !is TreeLanguage || !shouldReformat(settings)) return source

        process(source, settings)

        return source

    }

    override fun processText(
        source: PsiFile, rangeToReformat: TextRange, settings: CodeStyleSettings
    ): TextRange {
        if (source.language != TreeLanguage || !shouldReformat(settings) || rangeToReformat != source.textRange) {
            return rangeToReformat
        }

        process(source, settings)

        return rangeToReformat
    }

    private fun shouldReformat(codeStyleSettings: CodeStyleSettings): Boolean =
        !codeStyleSettings.getCustomSettings(TreeCodeStyleSettings::class.java).KEEP_INDENT

    private fun process(source: PsiElement, codeStyleSettings: CodeStyleSettings) {
        source.descendantsOfType<TreeIndent>().toReversedList().forEach { treeIndent ->
            reformat(treeIndent, codeStyleSettings)
        }
    }

    private fun reformat(source: TreeIndent, codeStyleSettings: CodeStyleSettings) {

        val settings = codeStyleSettings.getCustomSettings(TreeCodeStyleSettings::class.java)
        val keepComments = settings.KEEP_COMMENTS
        val keepEmpty =
            settings.KEEP_INDENTS_ON_EMPTY_LINES
        val commentsOnFirstColumn = settings.LINE_COMMENT_AT_FIRST_COLUMN
        val levelsByElement = (source.containingFile as? TreeFile)?.getLevelsByElement() ?: return

        source.parentOfType<PsiTreeLine>()?.let { treeLine ->
            if (treeLine.isEmpty()) {
                if (keepComments && treeLine.hasComment()) {
                    return@let
                }
                if (keepEmpty && !treeLine.hasComment()) {
                    return@let
                }
            }
            levelsByElement.getOrDefault(treeLine, 0).let { level ->
                val newLevel =
                    if (treeLine.isEmpty() && !treeLine.hasComment()) 0 else if (commentsOnFirstColumn && treeLine.isEmpty() && treeLine.hasComment()) 0 else level
                TreeElementFactory.createIndent(source.project, newLevel)?.let { newIndent ->
                    if (newIndent.text != source.text) {
                        source.replace(newIndent)
                        return
                    }
                }
            }
        }

    }
}
