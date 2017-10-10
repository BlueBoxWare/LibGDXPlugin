package com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.ListCellRendererWrapper
import java.awt.event.ItemEvent
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.JPanel

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonCodeStylePanel.java
 *
 */
class SkinCodeStylePanel(settings: CodeStyleSettings) : CodeStyleAbstractPanel(LibGDXSkinLanguage.INSTANCE, null, settings) {

  lateinit private var myPropertiesAlignmentCombo: JComboBox<SkinCodeStyleSettings.PropertyAlignment>
  lateinit private var myDontWrapColors: JCheckBox
  lateinit private var myPreviewPanel: JPanel
  lateinit private var myPanel: JPanel

  init {
    addPanelToWatch(myPanel)
    installPreviewPanel(myPreviewPanel)

    for (alignment in SkinCodeStyleSettings.PropertyAlignment.values()) {
      myPropertiesAlignmentCombo.addItem(alignment)
    }

    myPropertiesAlignmentCombo.renderer = object : ListCellRendererWrapper<SkinCodeStyleSettings.PropertyAlignment>() {
      override fun customize(list: JList<*>?, value: SkinCodeStyleSettings.PropertyAlignment?, index: Int, selected: Boolean, hasFocus: Boolean) {
        setText(value?.description ?: "")
      }
    }

    myPropertiesAlignmentCombo.addItemListener { e ->
      if (e.stateChange == ItemEvent.SELECTED) {
        somethingChanged()
      }
    }

    myDontWrapColors.addItemListener { e ->
      if (e.stateChange == ItemEvent.SELECTED || e.stateChange == ItemEvent.DESELECTED) {
        somethingChanged()
      }
    }
  }

  override fun resetImpl(settings: CodeStyleSettings) {
    for (i in 0 until myPropertiesAlignmentCombo.itemCount) {
      if (myPropertiesAlignmentCombo.getItemAt(i).id == getCustomSettings(settings).PROPERTY_ALIGNMENT) {
        myPropertiesAlignmentCombo.selectedIndex = i
      }
    }

    myDontWrapColors.isSelected = getCustomSettings(settings).DO_NOT_WRAP_COLORS
  }

  override fun isModified(settings: CodeStyleSettings) =
          getCustomSettings(settings).PROPERTY_ALIGNMENT != getSelectedAlignmentType().id ||
          getCustomSettings(settings).DO_NOT_WRAP_COLORS != myDontWrapColors.isSelected

  override fun getPanel() = myPanel

  override fun getRightMargin() = 80

  override fun apply(settings: CodeStyleSettings) {
    getCustomSettings(settings).PROPERTY_ALIGNMENT = getSelectedAlignmentType().id
    getCustomSettings(settings).DO_NOT_WRAP_COLORS = myDontWrapColors.isSelected
  }

  override fun getFileType() = LibGDXSkinFileType.INSTANCE

  override fun createHighlighter(scheme: EditorColorsScheme) = EditorHighlighterFactory.getInstance().createEditorHighlighter(LightVirtualFile("a.skin"), scheme, null)

  override fun getPreviewText() = """
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

  private fun getSelectedAlignmentType(): SkinCodeStyleSettings.PropertyAlignment = myPropertiesAlignmentCombo.selectedItem as SkinCodeStyleSettings.PropertyAlignment

  private fun getCustomSettings(settings: CodeStyleSettings): SkinCodeStyleSettings = settings.getCustomSettings(SkinCodeStyleSettings::class.java)
}