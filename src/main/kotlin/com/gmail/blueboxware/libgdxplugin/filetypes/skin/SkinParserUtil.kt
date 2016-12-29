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

    while (i > 0 && builder.originalText[i - 1] in listOf(' ', '\t', '\r', '\n')) {
      i--
    }

    var separatorFound = false

    while (i < builder.originalText.length && builder.originalText[i] in listOf(' ', '\t', '\r', '\n', ',') ) {
      if (builder.originalText[i] == '\r' || builder.originalText[i] == '\n' || builder.originalText[i] == ',') {
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

  @JvmStatic
  fun parseOtionalComma(builder: PsiBuilder, level: Int): Boolean {

    while (builder.tokenType == SkinElementTypes.COMMA) {
      builder.advanceLexer()
    }

    return true

  }

}