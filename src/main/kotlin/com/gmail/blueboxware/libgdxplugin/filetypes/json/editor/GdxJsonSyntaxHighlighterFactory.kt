package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonLexer
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonSyntaxHighlighterFactory: SyntaxHighlighterFactory() {

  override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = GdxJsonHighlighter()

  class GdxJsonHighlighter: SyntaxHighlighterBase() {

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = pack(attributes[tokenType])

    override fun getHighlightingLexer(): Lexer = GdxJsonLexer()

    companion object {

      val attributes = mapOf(
              GdxJsonElementTypes.L_CURLY to JsonSyntaxHighlighterFactory.JSON_BRACES,
              GdxJsonElementTypes.R_CURLY to JsonSyntaxHighlighterFactory.JSON_BRACES,

              GdxJsonElementTypes.L_BRACKET to JsonSyntaxHighlighterFactory.JSON_BRACKETS,
              GdxJsonElementTypes.R_BRACKET to JsonSyntaxHighlighterFactory.JSON_BRACKETS,

              GdxJsonElementTypes.COMMA to JsonSyntaxHighlighterFactory.JSON_COMMA,
              GdxJsonElementTypes.COLON to JsonSyntaxHighlighterFactory.JSON_COLON
      )

    }

  }

}