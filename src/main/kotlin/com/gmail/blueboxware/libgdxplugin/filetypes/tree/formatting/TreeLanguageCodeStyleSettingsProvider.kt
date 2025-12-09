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
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.application.options.IndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.openapi.application.ApplicationBundle
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

internal class TreeLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun getLanguage(): Language = TreeLanguage

    override fun getIndentOptionsEditor(): IndentOptionsEditor = IndentOptionsEditor()

    override fun getRightMargin(settingsType: SettingsType): Int = -1

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {

            SettingsType.LANGUAGE_SPECIFIC -> {
                consumer.showCustomOption(
                    TreeCodeStyleSettings::class.java,
                    "KEEP_INDENT",
                    message("tree.formatting.keep.indentation"), TreeCustomOptionsEditor.GROUP
                )
                consumer.showCustomOption(
                    TreeCodeStyleSettings::class.java,
                    "KEEP_COMMENTS",
                    message("tree.formatting.keep.comments"), TreeCustomOptionsEditor.GROUP
                )
                consumer.showCustomOption(
                    TreeCodeStyleSettings::class.java,
                    "KEEP_INDENTS_ON_EMPTY_LINES",
                    ApplicationBundle.message("checkbox.indent.keep.indents.on.empty.lines"),
                    TreeCustomOptionsEditor.GROUP
                )
            }

            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_WITHIN_PARENTHESES", "SPACE_BEFORE_COLON", "SPACE_AFTER_COLON", "SPACE_AFTER_TYPE_CAST"
                )
                consumer.renameStandardOption("SPACE_WITHIN_PARENTHESES", "Parentheses")
                consumer.renameStandardOption("SPACE_AFTER_TYPE_CAST", "After guard")
            }

            SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
            else -> {}
        }
    }

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings, indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        indentOptions.INDENT_SIZE = 2
        indentOptions.TAB_SIZE = 2
        indentOptions.USE_TAB_CHARACTER = false

        commonSettings.SPACE_BEFORE_COLON = false
        commonSettings.SPACE_AFTER_COLON = false
        commonSettings.SPACE_WITHIN_PARENTHESES = false
        commonSettings.SPACE_AFTER_TYPE_CAST = true
    }


    override fun getCodeSample(settingsType: SettingsType): String = """
        import rest:"com.badlogic.gdx.ai.tests.btree.dog.RestTask"
        ${if (settingsType == SettingsType.BLANK_LINES_SETTINGS) "\n\n\n\n" else ""}                
        # Comment
        root
          selector
            parallel
              care urgentProb:0.8 bool:true
              com.badlogic.gdx.ai.tests.btree.dog.PlayTask # fully qualified task
            randomSelector
         # Comment
              untilSuccess
                sequence
                  bark times:"uniform,1,2"
                  (include subtree:"path/to/my/subtree") walk
                  (guard a:1 b:2) mark
              parallel policy:"selector"  # sleep with random timeout
                wait seconds:"triangular,2.5,5.5"
                rest
    """.trimIndent()

}
