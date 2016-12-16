package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.util.ui.ColorIcon
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

class GutterColorRenderer(val color: Color): GutterIconRenderer() {

  override fun getIcon(): Icon = ColorIcon(10, color, true)

  override fun equals(other: Any?) = other is GutterColorRenderer && color == other.color

  override fun hashCode() = color.hashCode()

}