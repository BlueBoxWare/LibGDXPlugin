/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

class TreeBlock(
    private val node: ASTNode,
    private val spacingBuilder: SpacingBuilder
) :
    AbstractBlock(node, null, null) {

    private var blocks: MutableList<Block>? = null

    override fun buildChildren(): List<Block?> {
        if (blocks == null) {
            val children = node.getChildren(null)
            blocks = mutableListOf()
            for (child in children) {
                if (isWhiteSpaceOrEmpty(child)) {
                    continue
                }
                blocks?.add(makeSubBlock(child))
            }
        }

        return blocks ?: listOf()
    }

    override fun getIndent(): Indent? = Indent.getAbsoluteNoneIndent()

    override fun getSpacing(
        child1: Block?, child2: Block
    ): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }

    private fun makeSubBlock(child: ASTNode): Block {
        return TreeBlock(child, spacingBuilder)
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(
        Indent.getAbsoluteNoneIndent(),
        Alignment.createAlignment()
    )

    companion object {

        private fun isWhiteSpaceOrEmpty(node: ASTNode) =
            node.elementType == TokenType.WHITE_SPACE || node.textLength == 0

    }

}
