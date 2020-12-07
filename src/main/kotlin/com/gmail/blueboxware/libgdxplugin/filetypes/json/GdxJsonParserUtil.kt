package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.COMMA
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
object GdxJsonParserUtil: GeneratedParserUtilBase() {

  @JvmStatic
  fun parseSeparator(builder: PsiBuilder, @Suppress("UNUSED_PARAMETER") level: Int): Boolean {

    if (builder.tokenType == COMMA) {
      builder.advanceLexer()
      return true
    }

    var i = builder.currentOffset - 1
    var inComment = false

    while (i >= 0) {
      if (inComment) {
        if (builder.originalText[i] == '/' && builder.originalText[i + 1] == '*') {
          inComment = false
        }
      } else {
        if (i > 0 && builder.originalText[i - 1] == '*' && builder.originalText[i] == '/') {
          inComment = true
        } else if (builder.originalText[i] == '\n') {
          return true
        } else if (!builder.originalText[i].isWhitespace()) {
          break
        }
      }
      i--
    }

    return false

  }

}