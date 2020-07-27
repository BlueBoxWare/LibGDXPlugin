package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.getValue
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasPage
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.AtlasElementImpl
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.codeInsight.preview.ImagePreviewComponent
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.util.IconUtil
import com.intellij.util.IncorrectOperationException
import org.jetbrains.kotlin.idea.search.allScope
import java.awt.image.BufferedImage
import java.awt.image.RasterFormatException
import java.io.IOException
import javax.swing.Icon

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
abstract class AtlasRegionMixin(node: ASTNode): AtlasRegion, AtlasElementImpl(node) {

  override fun getName(): String = regionName.text

  override fun getX() = xy.valueList.firstOrNull()?.text?.toIntOrNull()

  override fun getY() = xy.valueList.getOrNull(1)?.text?.toIntOrNull()

  override fun getWidth() = size.valueList.firstOrNull()?.text?.toIntOrNull()

  override fun getHeight() = size.valueList.getOrNull(1)?.text?.toIntOrNull()

  override fun getOriginalWidth() = orig.valueList.firstOrNull()?.text?.toIntOrNull() ?: 0

  override fun getOriginalHeight() = orig.valueList.getOrNull(1)?.text?.toIntOrNull() ?: 0

  override fun getOriginalSize() = originalWidth * originalHeight

  override fun getPage(): AtlasPage? = getParentOfType()

  override fun setName(name: String): PsiElement = throw IncorrectOperationException()

  override fun getUseScope() = project.allScope()

  override fun getPreviewIcon(): Icon? = myPreviewIcon

  override fun getImage(): BufferedImage? {
    val virtualFile = page?.imageFile ?: return null
    val x = x ?: return null
    val y = y ?: return null
    val width = width ?: return null
    val height = height ?: return null

    return try {
      ImagePreviewComponent.readImageFromBytes(virtualFile.contentsToByteArray()).getSubimage(x, y, width, height)
    } catch (e: IOException) {
      null
    } catch (e: RasterFormatException) {
      null
    }

  }

  private val myPreviewIcon: Icon? by lazy {
    image?.let { image: BufferedImage ->
      IconUtil.createImageIcon(image.getScaledInstance(16, 16, BufferedImage.SCALE_DEFAULT))
    }
  }

  override fun getPresentation() = object: ItemPresentation {

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean) = AllIcons.FileTypes.UiForm

    override fun getPresentableText() = name + index.value?.getValue()?.let { if (it != "-1") " ($it)" else "" }

  }

}