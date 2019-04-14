package com.gmail.blueboxware.libgdxplugin.filetypes.json.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement


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

    when (element) {
      is GdxJsonPropertyName -> a(JSON_PROPERTY_KEY)
      is GdxJsonNull, is GdxJsonBoolean -> a(JSON_KEYWORD)
      is GdxJsonNumber -> a(JSON_NUMBER)
      is GdxJsonString -> a(JSON_STRING)
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