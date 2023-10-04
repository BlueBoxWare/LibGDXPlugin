package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet


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
internal class Atlas2ParserDefinition : ParserDefinition {

    object Util {
        val FILE = IFileElementType(LibGDXAtlas2Language.INSTANCE)
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
    }

    override fun createParser(project: Project?) = Atlas2Parser()

    override fun createFile(viewProvider: FileViewProvider) = Atlas2File(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) =
        ParserDefinition.SpaceRequirements.MAY

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun getFileNodeType() = Util.FILE

    override fun getWhitespaceTokens() = Util.WHITE_SPACES

    override fun createLexer(project: Project?) = Atlas2Lexer()

    override fun createElement(node: ASTNode?): PsiElement = Atlas2ElementTypes.Factory.createElement(node)

    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY
}

