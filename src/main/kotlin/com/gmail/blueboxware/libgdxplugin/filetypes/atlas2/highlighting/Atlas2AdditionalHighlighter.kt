package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement

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
internal class Atlas2AdditionalHighlighter : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        var attributes: TextAttributesKey? = null

        if (element is Atlas2Key) {
            attributes = Atlas2SyntaxHighlighter.KEY
        } else if (element is Atlas2Value) {
            attributes = Atlas2SyntaxHighlighter.VALUE
        } else if (element is Atlas2Header) {
            if (element.parent is Atlas2Page) {
                attributes = Atlas2SyntaxHighlighter.FILE_NAME
            } else if (element.parent is Atlas2Region) {
                attributes = Atlas2SyntaxHighlighter.TEXTURE_NAME
            }
        }

        attributes?.let {
            val ab = if (ApplicationManager.getApplication().isUnitTestMode) {
                holder.newAnnotation(HighlightSeverity.INFORMATION, attributes.externalName)
            } else {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            }
            ab.textAttributes(it).range(element).create()
        }
    }
}
