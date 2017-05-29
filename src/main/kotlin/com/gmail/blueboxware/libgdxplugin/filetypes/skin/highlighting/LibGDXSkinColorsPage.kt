package com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BLOCK_COMMENT
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BRACES
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_BRACKETS
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_COLON
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_COMMA
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_INVALID_ESCAPE
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_KEYWORD
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_LINE_COMMENT
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_NUMBER
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_PROPERTY_NAME
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_RESOURCE_NAME
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory.Companion.SKIN_STRING
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

    private val additionalHighlighting = mapOf(
            "resourceName" to SKIN_RESOURCE_NAME,
            "propertyName" to SKIN_PROPERTY_NAME,
            "className" to SKIN_CLASS_NAME,
            "number" to SKIN_NUMBER,
            "keyword" to SKIN_KEYWORD
    )

    private val myAttributeDescriptors = arrayOf(
            AttributesDescriptor("Property name", SKIN_PROPERTY_NAME),
            AttributesDescriptor("Braces", SKIN_BRACES),
            AttributesDescriptor("Brackets", SKIN_BRACKETS),

            AttributesDescriptor("Comma", SKIN_COMMA),
            AttributesDescriptor("Colon", SKIN_COLON),

            AttributesDescriptor("Number", SKIN_NUMBER),

            AttributesDescriptor("Keyword", SKIN_KEYWORD),

            AttributesDescriptor("Line comment", SKIN_LINE_COMMENT),
            AttributesDescriptor("Block comment", SKIN_BLOCK_COMMENT),

            AttributesDescriptor("Valid escape sequence", SKIN_VALID_ESCAPE),
            AttributesDescriptor("Invalid escape sequence", SKIN_INVALID_ESCAPE),

            AttributesDescriptor("String", SKIN_STRING),

            AttributesDescriptor("Class name", SKIN_CLASS_NAME),
            AttributesDescriptor("Resource name", SKIN_RESOURCE_NAME)
    )

  }

  override fun getIcon(): Icon? = Icons.SKIN_FILETYPE

  override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(LibGDXSkinLanguage.INSTANCE, null, null)

  override fun getDemoText() = """
{
    // Line comment
    /* Block comment */
    <className>com.badlogic.gdx.graphics.Color</className>: {
        <resourceName>red</resourceName>: { <propertyName>r</propertyName>: <number>1</number>, <propertyName>g</propertyName>: <number>0</number>, <propertyName>b</propertyName>: <number>0</number>, <propertyName>a</propertyName>: <number>1</number> },
        <resourceName>yellow</resourceName>: { <propertyName>r</propertyName>: <number>0.5</number>, <propertyName>g</propertyName>: <number>0.5</number>, <propertyName>b</propertyName>: <number>0</number>, <propertyName>a</propertyName>: <number>1</number> }
    },
    <className>com.badlogic.gdx.graphics.g2d.BitmapFont</className>: {
        <resourceName>medium</resourceName>: { <propertyName>file</propertyName>: medium.fnt, <propertyName>keyword</propertyName>: <keyword>true</keyword> }
    },
    <className>com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle</className>: {
        <resourceName>default</resourceName>: {
            <propertyName>down</propertyName>: "round-down", <propertyName>up</propertyName>: round,
            <propertyName>font</propertyName>: 'medium', <propertyName>fontColor</propertyName>: white
        },
        <resourceName>toggle</resourceName>: {
            <propertyName>down</propertyName>: round-down, <propertyName>up</propertyName>: round, <propertyName>checked</propertyName>: round-down,
            <propertyName>font</propertyName>: medium, <propertyName>fontColor</propertyName>: white, <propertyName>checkedFontColor</propertyName>: red
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