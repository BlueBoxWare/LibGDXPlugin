package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFile
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontFontChar
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontKerning
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset

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
class BitmapFontFoldingBuilder : FoldingBuilder, DumbAware {

  override fun getPlaceholderText(node: ASTNode): String? {

    val element = node.psi

    if (element is BitmapFontKerning) {
      return " Kernings ..."
    } else if (element is BitmapFontFontChar) {
      return " Characters ..."
    }

    return "..."

  }

  override fun buildFoldRegions(node: ASTNode, document: Document): Array<out FoldingDescriptor> {

    val fontFile = (node.psi as? BitmapFontFile) ?: return arrayOf()
    val text = fontFile.text

    val descriptors = mutableListOf<FoldingDescriptor>()

    val characters = fontFile.getCharacters()

    val firstChar = characters.firstOrNull()
    val lastChar = characters.lastOrNull()

    if (firstChar != null && lastChar != null && firstChar != lastChar) {
      var end = lastChar.endOffset
      while (end > firstChar.startOffset && text[end - 1] == '\n') {
        end--
      }
      descriptors.add(FoldingDescriptor(
              firstChar,
              TextRange(firstChar.startOffset, end)
      ))
    }

    val kernings = fontFile.kernings

    val firstKerning = kernings.firstOrNull()
    val lastKerning = kernings.lastOrNull()

    if (firstKerning != null && lastKerning != null && firstKerning != lastKerning) {
      var end = lastKerning.endOffset
      while (end > firstKerning.startOffset && text[end - 1] == '\n') {
        end--
      }
      descriptors.add(FoldingDescriptor(
              firstKerning,
              TextRange(firstKerning.startOffset, end)
      ))
    }

    return descriptors.toTypedArray()

  }

  override fun isCollapsedByDefault(node: ASTNode) = false
}