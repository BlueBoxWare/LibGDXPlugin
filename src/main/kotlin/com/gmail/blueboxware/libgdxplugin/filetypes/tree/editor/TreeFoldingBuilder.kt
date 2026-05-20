/*
 * Copyright 2026 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeFile
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.nextLine
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

internal class TreeFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(
        root: PsiElement, document: Document, quick: Boolean
    ): Array<out FoldingDescriptor?> {

        if (root !is TreeFile && root !is PsiTreeLine) return emptyArray()

        val descriptors = mutableListOf<FoldingDescriptor>()

        val file = root.containingFile as? TreeFile ?: return emptyArray()
        val tree = file.getTree(root)

        tree?.accept { line ->
            file.getTree(line)?.let { tree ->
                if (tree.children.isEmpty() || tree.line == null) return@accept
                val start = tree.children.firstOrNull()?.textRange()?.startOffset ?: return@accept
                var end = tree.children.lastOrNull()?.textRange()?.endOffset ?: return@accept
                if (end != file.textRange.endOffset)
                    end -= 1
                line.node?.let { node ->
                    val realChildren = tree.realChildren()
                    val children = if (realChildren < 2) "child" else "children"
                    descriptors.add(
                        FoldingDescriptor(
                            node, TextRange(start - 1, end), null, " [${realChildren} $children] "
                        )
                    )
                }
            }
        }

        if (root is TreeFile) {
            collectImportCollabsibles(root, descriptors)
        }

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String = " [...] "


    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    private fun collectImportCollabsibles(file: TreeFile, descriptors: MutableList<FoldingDescriptor>) {
        var line = file.childOfType<PsiTreeLine>()

        while (line != null) {
            if (line.statement?.isImport() == true) {
                val startLine = line
                var endLine = line
                while (true) {
                    val nextLine = endLine?.nextLine(includeComments = false, includeEmpty = false)
                    if (nextLine?.statement?.isImport() == true) {
                        endLine = nextLine
                    } else {
                        break
                    }
                }
                if (startLine != endLine) {
                    startLine.node?.let { node ->
                        var end = endLine.textRange.endOffset
                        if (end != file.textRange.endOffset)
                            end -= 1
                        descriptors.add(
                            FoldingDescriptor(
                                node,
                                TextRange(startLine.textRange.startOffset + 7, end),
                                null,
                                "..."
                            )
                        )
                    }
                }
                line = endLine
            }

            line = line.nextLine(includeComments = false, includeEmpty = false)
        }

    }

}
