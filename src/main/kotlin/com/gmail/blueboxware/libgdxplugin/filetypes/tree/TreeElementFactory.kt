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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeIndent
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory

object TreeElementFactory {

    private fun createFile(project: Project, content: String): PsiFile = PsiFileFactory.getInstance(project)
        .createFileFromText("dummy." + TreeFileType.defaultExtension, TreeFileType, content)

    fun createIndent(project: Project, level: Int): PsiTreeIndent? {
        val indentSettings = CodeStyle.getSettings(project).getLanguageIndentOptions(TreeLanguage)
        val indentSize = indentSettings?.INDENT_SIZE ?: 2
        val indent = if (indentSettings?.USE_TAB_CHARACTER == true) {
            "\t".repeat(level)
        } else {
            " ".repeat(level).repeat(indentSize)
        }
        val file = createFile(project, indent + "root")
        return file.childOfType<PsiTreeIndent>()
    }

}
