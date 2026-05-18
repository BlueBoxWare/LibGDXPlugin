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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting.TreeCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.application.options.CodeStyle
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.findParentOfType
import com.intellij.util.IncorrectOperationException

enum class SuppressionTag(val tag: String) {
    LINE("line"), TREE("tree"), FILE("file");

    companion object {
        fun getTagFromSuppression(text: String, startIndex: Int = 0): SuppressionTag? {
            for (tag in entries) {
                if (text.startsWith("${tag.tag}:noinspection", startIndex)) {
                    return tag
                }
            }
            if (text.startsWith("noinspection", startIndex)) {
                return LINE
            }
            return null
        }
    }
}

abstract class TreeSuppressQuickFix(val id: String, val forFile: Boolean = false) : SuppressQuickFix {
    override fun isAvailable(
        project: Project, element: PsiElement
    ): Boolean = element.isValid

    abstract fun getTag(): SuppressionTag

    override fun isSuppressAll(): Boolean = false

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        descriptor.psiElement.findParentOfType<PsiTreeLine>()?.let { line ->
            val file = line.containingFile
            val codeStyleSettings = CodeStyle.getSettings(file).getCustomSettings(TreeCodeStyleSettings::class.java)
            val addSpace = codeStyleSettings.LINE_COMMENT_ADD_SPACE_IN_SUPPRESSION
            val commentIndent = if (addSpace) " " else ""
            val lineIndent = if (forFile || codeStyleSettings.LINE_COMMENT_AT_FIRST_COLUMN) 0 else line.getIndentSize()
            val tag = getTag().tag
            val text = "#${commentIndent}${tag}:noinspection $id"
            val comment = TreeElementFactory.createLine(project, lineIndent, text) ?: return
            try {
                if (forFile) {
                    file.childOfType<PsiTreeLine>()?.let {
                        file.addBefore(comment, it)
                    }
                } else {
                    line.parent.addBefore(comment, line)
                }
            } catch (e: IncorrectOperationException) {
                Logger.getInstance(this::class.java).warn(e)
            }
        }
    }

}

class TreeSuppressForFileFix(id: String) : TreeSuppressQuickFix(id, forFile = true) {

    override fun getPriority(): Int = 80

    override fun getTag(): SuppressionTag = SuppressionTag.FILE

    override fun getFamilyName(): @IntentionFamilyName String = message("suppress.file")

}

class TreeSuppressForLineFix(id: String) : TreeSuppressQuickFix(id) {

    override fun getPriority(): Int = 100

    override fun getTag(): SuppressionTag = SuppressionTag.LINE

    override fun getFamilyName(): @IntentionFamilyName String = message("suppress.line")

}

class TreeSuppressForTreeFix(id: String) : TreeSuppressQuickFix(id) {

    override fun getPriority(): Int = 90

    override fun getTag(): SuppressionTag = SuppressionTag.TREE

    override fun getFamilyName(): @IntentionFamilyName String = message("suppress.tree")

}
