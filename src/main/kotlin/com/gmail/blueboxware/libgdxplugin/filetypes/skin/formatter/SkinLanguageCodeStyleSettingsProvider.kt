package com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonLanguageCodeStyleSettingsProvider.java
 *
 */
internal class SkinLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        val wrapOptions = CodeStyleSettingsCustomizableOptions.getInstance().WRAP_OPTIONS
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_WITHIN_BRACKETS",
                    "SPACE_WITHIN_BRACES",
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA"
                )
                consumer.renameStandardOption("SPACE_WITHIN_BRACES", "Braces")
                val spacesOther = CodeStyleSettingsCustomizableOptions.getInstance().SPACES_OTHER
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "SPACE_BEFORE_COLON",
                    "Before ':'",
                    spacesOther
                )
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "SPACE_AFTER_COLON",
                    "After ':'",
                    spacesOther
                )
            }

            SettingsType.BLANK_LINES_SETTINGS -> consumer.showStandardOptions("KEEP_BLANK_LINES_IN_CODE")
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    "RIGHT_MARGIN",
                    "WRAP_ON_TYPING",
                    "KEEP_LINE_BREAKS",
                    "WRAP_LONG_LINES"
                )
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "ARRAY_WRAPPING",
                    "Arrays",
                    null,
                    wrapOptions,
                    CodeStyleSettingsCustomizable.WRAP_VALUES
                )
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "OBJECT_WRAPPING",
                    "Objects",
                    null,
                    wrapOptions,
                    CodeStyleSettingsCustomizable.WRAP_VALUES
                )
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "PROPERTY_ALIGNMENT",
                    "Align",
                    "Objects",
                    SkinCodeStyleSettings.PropertyAlignment.entries.map { it.description }.toTypedArray(),
                    SkinCodeStyleSettings.PropertyAlignment.entries.map { it.id }.toIntArray()
                )
                consumer.showCustomOption(
                    SkinCodeStyleSettings::class.java,
                    "DO_NOT_WRAP_COLORS",
                    "Do not wrap colors",
                    "Objects"
                )
            }

            else -> {}
        }
    }

    override fun getCodeSample(settingsType: SettingsType) = """
{
    com.badlogic.gdx.graphics.Color: {
        white: { r: 1, g: 1, b: 1, a: 1 },
        red: { r: 1, g: 0, b: 0, a: 1 },
        yellow: { r: 0.5, g: 0.5, b: 0, a: 1 }
    },
    com.badlogic.gdx.graphics.g2d.BitmapFont: {
        medium: { file: medium.fnt }
    },
    com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
        default: {
            down: round-down, up: round,
            font: medium, fontColor: white
        },
        toggle: {
            down: round-down, up: round, checked: round-down,
            font: medium, fontColor: white, checkedFontColor: red
        },
        green: {
            down: round-down, up: round,
            font: medium, fontColor: { r: 0, g: 1, b: 0, a: 1 }
        }
    }
}
"""

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        indentOptions.INDENT_SIZE = 2
        commonSettings.KEEP_BLANK_LINES_IN_CODE = 0
        commonSettings.SPACE_WITHIN_BRACES = true
    }

    override fun getLanguage() = LibGDXSkinLanguage.INSTANCE

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()
}
