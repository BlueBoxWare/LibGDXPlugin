package com.gmail.blueboxware.libgdxplugin.utils

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
private val COLOR_REGEX = Regex("#?(?:[0-9a-fA-F]{2}){3,4}")

internal fun color(r: Float, g: Float, b: Float, a: Float): Color? =
        if (r < 0 || r > 1 || g < 0 || g > 1 || b < 0 || b > 1 || a < 0 || a > 1)
          null
        else
          Color(r, g, b, a)

internal fun color(string: String): Color? {

  if (string.length < 6) return null

  var str = if (string[0] == '"') string.substring(1, string.length - 1) else string

  if (!COLOR_REGEX.matches(str)) return null

  if (str[0] == '#') {
    str = str.substring(1)
  }

  return try {
    val r = Integer.valueOf(str.substring(0, 2), 16)
    val g = Integer.valueOf(str.substring(2, 4), 16)
    val b = Integer.valueOf(str.substring(4, 6), 16)
    val a = if (str.length == 8) Integer.valueOf(str.substring(6, 8), 16) else 255

    color(r / 255f, g / 255f, b / 255f, a / 255f)
  } catch (e: NumberFormatException) {
    null
  }
}

internal fun Color.toHexString() =
        String.format("#%02x%02x%02x%02x", red, green, blue, alpha)

internal fun Color.toRGBComponents(): List<Pair<String, Float>> {
  val components = getRGBComponents(null)
  return listOf(
          "r" to components[0],
          "g" to components[1],
          "b" to components[2],
          "a" to components[3]
  )
}

internal fun createColorIcon(color: Color): Icon = ColorIcon(if (UIUtil.isRetina()) 24 else 12, color, true)

open class GutterColorRenderer(val color: Color): GutterIconRenderer() {

  override fun getIcon(): Icon = createColorIcon(color)

  override fun equals(other: Any?) = other is GutterColorRenderer && color == other.color

  override fun hashCode() = color.hashCode()

}