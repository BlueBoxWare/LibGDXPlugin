package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition.Companion.SKIN_COMMENTARIES
import com.intellij.json.psi.JsonPsiUtil
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
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
object SkinPsiUtil {

  fun stripQuotes(text: String) = JsonPsiUtil.stripQuotes(text)

  fun findFurthestSiblingOfSameType(anchor: PsiElement, after: Boolean): PsiElement {

    var node = anchor.node
    val expectedType = node.elementType
    var lastSeen = node

    while (node != null) {
      val elementType = node.elementType
      if (elementType == expectedType) {
        lastSeen = node
      } else if (elementType == TokenType.WHITE_SPACE) {
        if (expectedType == SkinElementTypes.LINE_COMMENT && node.text.indexOf('\n', 1) != -1) {
          break
        }
      } else if (!SKIN_COMMENTARIES.contains(elementType) || SKIN_COMMENTARIES.contains(expectedType)) {
        break
      }
      node = if (after) node.treeNext else node.treePrev
    }

    return lastSeen.psi
  }

  fun hasElementType(node: ASTNode, set: TokenSet) = set.contains(node.elementType)

  fun hasElementType(node: ASTNode, vararg types: IElementType) = hasElementType(node, TokenSet.create(*types))

}