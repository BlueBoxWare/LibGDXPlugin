package com.gmail.blueboxware.libgdxplugin.filetypes.skin

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase

/*
 * Copyright 2016 Blue Box Ware
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
object SkinParserUtil : GeneratedParserUtilBase() {

    @JvmStatic
    fun parseSeparator(builder: PsiBuilder, level: Int): Boolean {

        var i = builder.currentOffset
        val originalText = builder.originalText

        while (i > 0) {
            val c = originalText[i - 1]
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                break
            }
            i--
        }

        var separatorFound = false

        val length = originalText.length
        while (i < length) {
            val c = originalText[i]
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n' && c != ',') {
                break
            }
            if (c == '\r' || c == '\n' || c == ',') {
                separatorFound = true
                break
            }
            i++
        }

        while (builder.tokenType == SkinElementTypes.COMMA) {
            builder.advanceLexer()
        }

        return separatorFound

    }

    @Suppress("SameReturnValue")
    @JvmStatic
    fun parseOtionalComma(builder: PsiBuilder, level: Int): Boolean {

        while (builder.tokenType == SkinElementTypes.COMMA) {
            builder.advanceLexer()
        }

        return true

    }

}
