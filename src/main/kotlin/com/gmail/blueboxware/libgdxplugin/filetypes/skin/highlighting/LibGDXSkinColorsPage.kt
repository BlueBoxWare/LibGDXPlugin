package com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BLOCK_COMMENT
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BRACES
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BRACKETS
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_COLON
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_COMMA
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_INVALID_ESCAPE
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_KEYWORD
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_LINE_COMMENT
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_NUMBER
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_PROPERTY_KEY
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_VALID_ESCAPE
import com.intellij.application.options.colors.InspectionColorSettingsPage
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.psi.codeStyle.DisplayPriority
import com.intellij.psi.codeStyle.DisplayPrioritySortable
import icons.Icons
import javax.swing.Icon

/*
 * Copyright 2016 Blue Box Ware
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
class LibGDXSkinColorsPage : ColorSettingsPage, InspectionColorSettingsPage, DisplayPrioritySortable {

  companion object {

    private val additionalHighlighting = mapOf("propertyKey" to SKIN_PROPERTY_KEY)

    private val myAttributeDescriptors = arrayOf(
            AttributesDescriptor("Property key", SKIN_PROPERTY_KEY),
            AttributesDescriptor("Braces", SKIN_BRACES),
            AttributesDescriptor("Brackets", SKIN_BRACKETS),

            AttributesDescriptor("Comma", SKIN_COMMA),
            AttributesDescriptor("Colon", SKIN_COLON),

            AttributesDescriptor("Number", SKIN_NUMBER),

            AttributesDescriptor("Keyword", SKIN_KEYWORD),

            AttributesDescriptor("Line comment", SKIN_LINE_COMMENT),
            AttributesDescriptor("Block comment", SKIN_BLOCK_COMMENT),

            AttributesDescriptor("Valid escape sequence", SKIN_VALID_ESCAPE),
            AttributesDescriptor("Invalid escape sequence", SKIN_INVALID_ESCAPE)
    )

  }

  override fun getIcon(): Icon? = Icons.LIBGDX_ICON

  override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(LibGDXSkinLanguage.INSTANCE, null, null)

  override fun getDemoText() = """
{
    // Line comment
    /* Block comment */
    com.badlogic.gdx.graphics.Color: {
        red: { r: 1, g: 0, b: 0, a: 1 },
        yellow: { r: 0.5, g: 0.5, b: 0, a: 1 }
    },
    com.badlogic.gdx.graphics.g2d.BitmapFont: {
        medium: { file: medium.fnt, keyword: true }
    },
    com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
        default: {
            down: "round-down", up: round,
            font: 'medium', fontColor: white
        },
        toggle: {
            down: round-down, up: round, checked: round-down,
            font: medium, fontColor: white, checkedFontColor: red
        },
    }
}
  """

  override fun getAdditionalHighlightingTagToDescriptorMap() = additionalHighlighting

  override fun getAttributeDescriptors(): Array<out AttributesDescriptor> = myAttributeDescriptors

  override fun getColorDescriptors(): Array<out ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName() = "LibGDX Skin"

  override fun getPriority(): DisplayPriority = DisplayPriority.LANGUAGE_SETTINGS

}