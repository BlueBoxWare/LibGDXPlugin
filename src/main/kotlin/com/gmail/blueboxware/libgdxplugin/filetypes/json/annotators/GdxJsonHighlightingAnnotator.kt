package com.gmail.blueboxware.libgdxplugin.filetypes.json.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
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
class GdxJsonHighlightingAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        fun a(textAttribute: TextAttributesKey) = element.annotate(holder, textAttribute)

        if (element is GdxJsonPropertyName) {
            a(JSON_PROPERTY_KEY)
        } else if (element is GdxJsonString) {
            when {
                element.isKeyword -> {
                    a(JSON_KEYWORD)
                }

                element.isNumber -> {
                    a(JSON_NUMBER)
                }

                else -> {
                    a(JSON_STRING)
                }
            }
        }

    }

    private fun PsiElement.annotate(holder: AnnotationHolder, textAttribute: TextAttributesKey) {

        val msg = if (ApplicationManager.getApplication().isUnitTestMode) {
            textAttribute.externalName
        } else {
            null
        }

        val annotation = if (msg != null)
            holder.newAnnotation(HighlightSeverity.INFORMATION, msg)
        else
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)

        annotation
            .range(this)
            .textAttributes(textAttribute)
            .create()

    }

}
