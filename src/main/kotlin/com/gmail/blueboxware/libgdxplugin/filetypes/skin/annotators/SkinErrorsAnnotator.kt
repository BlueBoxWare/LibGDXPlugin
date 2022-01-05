package com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.isEscapedChar
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.unescape
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
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
class SkinErrorsAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element is SkinStringLiteral) {

            // Missing closing quote
            val length = element.text.length
            if (length > 0) {
                val firstChar = element.text.firstOrNull()
                val lastChar = element.text.lastOrNull()
                if (firstChar == '\"') {
                    if (length == 1 || (length > 1 && (lastChar != '\"' || element.text.isEscapedChar(length - 1)))) {
                        holder
                            .newAnnotation(HighlightSeverity.ERROR, message("skin.error.annotator.closing.quote"))
                            .range(element)
                            .create()
                        return
                    }
                }
            }

            // Illegal escape sequences
            element.text.unescape { start, end ->
                val absStart = element.startOffset + start
                val absEnd = element.startOffset + end
                holder
                    .newAnnotation(HighlightSeverity.ERROR, message("skin.error.annotator.escape"))
                    .range(TextRange(absStart, absEnd))
                    .create()
            }

        }

    }

}
