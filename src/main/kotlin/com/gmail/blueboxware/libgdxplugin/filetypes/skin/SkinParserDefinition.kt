package com.gmail.blueboxware.libgdxplugin.filetypes.skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

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
class SkinParserDefinition : ParserDefinition {

  companion object {
    val FILE = IFileElementType(LibGDXSkinLanguage.INSTANCE)

    val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
    val SKIN_COMMENTARIES = TokenSet.create(BLOCK_COMMENT, LINE_COMMENT)

    val SKIN_CONTAINERS = TokenSet.create(OBJECT, ARRAY, RESOURCES)
  }

  override fun createParser(project: Project?) = SkinParser()

  override fun createFile(viewProvider: FileViewProvider) = SkinFileImpl(viewProvider)

  override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY

  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

  override fun getFileNodeType(): IFileElementType = FILE

  override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

  override fun createLexer(project: Project?): Lexer  = SkinLexer()

  override fun createElement(node: ASTNode?): PsiElement = SkinElementTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet  = SKIN_COMMENTARIES
}