package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonHighlightingAnnotator: Annotator {

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    fun a(textAttribute: TextAttributesKey) = element.annotate(holder, textAttribute)

    if (element is LeafPsiElement && element.getParentOfType<GdxJsonString>() == null) {
      when (element.elementType) {
        L_CURLY, R_CURLY -> a(JSON_BRACES)
        L_BRACKET, R_BRACKET -> a(JSON_BRACKETS)
        COMMA -> a(JSON_COMMA)
        COLON -> a(JSON_COLON)
      }
    } else {
      when (element) {
        is GdxJsonPropertyName -> a(JSON_PROPERTY_KEY)
        is GdxJsonComment -> a(JSON_BLOCK_COMMENT)
        is GdxJsonNull, is GdxJsonBoolean -> a(JSON_KEYWORD)
        is GdxJsonNumber -> a(JSON_NUMBER)
      }
    }

  }

  private fun PsiElement.annotate(holder: AnnotationHolder, textAttribute: TextAttributesKey) {

    val msg = if (ApplicationManager.getApplication().isUnitTestMode) {
      textAttribute.externalName
    } else {
      null
    }

    holder.createInfoAnnotation(this, msg).apply {
      textAttributes = textAttribute
    }
  }

}