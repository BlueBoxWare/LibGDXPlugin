package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import icons.Icons

/*
 * Copyright 2017 Blue Box Ware
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
class BitmapFontColorSettingsPage: ColorSettingsPage {

  companion object {
    val EQUALS_SIGN = createTextAttributesKey("LIBGDX.FONT.EQUALS", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val COMMA = createTextAttributesKey("LIBGDX.FONT.COMMA", DefaultLanguageHighlighterColors.COMMA)
    val KEYWORD = createTextAttributesKey("LIBGDX.FONT.KEYWORD", DefaultLanguageHighlighterColors.CONSTANT)
    val KEY = createTextAttributesKey("LIBGDX.FONT.KEY", DefaultLanguageHighlighterColors.KEYWORD)
    val VALUE = createTextAttributesKey("LIBGDX.FONT.VALUE", DefaultLanguageHighlighterColors.STRING)
  }

  override fun getAdditionalHighlightingTagToDescriptorMap() = mutableMapOf(
          "equals" to EQUALS_SIGN,
          "comma" to COMMA,
          "keyword" to KEYWORD,
          "key" to KEY,
          "value" to VALUE
  )

  override fun getIcon() = Icons.FONT_FILETYPE

  override fun getAttributeDescriptors() = arrayOf(
          AttributesDescriptor("Equals Sign", EQUALS_SIGN),
          AttributesDescriptor("Comma", COMMA),
          AttributesDescriptor("Keyword", KEYWORD),
          AttributesDescriptor("Key", KEY),
          AttributesDescriptor("Value", VALUE)
  )

  override fun getColorDescriptors(): Array<out ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName() = "libGDX Bitmap Font"

  override fun getHighlighter() = PlainSyntaxHighlighter()

  override fun getDemoText() =
          """<keyword>info</keyword> <key>face</key><equals>=</equals><value>"Arial"</value> <key>size</key><equals>=</equals><value>32</value> <key>bold</key><equals>=</equals><value>0</value> <key>italic</key><equals>=</equals><value>0</value> <key>charset</key><equals>=</equals><value>""</value> <key>unicode</key><equals>=</equals><value>0</value> <key>stretchH</key><equals>=</equals><value>100</value> <key>smooth</key><equals>=</equals><value>1</value> <key>aa</key><equals>=</equals><value>1</value> <key>padding</key><equals>=</equals><value>0<comma>,</comma>0<comma>,</comma>0<comma>,</comma>0</value> <key>spacing</key><equals>=</equals><value>0<comma>,</comma>0</value>
<keyword>common</keyword> <key>lineHeight</key><equals>=</equals><value>38</value> <key>base</key><equals>=</equals><value>30</value> <key>scaleW</key><equals>=</equals><value>512</value> <key>scaleH</key><equals>=</equals><value>128</value> <key>pages</key><equals>=</equals><value>1</value> <key>packed</key><equals>=</equals><value>0</value>
<keyword>page</keyword> <key>id</key><equals>=</equals><value>0</value> <key>file</key><equals>=</equals><value>"arial-32.png"</value>
<keyword>chars</keyword> <key>count</key><equals>=</equals><value>95</value>
<keyword>char</keyword> <key>id</key><equals>=</equals><value>32</value>   <key>x</key><equals>=</equals><value>0</value>     <key>y</key><equals>=</equals><value>0</value>     <key>width</key><equals>=</equals><value>10</value>     <key>height</key><equals>=</equals><value>32</value>     <key>xoffset</key><equals>=</equals><value>0</value>     <key>yoffset</key><equals>=</equals><value>30</value>    <key>xadvance</key><equals>=</equals><value>9</value>     <key>page</key><equals>=</equals><value>0</value>  <key>chnl</key><equals>=</equals><value>0</value>
<keyword>char</keyword> <key>id</key><equals>=</equals><value>64</value>   <key>x</key><equals>=</equals><value>0</value>     <key>y</key><equals>=</equals><value>0</value>     <key>width</key><equals>=</equals><value>31</value>     <key>height</key><equals>=</equals><value>32</value>     <key>xoffset</key><equals>=</equals><value>2</value>     <key>yoffset</key><equals>=</equals><value>6</value>    <key>xadvance</key><equals>=</equals><value>32</value>     <key>page</key><equals>=</equals><value>0</value>  <key>chnl</key><equals>=</equals><value>0</value>
<keyword>kernings</keyword> <key>count</key><equals>=</equals><value>660</value>
<keyword>kerning</keyword> <key>first</key><equals>=</equals><value>49</value>  <key>second</key><equals>=</equals><value>49</value>  <key>amount</key><equals>=</equals><value>-2</value>
<keyword>kerning</keyword> <key>first</key><equals>=</equals><value>121</value>  <key>second</key><equals>=</equals><value>44</value>  <key>amount</key><equals>=</equals><value>-2</value>
  """
}