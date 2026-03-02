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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

internal class TreeBraceMatcher : PairedBraceMatcherAdapter(matcher, TreeLanguage) {
    override fun findPair(
        left: Boolean, iterator: HighlighterIterator?, fileText: CharSequence?, fileType: FileType?
    ): BracePair? {
        val tokenType = iterator?.tokenType ?: return null
        PAIRS.forEach {
            if ((left && tokenType in listOf(
                    it.leftBraceType,
                    TreeElementTypes.EOL
                )) || (!left && tokenType == it.rightBraceType)
            ) {
                return it
            }
        }

        return null
    }
}

private val matcher = object : PairedBraceMatcher {

    override fun getPairs(): Array<out BracePair?> = PAIRS

    override fun isPairedBracesAllowedBeforeType(
        lbraceType: IElementType, contextType: IElementType?
    ): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}

private val PAIRS = arrayOf(BracePair(TreeElementTypes.LPAREN, TreeElementTypes.RPAREN, true))
