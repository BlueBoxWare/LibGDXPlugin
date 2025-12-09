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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLexerAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class TreeSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = TreeLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<out TextAttributesKey?> {
        val attributes = when (tokenType) {
            TreeElementTypes.COLON -> COLON
            TreeElementTypes.LPAREN, TreeElementTypes.RPAREN -> PAREN
            TreeElementTypes.TRUE, TreeElementTypes.FALSE -> BOOL
            TreeElementTypes.TASKNAME -> TASKNAME
            TreeElementTypes.STRING -> STRING
            TreeElementTypes.NUMBER -> NUMBER
            TreeElementTypes.COMMENT -> COMMENT
            TreeElementTypes.ATTRNAME -> ATTRNAME
            TreeElementTypes.NULL -> NULL
            TreeElementTypes.SUBTREEREF -> SUBTREEREF
            TreeElementTypes.QUESTION_MARK -> QUESTION_MARK
            TreeElementTypes.TIMPORT -> IMPORT
            TreeElementTypes.TROOT -> ROOT
            TreeElementTypes.TSUBTREE -> SUBTREE
            else -> null
        }
        return attributes?.let { arrayOf(it) } ?: emptyArray()
    }

    companion object {

        val COLON = createTextAttributesKey("COLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val PAREN = createTextAttributesKey("PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val QUESTION_MARK = createTextAttributesKey("QUESTION MARK", DefaultLanguageHighlighterColors.STRING)

        val TASKNAME = createTextAttributesKey("TASKNAME", DefaultLanguageHighlighterColors.CLASS_NAME)
        val ATTRNAME = createTextAttributesKey("ATTRNAME", DefaultLanguageHighlighterColors.KEYWORD)
        val SUBTREEREF = createTextAttributesKey("SUBTREEREF", DefaultLanguageHighlighterColors.CLASS_REFERENCE)
        val COMMENT = createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)

        val NUMBER = createTextAttributesKey("NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val STRING = createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING)
        val BOOL = createTextAttributesKey("BOOL", DefaultLanguageHighlighterColors.CONSTANT)
        val NULL = createTextAttributesKey("NULL", DefaultLanguageHighlighterColors.CONSTANT)

        val IMPORT = createTextAttributesKey("IMPORT", DefaultLanguageHighlighterColors.KEYWORD)
        val ROOT = createTextAttributesKey("ROOT", DefaultLanguageHighlighterColors.KEYWORD)
        val SUBTREE = createTextAttributesKey("SUBTREE", DefaultLanguageHighlighterColors.KEYWORD)

    }
}
