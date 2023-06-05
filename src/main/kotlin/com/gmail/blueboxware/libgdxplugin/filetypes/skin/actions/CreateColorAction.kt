package com.gmail.blueboxware.libgdxplugin.filetypes.skin.actions

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.intellij.codeInsight.actions.SimpleCodeInsightAction
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.intellij.ui.ColorChooserService
import com.intellij.ui.JBColor


/*
 * Copyright 2018 Blue Box Ware
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
class CreateColorAction : SimpleCodeInsightAction() {

    override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean = file is SkinFile

    override fun startInWriteAction(): Boolean = false

    override fun update(e: AnActionEvent) {
        super.update(e)

        templatePresentation.icon = AllIcons.Gutter.Colors
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {

        if (file !is SkinFileImpl) return

        Messages.showInputDialogWithCheckBox(
            "Name for the new color",
            "Name",
            "Use float components",
            false,
            true,
            AllIcons.Gutter.Colors,
            null,
            null
        ).let { result ->
            if (result.first.isNullOrBlank()) {
                return
            }

            ColorChooserService.instance.showDialog(editor.component, "Choose Color To Create", JBColor.WHITE, true)
                ?.let { color ->
                    ApplicationManager.getApplication().runWriteAction {
                        file.addColor(result.first, color = color, useComponents = result.second ?: false)
                    }
                }
        }

    }

}
