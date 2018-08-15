package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

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
class BitmapFontParserDefinition : ParserDefinition {

  companion object {

    val FILE = IFileElementType(BitmapFontLanguage.INSTANCE)
    val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)

  }

  override fun createParser(project: Project?) = BitmapFontParser()

  override fun createFile(viewProvider: FileViewProvider) = BitmapFontFile(viewProvider)

  @Suppress("OverridingDeprecatedMember")
  // COMPAT: Deprecated in 182
  override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) = ParserDefinition.SpaceRequirements.MAY

  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

  override fun getFileNodeType() = FILE

  override fun getWhitespaceTokens() = WHITE_SPACES

  override fun createLexer(project: Project?) = BitmapFontLexer()

  override fun createElement(node: ASTNode?): PsiElement = BitmapFontElementTypes.Factory.createElement(node)

  override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

}