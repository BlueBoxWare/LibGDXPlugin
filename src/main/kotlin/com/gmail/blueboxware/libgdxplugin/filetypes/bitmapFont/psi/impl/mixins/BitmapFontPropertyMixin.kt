package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.BitmapFontElementImpl
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
abstract class BitmapFontPropertyMixin(node: ASTNode) : BitmapFontProperty, BitmapFontElementImpl(node) {

  override fun getKey(): String = keyElement.unquotedString.text

  override fun getValue(): String? = valueElement?.value

  override fun getPresentation() = object : ItemPresentation {

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean) = AllIcons.Nodes.Property

    override fun getPresentableText() = key + ": " + valueElement?.text

  }

}