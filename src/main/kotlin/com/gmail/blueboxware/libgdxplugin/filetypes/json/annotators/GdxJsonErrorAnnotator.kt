package com.gmail.blueboxware.libgdxplugin.filetypes.json.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.isLeaf
import com.gmail.blueboxware.libgdxplugin.utils.lastChild
import com.gmail.blueboxware.libgdxplugin.utils.prevLeafSkipWithSpace
import com.intellij.lang.ASTNode
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import org.jetbrains.kotlin.psi.psiUtil.children
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.getPrevSiblingIgnoringWhitespaceAndComments


/*
 * Copyright 2021 Blue Box Ware
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
class GdxJsonErrorAnnotator: Annotator {

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if (element is GdxJsonJobject) {
      if (!element.lastChild.isLeaf(GdxJsonElementTypes.R_CURLY)) {
        holder
                .newAnnotation(HighlightSeverity.ERROR, message("json.error.bracket.expected"))
                .range(TextRange.create(element.lastChild.endOffset - 1, element.textRange.endOffset))
                .create()
      }
    }

    if (element is GdxJsonJobject || element is GdxJsonArray) {

      var lastNode: ASTNode? = null
      for (currentNode in element.node.children()) {
        if (currentNode.elementType == GdxJsonElementTypes.COMMA && lastNode?.elementType == GdxJsonElementTypes.COMMA) {
          holder
                  .newAnnotation(HighlightSeverity.ERROR, message("json.error.unexpected.comma"))
                  .range(currentNode)
                  .create()
          return
        }
        if (currentNode.elementType !in SKIP) {
          lastNode = currentNode
        }
      }

      if (element.children.isEmpty()) {
        return
      }

      val last = element.lastChild.getPrevSiblingIgnoringWhitespaceAndComments(false)
              ?: return
      if (last.elementType == GdxJsonElementTypes.COMMA) {
        last.prevLeaf { !it.isLeaf(GdxJsonParserDefinition.COMMENTS) }?.let { secondLast ->
          if (secondLast is PsiWhiteSpace && secondLast.textContains('\n')) {
            element.lastChild { it.isLeaf(GdxJsonElementTypes.COMMA) }?.let { comma ->
              holder
                      .newAnnotation(HighlightSeverity.ERROR, message("json.error.trailing.comma"))
                      .range(comma)
                      .create()
            }
          }
        }
      }
    } else if (element is GdxJsonString && !element.isQuoted) {
      if (element.textContains('\n')) {
        val offset = element.textRange.startOffset + element.text.indexOf('\n')
        holder
                .newAnnotation(HighlightSeverity.ERROR, message("json.error.unexpected.newline"))
                .range(TextRange.create(offset, offset + 1))
                .afterEndOfLine()
                .create()
      }
    }

  }

  companion object {
    private val SKIP = TokenSet.create(
            GdxJsonElementTypes.LINE_COMMENT, GdxJsonElementTypes.BLOCK_COMMENT, TokenType.WHITE_SPACE
    )
  }

}