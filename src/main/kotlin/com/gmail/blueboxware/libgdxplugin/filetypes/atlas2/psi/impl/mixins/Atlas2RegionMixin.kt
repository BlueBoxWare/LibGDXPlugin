package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Page
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.Atlas2FieldOwnerImpl
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException
import java.awt.image.BufferedImage
import java.awt.image.RasterFormatException
import java.io.IOException
import javax.imageio.ImageIO


/*
 * Copyright 2022 Blue Box Ware
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
abstract class Atlas2RegionMixin(node: ASTNode) : Atlas2Region, Atlas2FieldOwnerImpl(node) {

    override fun getIndex(): Int? = getFieldValue("index")?.toIntOrNull()

    override fun getName(): String? = header.value

    override fun getPage(): Atlas2Page? = getParentOfType()

    override fun getX(): Int? = getFieldValueI("bounds") ?: getFieldValueI("xy")

    override fun getY(): Int? = getFieldValuesI("bounds")?.getOrNull(1) ?: getFieldValuesI("xy")?.getOrNull(1)

    override fun getWidth(): Int? = getFieldValuesI("bounds")?.getOrNull(2) ?: getFieldValueI("size")

    override fun getHeight(): Int? = getFieldValuesI("bounds")?.getOrNull(3) ?: getFieldValuesI("size")?.getOrNull(1)

    override fun setName(name: String): PsiElement = throw IncorrectOperationException()

    override fun getImage(): BufferedImage? {
        val virtualFile = page?.imageFile ?: return null
        val x = x ?: return null
        val y = y ?: return null
        val width = width ?: return null
        val height = height ?: return null

        return try {
            ImageIO.read(virtualFile.inputStream)?.getSubimage(x, y, width, height)
        } catch (e: IOException) {
            LOG.debug(e)
            null
        } catch (e: RasterFormatException) {
            LOG.debug(e)
            null
        }

    }

    override fun getPresentation() = object : ItemPresentation {

        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean) = AllIcons.FileTypes.UiForm

        override fun getPresentableText() = name + (index?.let { if (it == -1) "" else " ($it)" } ?: "")

    }

    companion object {
        val LOG = Logger.getInstance(Atlas2RegionMixin::class.java)
    }

}
