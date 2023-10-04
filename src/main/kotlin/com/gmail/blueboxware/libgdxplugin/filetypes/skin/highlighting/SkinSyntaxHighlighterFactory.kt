package com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinLexer
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory.*
import com.intellij.lexer.LayeredLexer
import com.intellij.lexer.Lexer
import com.intellij.lexer.StringLiteralLexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.StringEscapesTokenTypes
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

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

val SKIN_INVALID_ESCAPE = TextAttributesKey.createTextAttributesKey("SKIN.INVALID_ESCAPE", JSON_INVALID_ESCAPE)
val SKIN_BRACKETS = TextAttributesKey.createTextAttributesKey("SKIN.BRACKETS", JSON_BRACKETS)
val SKIN_BRACES = TextAttributesKey.createTextAttributesKey("SKIN.BRACES", JSON_BRACES)
val SKIN_COMMA = TextAttributesKey.createTextAttributesKey("SKIN.COMMA", JSON_COMMA)
val SKIN_COLON = TextAttributesKey.createTextAttributesKey("SKIN.COLON", JSON_COLON)

val SKIN_NUMBER = TextAttributesKey.createTextAttributesKey("SKIN.NUMBER", JSON_NUMBER)
val SKIN_STRING = TextAttributesKey.createTextAttributesKey("SKIN.STRING", JSON_STRING)

val SKIN_KEYWORD = TextAttributesKey.createTextAttributesKey("SKIN.KEYWORD", JSON_KEYWORD)

val SKIN_LINE_COMMENT = TextAttributesKey.createTextAttributesKey("SKIN.LINE_COMMENT", JSON_LINE_COMMENT)
val SKIN_BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("SKIN.BLOCK_COMMENT", JSON_BLOCK_COMMENT)

val SKIN_CLASS_NAME = TextAttributesKey.createTextAttributesKey("SKIN.CLASSNAME", JSON_KEYWORD)
val SKIN_RESOURCE_NAME = TextAttributesKey.createTextAttributesKey("SKIN.RESOURCENAME", JSON_IDENTIFIER)
val SKIN_PROPERTY_NAME = TextAttributesKey.createTextAttributesKey("SKIN.PROPERTY_NAME", JSON_PROPERTY_KEY)
val SKIN_PARENT_PROPERTY =
    TextAttributesKey.createTextAttributesKey("SKIN.PARENT_PROPERTY", DefaultLanguageHighlighterColors.KEYWORD)

val SKIN_VALID_ESCAPE = TextAttributesKey.createTextAttributesKey("SKIN.VALID_ESCAPE", JSON_VALID_ESCAPE)

internal class SkinSyntaxHighlighterFactory : SyntaxHighlighterFactory() {

    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = SkinHighlighter()


    class SkinHighlighter : SyntaxHighlighterBase() {

        companion object {
            val attributes = mapOf(
                SkinElementTypes.L_CURLY to SKIN_BRACES,
                SkinElementTypes.R_CURLY to SKIN_BRACES,

                SkinElementTypes.L_BRACKET to SKIN_BRACKETS,
                SkinElementTypes.R_BRACKET to SKIN_BRACKETS,

                SkinElementTypes.COMMA to SKIN_COMMA,
                SkinElementTypes.COLON to SKIN_COLON,

                SkinElementTypes.DOUBLE_QUOTED_STRING to SKIN_STRING,
                SkinElementTypes.UNQUOTED_STRING to SKIN_STRING,

                SkinElementTypes.LINE_COMMENT to SKIN_LINE_COMMENT,
                SkinElementTypes.BLOCK_COMMENT to SKIN_BLOCK_COMMENT,

                SkinElementTypes.PROPERTY_NAME to SKIN_PROPERTY_NAME,
                SkinElementTypes.RESOURCE_NAME to SKIN_RESOURCE_NAME,

                TokenType.BAD_CHARACTER to HighlighterColors.BAD_CHARACTER,

                StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN to SKIN_VALID_ESCAPE,
                StringEscapesTokenTypes.INVALID_CHARACTER_ESCAPE_TOKEN to SKIN_INVALID_ESCAPE,
                StringEscapesTokenTypes.INVALID_UNICODE_ESCAPE_TOKEN to SKIN_INVALID_ESCAPE
            )
        }

        override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
            return pack(attributes[tokenType])
        }

        override fun getHighlightingLexer(): Lexer {
            val layeredLexer = LayeredLexer(SkinLexer())
            layeredLexer.registerSelfStoppingLayer(
                StringLiteralLexer('\"', SkinElementTypes.DOUBLE_QUOTED_STRING, false, "/", false, false),
                arrayOf(SkinElementTypes.DOUBLE_QUOTED_STRING), IElementType.EMPTY_ARRAY
            )
            return layeredLexer
        }
    }
}

