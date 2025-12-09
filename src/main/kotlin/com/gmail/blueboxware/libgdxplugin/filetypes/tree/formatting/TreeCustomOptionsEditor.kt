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
import com.intellij.application.options.codeStyle.OptionTreeWithPreviewPanel
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class TreeCustomOptionsEditor(settings: CodeStyleSettings) : OptionTreeWithPreviewPanel(settings) {

    init {
        init()
    }

    override fun initTables() {
        initCustomOptions(GROUP)
    }

    override fun getPreviewText(): String? =
        LanguageCodeStyleSettingsProvider.forLanguage(TreeLanguage)?.getCodeSample(settingsType)

    override fun getFileType(): FileType = TreeFileType

    override fun getTabTitle(): @NlsContexts.TabTitle String = message("tree.formatting.title")

    override fun getSettingsType(): LanguageCodeStyleSettingsProvider.SettingsType =
        LanguageCodeStyleSettingsProvider.SettingsType.LANGUAGE_SPECIFIC

    override fun customizeSettings() {
        LanguageCodeStyleSettingsProvider.forLanguage(TreeLanguage)?.customizeSettings(this, settingsType)
    }

    companion object {
        val GROUP = message("tree.formatting.group")
    }

}
