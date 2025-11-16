package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontFontChar
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.PropertyContainerImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation

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
abstract class BitmapFontFontCharMixin(node: ASTNode) : BitmapFontFontChar, PropertyContainerImpl(node) {

    override fun getCharacter(): Int? = getValue("id")?.toIntOrNull()

    override fun getLetter(): String? {

        getValue("letter")?.firstOrNull()?.let { letter ->

            return when {
                letter == ' ' -> "space"
                letter == '\t' -> "tab"
                letter == '\n' -> "newline"
                letter == '\r' -> "return"
                Character.isISOControl(letter) -> null
                else -> "'$letter'"
            }

        }

        return null

    }

    override fun getPresentation() = object : ItemPresentation {

        override fun getLocationString() = ""

        override fun getIcon(unused: Boolean) = AllIcons.Nodes.Class

        override fun getPresentableText() = getCharacter().toString() + (getLetter()?.let { " ($it)" } ?: "")

    }
}
