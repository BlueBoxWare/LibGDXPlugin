package com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonCodeStyleSettingsProvider.java
 *
 */
class SkinCodeStyleSettingsProvider: CodeStyleSettingsProvider() {

  @Suppress("DialogTitleCapitalization")
  override fun createConfigurable(
          settings: CodeStyleSettings,
          originalSettings: CodeStyleSettings
  ): CodeStyleConfigurable {

    return object: CodeStyleAbstractConfigurable(settings, originalSettings, "libGDX skin") {

      override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {

        return object: TabbedLanguageCodeStylePanel(LibGDXSkinLanguage.INSTANCE, currentSettings, settings) {

          override fun initTabs(settings: CodeStyleSettings) {
            addIndentOptionsTab(settings)
            addSpacesTab(settings)
            addBlankLinesTab(settings)
            addWrappingAndBracesTab(settings)
          }
        }

      }

      override fun getHelpTopic(): String? = null
    }

  }

  override fun createCustomSettings(settings: CodeStyleSettings) = SkinCodeStyleSettings(settings)

  override fun getConfigurableDisplayName() = LibGDXSkinLanguage.INSTANCE.displayName
}
