package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.annotators.ColorAnnotator
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.UIUtil
import java.awt.Color
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
internal fun stringToColor(string: String): Color? {

  if (string.length < 6) return null

  var str = if (string[0] == '"') string.substring(1, string.length - 1) else string

  if (!ColorAnnotator.colorRegex.matches(str)) return null

  if (str[0] == '#') {
    str = str.substring(1)
  }

  return try {
    val r = Integer.valueOf(str.substring(0, 2), 16)
    val g = Integer.valueOf(str.substring(2, 4), 16)
    val b = Integer.valueOf(str.substring(4, 6), 16)
    val a = if (str.length == 8) Integer.valueOf(str.substring(6, 8), 16) else 255

    Color(r / 255f, g / 255f, b / 255f, a / 255f)
  } catch (e: NumberFormatException) {
    null
  }
}

internal fun createColorIcon(color: Color): Icon = ColorIcon(if (UIUtil.isRetina()) 24 else 12, color, true)

open class GutterColorRenderer(val color: Color): GutterIconRenderer() {

  override fun getIcon(): Icon = createColorIcon(color)

  override fun equals(other: Any?) = other is GutterColorRenderer && color == other.color

  override fun hashCode() = color.hashCode()

}