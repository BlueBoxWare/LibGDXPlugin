package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFile
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontKerning
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.BitmapFontFontCharImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.PropertyContainerImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.references.BitmapFontCharReference
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiReference

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
abstract class BitmapFontKerningMixin(node: ASTNode) : BitmapFontKerning, PropertyContainerImpl(node) {

    override fun getFirstCharacter(): BitmapFontFontCharImpl? =
        getValue("first")
            ?.toIntOrNull()
            ?.let { (containingFile as? BitmapFontFile)?.getCharacter(it) as? BitmapFontFontCharImpl }

    override fun getSecondCharacter(): BitmapFontFontCharImpl? =
        getValue("second")
            ?.toIntOrNull()
            ?.let { (containingFile as? BitmapFontFile)?.getCharacter(it) as? BitmapFontFontCharImpl }

    override fun getPresentation() = object : ItemPresentation {

        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean) = IconLoader.getDisabledIcon(AllIcons.Nodes.Tag)

        override fun getPresentableText() =
            firstCharacter?.presentation?.presentableText +
                    " - " +
                    secondCharacter?.presentation?.presentableText +
                    ": " +
                    (getValue("amount") ?: "<unknown>")

    }

    override fun getReference(): PsiReference? = BitmapFontCharReference(this)
}
