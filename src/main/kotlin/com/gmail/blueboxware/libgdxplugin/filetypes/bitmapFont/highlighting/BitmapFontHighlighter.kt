package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.highlighting

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontKey
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontValue
import com.gmail.blueboxware.libgdxplugin.utils.isLeaf
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
class BitmapFontHighlighter : Annotator {

    companion object {
        val KEYWORDS = listOf("info", "common", "page", "chars", "char", "kernings", "kerning")
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (element is BitmapFontProperty) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.keyElement)
                .textAttributes(BitmapFontColorSettingsPage.KEY)
                .create()
            element.valueElement?.let {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(it)
                    .textAttributes(BitmapFontColorSettingsPage.VALUE)
                    .create()
            }

            element.node.getChildren(null).forEach { node ->
                if (node.text == "=") {
                    holder
                        .newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(node)
                        .textAttributes(BitmapFontColorSettingsPage.EQUALS_SIGN)
                        .create()
                }
            }

            element.valueElement?.let { valueElement ->
                val valueText = valueElement.text
                val start = valueElement.startOffset
                for (i in valueText.indices) {
                    if (valueText[i] == ',') {
                        holder
                            .newSilentAnnotation(HighlightSeverity.INFORMATION)
                            .range(TextRange(start + i, start + i + 1))
                            .textAttributes(BitmapFontColorSettingsPage.COMMA)
                            .create()
                    }
                }
            }

        } else if (element.isLeaf(BitmapFontElementTypes.UNQUOTED_STRING)) {
            if (KEYWORDS.contains(element.text) && element.parent !is BitmapFontKey && element.parent !is BitmapFontValue) {
                holder
                    .newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(BitmapFontColorSettingsPage.KEYWORD)
                    .create()
            }
        }

    }

}
