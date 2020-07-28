package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.util.ui.ImageUtil
import java.awt.Color
import java.awt.image.BufferedImage

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
internal fun BufferedImage.tint(color: Color): BufferedImage {

  val tintedImage = ImageUtil.createImage(width, height, BufferedImage.TYPE_INT_ARGB)

  for (x in 0 until width) {
    for (y in 0 until height) {
      val oldColor = Color(getRGB(x, y))
      val r = mul(oldColor.red, color.red)
      val g = mul(oldColor.green, color.green)
      val b = mul(oldColor.blue, color.blue)
      val a = mul(oldColor.alpha, color.alpha)
      tintedImage.setRGB(x, y, (a shl 24) or (r shl 16) or (g shl 8) or b)
    }
  }

  return tintedImage

}

private fun mul(x: Int, y: Int): Int = (x * y / 255f).toInt()
