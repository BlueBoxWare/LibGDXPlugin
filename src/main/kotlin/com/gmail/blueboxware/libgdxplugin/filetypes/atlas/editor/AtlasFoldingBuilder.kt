package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.getValue
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasPage
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
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
class AtlasFoldingBuilder : FoldingBuilder, DumbAware {

  override fun getPlaceholderText(node: ASTNode): String {

    val element = node.psi

    if (element is AtlasPage) {
      return "Page: " + element.pageName.getValue() + " ..."
    } else if (element is AtlasRegion) {
      return "Texture: " + element.regionName.getValue() + " ..."
    }

    return "..."

  }

  override fun isCollapsedByDefault(node: ASTNode) = false

  override fun buildFoldRegions(node: ASTNode, document: Document): Array<out FoldingDescriptor> {

    val descriptors = mutableListOf<FoldingDescriptor>()

    collectDescriptorsRecursively(node.psi, document, descriptors)

    return descriptors.toTypedArray()

  }

  private fun collectDescriptorsRecursively(element: PsiElement, document: Document, descriptors: MutableList<FoldingDescriptor>) {

    if (element is AtlasPage) {
      descriptors.add(FoldingDescriptor(element, element.textRange))
    } else if (element is AtlasRegion) {
      val start = element.startOffset
      var end = element.endOffset
      while (end > start && element.text[end-start-1] == '\n') {
        end--
      }
      descriptors.add(FoldingDescriptor(element, TextRange(start, end)))
    }

    for (child in element.children) {
      collectDescriptorsRecursively(child, document, descriptors)
    }
  }
}

