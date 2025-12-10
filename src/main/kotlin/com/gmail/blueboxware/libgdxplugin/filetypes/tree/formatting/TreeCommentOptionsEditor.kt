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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.codeStyle.CommenterForm
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import javax.swing.JComponent

class TreeCommentOptionsEditor(settings: CodeStyleSettings) : CodeStyleAbstractPanel(settings) {

    private val commenterForm = CommenterForm(TreeLanguage)

    override fun getRightMargin(): Int = -1

    override fun createHighlighter(scheme: EditorColorsScheme): EditorHighlighter? =
        FileTypeEditorHighlighterProviders.getInstance().forFileType(TreeFileType).getEditorHighlighter(
            null,
            TreeFileType, null, scheme
        )

    override fun getFileType(): FileType = TreeFileType

    override fun getPreviewText(): String? =
        LanguageCodeStyleSettingsProvider.forLanguage(TreeLanguage)
            ?.getCodeSample(LanguageCodeStyleSettingsProvider.SettingsType.COMMENTER_SETTINGS)

    override fun apply(settings: CodeStyleSettings) = commenterForm.apply(settings)

    override fun isModified(settings: CodeStyleSettings): Boolean = commenterForm.isModified(settings)

    override fun getPanel(): JComponent? = commenterForm.commenterPanel

    override fun resetImpl(settings: CodeStyleSettings) = commenterForm.reset(settings)

    override fun getTabTitle(): @NlsContexts.TabTitle String = message("tree.formatting.comments.title")

}
