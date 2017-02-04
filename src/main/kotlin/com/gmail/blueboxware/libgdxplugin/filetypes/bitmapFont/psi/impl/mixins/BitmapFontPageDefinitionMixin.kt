package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontPageDefinition
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
abstract class BitmapFontPageDefinitionMixin(node: ASTNode) : BitmapFontPageDefinition, PropertyContainerImpl(node) {

  override fun getPresentation() = object : ItemPresentation {

    override fun getLocationString() = null

    override fun getIcon(unused: Boolean) = AllIcons.FileTypes.Any_type

    override fun getPresentableText() = "Page " + getValue("id")

  }

}