package com.gmail.blueboxware.libgdxplugin.filetypes.atlas

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.AtlasElementImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.util.PsiTreeUtil

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
abstract class AtlasProperty(node: ASTNode) : AtlasElementImpl(node), NavigationItem {

  fun getKey(): String = when (this) {
    is AtlasFormat -> "Format"
    is AtlasFilter -> "Filter"
    is AtlasRepeat -> "Repeat"
    is AtlasRotate -> "Rotate"
    is AtlasXy     -> "XY"
    is AtlasSize   -> "Size"
    is AtlasSplit  -> "Split"
    is AtlasPad    -> "Pad"
    is AtlasOrig   -> "Orig"
    is AtlasOffset -> "Offset"
    is AtlasIndex  -> "Index"
    else -> "<unknown>"
  }

  fun getValueAsString() = (PsiTreeUtil.findChildrenOfType(this, AtlasValueElement::class.java)).map { it.text }.joinToString(separator = ", ")

  override fun getPresentation() = object : ItemPresentation {

    override fun getLocationString() = null

    override fun getIcon(unused: Boolean) = AllIcons.Nodes.Property

    override fun getPresentableText() = getKey() + ": " + getValueAsString()
  }

}