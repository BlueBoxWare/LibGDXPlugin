package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType


/*
 * Copyright 2022 Blue Box Ware
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
class Atlas2SyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val COLON = TextAttributesKey.createTextAttributesKey("ATLAS.COLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = TextAttributesKey.createTextAttributesKey("ATLAS.COMMA")

        // The following are handled by AtlasAdditionalHighlighter
        val FILE_NAME =
            TextAttributesKey.createTextAttributesKey("ATLAS.FILE_NAME", DefaultLanguageHighlighterColors.KEYWORD)
        val TEXTURE_NAME =
            TextAttributesKey.createTextAttributesKey(
                "ATLAS.TEXTURE_NAME",
                DefaultLanguageHighlighterColors.INSTANCE_FIELD
            )
        val KEY = TextAttributesKey.createTextAttributesKey("ATLAS.KEY", DefaultLanguageHighlighterColors.CLASS_NAME)
        val VALUE = TextAttributesKey.createTextAttributesKey("ATLAS.VALUE", DefaultLanguageHighlighterColors.STRING)
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = pack(
        when (tokenType) {
            Atlas2ElementTypes.COMMA -> COMMA
            Atlas2ElementTypes.COLON -> COLON
            Atlas2ElementTypes.KEY -> KEY
            else -> VALUE
        }
    )

    override fun getHighlightingLexer() = Atlas2Lexer()
}
