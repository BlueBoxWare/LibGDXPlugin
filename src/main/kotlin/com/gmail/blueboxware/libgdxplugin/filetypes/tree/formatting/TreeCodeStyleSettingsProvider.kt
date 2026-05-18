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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

internal class TreeCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun createConfigurable(
        settings: CodeStyleSettings,
        originalSettings: CodeStyleSettings
    ): CodeStyleConfigurable {

        return object : CodeStyleAbstractConfigurable(settings, originalSettings, TreeLanguage.displayName) {

            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {

                return object : TabbedLanguageCodeStylePanel(TreeLanguage, currentSettings, settings) {

                    override fun initTabs(settings: CodeStyleSettings) {
                        addIndentOptionsTab(settings)
                        addSpacesTab(settings)
                        addBlankLinesTab(settings)
                        addTab(TreeCustomOptionsEditor(settings))
                    }
                }

            }

            override fun getHelpTopic(): String? = null
        }

    }

    override fun getLanguage(): Language = TreeLanguage

    override fun createCustomSettings(settings: CodeStyleSettings) = TreeCodeStyleSettings(settings)

    @Suppress("DialogTitleCapitalization")
    override fun getConfigurableDisplayName() = "libGDX AI Tree"
}
