package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import icons.Icons
import javax.swing.Icon

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
class AtlasColorSettingsPage: ColorSettingsPage {

  override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> = mutableMapOf(
          "fileName" to AtlasSyntaxHighlighter.FILE_NAME,
          "textureName" to AtlasSyntaxHighlighter.TEXTURE_NAME,
          "key" to AtlasSyntaxHighlighter.KEY,
          "value" to AtlasSyntaxHighlighter.VALUE
  )

  override fun getIcon(): Icon = Icons.ATLAS_FILETYPE

  override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
          AttributesDescriptor("Colon", AtlasSyntaxHighlighter.COLON),
          AttributesDescriptor("Comma", AtlasSyntaxHighlighter.COMMA),

          AttributesDescriptor("Pack file name", AtlasSyntaxHighlighter.FILE_NAME),
          AttributesDescriptor("Texture name", AtlasSyntaxHighlighter.TEXTURE_NAME),
          AttributesDescriptor("Key", AtlasSyntaxHighlighter.KEY),
          AttributesDescriptor("Value", AtlasSyntaxHighlighter.VALUE)
  )

  override fun getColorDescriptors(): Array<out ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName(): String = "libGDX Atlas"

  override fun getHighlighter(): AtlasSyntaxHighlighter = AtlasSyntaxHighlighter()

  override fun getDemoText(): String = """<fileName>uiskin1.png</fileName>
<key>size</key>: <value>256</value>,<value>128</value>
<key>format</key>: <value>Alpha</value>
<key>filter</key>: <value>Linear</value>,<value>Linear</value>
<key>repeat</key>: <value>none</value>
<textureName>check-off</textureName>
  <key>rotate</key>: <value>false</value>
  <key>xy</key>: <value>11</value>, <value>5</value>
  <key>size</key>: <value>14</value>, <value>14</value>
  <key>orig</key>: <value>14</value>, <value>14</value>
  <key>offset</key>: <value>0</value>, <value>0f</value>
  <key>index</key>: <value>-1</value>
<textureName>textfield</textureName>
  <key>rotate</key>: <value>false</value>
  <key>xy</key>: <value>11</value>, <value>5</value>
  <key>size</key>: <value>14</value>, <value>14</value>
  <key>split</key>: <value>3</value>, <value>3</value>, <value>3</value>, <value>3</value>
  <key>orig</key>: <value>14</value>, <value>14</value>
  <key>offset</key>: <value>0</value>, <value>0</value>
  <key>index</key>: <value>-1</value>

<fileName>uiskin2.png</fileName>
<key>size</key>: <value>256</value>,<value>128</value>
<key>format</key>: <value>Alpha</value>
<key>filter</key>: <value>Linear</value>,<value>Linear</value>
<key>repeat</key>: <value>none</value>
<textureName>cursor</textureName>
  <key>rotate</key>: <value>false</value>
  <key>xy</key>: <value>23</value>, <value>1</value>
  <key>size</key>: <value>3</value>, <value>3</value>
  <key>split</key>: <value>1</value>, <value>1</value>, <value>1</value>, <value>1</value>
  <key>orig</key>: <value>3</value>, <value>3</value>
  <key>offset</key>: <value>0</value>, <value>0</value>
  <key>index</key>: <value>-1</value>
<textureName>default</textureName>
  <key>rotate</key>: <value>false</value>
  <key>xy</key>: <value>1</value>, <value>50</value>
  <key>size</key>: <value>254</value>, <value>77</value>
  <key>orig</key>: <value>254</value>, <value>77</value>
  <key>offset</key>: <value>0</value>, <value>0</value>
  <key>index</key>: <value>-1</value>
"""

}