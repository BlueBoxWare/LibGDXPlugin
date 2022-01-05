package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasLexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

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
class AtlasSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val COLON = createTextAttributesKey("ATLAS.COLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = createTextAttributesKey("ATLAS.COMMA")

        // The following are handled by AtlasAdditionalHighlighter
        val FILE_NAME = createTextAttributesKey("ATLAS.FILE_NAME", DefaultLanguageHighlighterColors.KEYWORD)
        val TEXTURE_NAME =
            createTextAttributesKey("ATLAS.TEXTURE_NAME", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val KEY = createTextAttributesKey("ATLAS.KEY", DefaultLanguageHighlighterColors.CLASS_NAME)
        val VALUE = createTextAttributesKey("ATLAS.VALUE", DefaultLanguageHighlighterColors.STRING)
    }

    override fun getTokenHighlights(tokenType: IElementType?) = arrayOf(
        when (tokenType) {
            AtlasElementTypes.COMMA -> COMMA
            AtlasElementTypes.COLON -> COLON
            else -> VALUE
        }
    )

    override fun getHighlightingLexer() = AtlasLexer()
}
