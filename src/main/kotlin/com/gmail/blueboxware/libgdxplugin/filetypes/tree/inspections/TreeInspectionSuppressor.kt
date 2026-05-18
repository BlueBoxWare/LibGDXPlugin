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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeFile
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.utils.SuppressionTag
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.utils.TreeSuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.utils.TreeSuppressForLineFix
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.utils.TreeSuppressForTreeFix
import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.codeInspection.SuppressionUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.findParentOfType
import com.intellij.util.text.CharArrayUtil
import org.jetbrains.kotlin.psi.psiUtil.prevSiblingOfSameType

internal class TreeInspectionSuppressor : InspectionSuppressor {
    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean =
        isSuppressedForFile(element, toolId) ||
                element.findParentOfType<PsiTreeLine>()?.let { line ->
                    line.isSuppressedForLine(toolId, false) || line.isSuppressedForTree(toolId)
                } ?: false

    override fun getSuppressActions(
        element: PsiElement?, toolId: String
    ): Array<out SuppressQuickFix?> = arrayOf(
        TreeSuppressForLineFix(toolId), TreeSuppressForTreeFix(toolId),
        TreeSuppressForFileFix(toolId)
    )

    private fun PsiTreeLine.isSuppressedForLine(id: String, treeOnly: Boolean): Boolean {
        var line = findParentOfType<PsiTreeLine>(false)

        while (line != null) {
            line.getComment()?.let { comment ->
                val tag = comment.isSuppressionComment()
                if (((tag == SuppressionTag.LINE && !treeOnly) || tag == SuppressionTag.TREE) && SuppressionUtil.isInspectionToolIdMentioned(
                        line.text, id
                    )
                ) {
                    return true
                }
            }
            line = line.prevSiblingOfSameType()
            if (line?.isEmpty() != true) {
                break
            }
        }
        return false
    }

    private fun PsiTreeLine.isSuppressedForTree(id: String): Boolean {
        var tree = (containingFile as? TreeFile)?.getTree()?.findLine(this) ?: return false

        while (tree.parent != null) {
            tree = tree.parent!!
            if (tree.line?.isSuppressedForLine(id, true) == true) return true
        }

        return false
    }

    private fun isSuppressedForFile(element: PsiElement, id: String): Boolean {
        val file = (element.containingFile as? TreeFile) ?: return false
        for (line in file.childrenOfType<PsiTreeLine>()) {
            if (!line.isEmpty()) {
                break
            }
            line.getComment()?.let { comment ->
                val tag = comment.isSuppressionComment()
                if (tag == SuppressionTag.FILE && SuppressionUtil.isInspectionToolIdMentioned(line.text, id)) {
                    return true
                }
            }
        }

        return false
    }

    private fun PsiElement.isSuppressionComment(): SuppressionTag? {
        val text = text
        val index = CharArrayUtil.shiftForward(text, 1, "# ")
        if (index >= textLength) return null
        return SuppressionTag.getTagFromSuppression(text, index)
    }
}
