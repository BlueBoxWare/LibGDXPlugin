package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.GdxJsonFileImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet


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
class GdxJsonParserDefinition: ParserDefinition {

  override fun createParser(project: Project?): PsiParser = GdxJsonParser()

  override fun createFile(viewProvider: FileViewProvider): PsiFile = GdxJsonFileImpl(viewProvider)

  override fun getStringLiteralElements(): TokenSet = STRINGS

  override fun getFileNodeType(): IFileElementType = FILE

  override fun createLexer(project: Project?): Lexer = GdxJsonLexer()

  override fun createElement(node: ASTNode?): PsiElement = Factory.createElement(node)

  override fun getCommentTokens(): TokenSet = COMMENTS

  companion object {

    val FILE = IFileElementType(LibGDXJsonLanuage.INSTANCE)
    val COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)
    val CONTAINERS = TokenSet.create(JOBJECT, ARRAY)
    val STRINGS = TokenSet.create(STRING)

  }

}