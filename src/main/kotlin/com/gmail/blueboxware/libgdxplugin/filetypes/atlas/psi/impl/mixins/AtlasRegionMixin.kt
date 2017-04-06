package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.getValue
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.AtlasElementImpl
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.IncorrectOperationException
import icons.ImagesIcons

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
abstract class AtlasRegionMixin(node: ASTNode) : AtlasRegion, AtlasElementImpl(node) {

  override fun getName() = regionName.text

  override fun setName(name: String): PsiElement = throw IncorrectOperationException()

  override fun getUseScope() = GlobalSearchScope.allScope(project)

  override fun getPresentation() = object : ItemPresentation {

    override fun getLocationString() = null

    override fun getIcon(unused: Boolean) = ImagesIcons.ImagesFileType

    override fun getPresentableText() = name + index.value?.getValue()?.let { if (it != "-1") " ($it)" else ""}

  }

}