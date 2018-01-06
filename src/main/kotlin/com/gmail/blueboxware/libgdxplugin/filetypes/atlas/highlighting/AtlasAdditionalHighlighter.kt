package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasValueElement
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasPage
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement

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
class AtlasAdditionalHighlighter : Annotator {

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if ((element as? LeafPsiElement)?.elementType != AtlasElementTypes.STRING) return

    var attributes: TextAttributesKey? = null

    if (element.parent is AtlasValueElement && element.parent.parent is AtlasPage) {
      attributes = AtlasSyntaxHighlighter.FILE_NAME
    } else if (element.parent is AtlasValueElement && element.parent.parent is AtlasRegion) {
      attributes = AtlasSyntaxHighlighter.TEXTURE_NAME
    } else if (element.parent is AtlasProperty) {
      attributes = AtlasSyntaxHighlighter.KEY
    } else if (element.parent is AtlasValueElement) {
      attributes = AtlasSyntaxHighlighter.VALUE
    }

    attributes?.let {
      holder.createInfoAnnotation(element, null).apply { textAttributes = it }
    }
  }
}