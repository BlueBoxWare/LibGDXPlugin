package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.utils.advanceLexer
import com.gmail.blueboxware.libgdxplugin.utils.isFollowedByNewline
import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.TokenSet

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

  private val UNQUOTED_VALUE_STRING_INVALID_STARTERS = TokenSet.create(DOUBLE_QUOTE, COLON, COMMA, L_CURLY, SLASH, L_BRACKET, R_BRACKET)
  private val UNQUOTED_VALUE_STRING_TERMINATORS = TokenSet.create(R_CURLY, R_BRACKET, COMMA)

  private val UNQUOTED_NAME_STRING_INVALID_STARTERS = TokenSet.create(DOUBLE_QUOTE, COLON, COMMA, R_CURLY, SLASH)
  private val UNQUOTED_NAME_STRING_TERMINATORS = TokenSet.create(COLON)

  private val COMMENT_STARTERS = TokenSet.create(ASTERIX, SLASH)

  @JvmStatic
  fun parseUnquotedValueString(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType in UNQUOTED_VALUE_STRING_INVALID_STARTERS || builder.eof()) {
      return false
    }

    val mark = builder.mark()

    val result = parseUnquotedString(builder, UNQUOTED_VALUE_STRING_TERMINATORS).toString().trim()

    if (result.isBlank()) {
      mark.rollbackTo()
      return false
    } else {
      when (result) {
        "null" -> mark.done(NULL)
        "true", "false" -> mark.done(BOOLEAN)
        else -> {

          try {
            java.lang.Double.parseDouble(result)
            mark.done(NUMBER)
            return true
          } catch (e: NumberFormatException) {
          }
          try {
            java.lang.Long.parseLong(result)
            mark.done(NUMBER)
            return true
          } catch (e: NumberFormatException) {
          }

          mark.done(STRING)
        }
      }
    }

    return true
  }

  @JvmStatic
  fun parseUnquotedNameString(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType in UNQUOTED_NAME_STRING_INVALID_STARTERS || builder.eof()) {
      return false
    }

    return parseUnquotedString(builder, UNQUOTED_NAME_STRING_TERMINATORS).isNotBlank()

  }

  @JvmStatic
  fun parseQuotedChars(builder: PsiBuilder, level: Int): Boolean {

    while (!builder.eof() && builder.tokenType != DOUBLE_QUOTE) {
      if (builder.tokenType == BACK_SLASH) {
        builder.advanceLexer()
      }

      builder.advanceLexer()

    }

    return true

  }

  @JvmStatic
  fun parseComment(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType !in COMMENT_STARTERS) {
      return false
    }

    builder.advanceLexer()

    if (builder.tokenType == SLASH) {
      while (!builder.eof()) {
        if (builder.isFollowedByNewline()) {
          builder.advanceLexer()
          break
        }
        builder.advanceLexer()
      }
    } else if (builder.tokenType == ASTERIX) {
      while (!builder.eof()) {
        if (builder.tokenType == ASTERIX && builder.rawLookup(1) == SLASH) {
          builder.advanceLexer(2)
          break
        }
        builder.advanceLexer()
      }
    } else {
      return false
    }

    return true

  }

  @JvmStatic
  fun parseSeparator(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType == COMMA) {
      builder.advanceLexer()
      return true
    }

    var i = builder.currentOffset - 1

    while (i >= 0 && builder.originalText[i] in listOf(' ', '\t', '\n')) {
      if (builder.originalText[i] == '\n') {
        return true
      }
      i--
    }

    return false

  }

  private fun parseUnquotedString(builder: PsiBuilder, terminatingChars: TokenSet): CharSequence {

    var stop = false
    val start = builder.currentOffset

    while (!builder.eof() && !stop) {

      when (builder.tokenType) {
        SLASH ->
          if (builder.rawLookup(1) in COMMENT_STARTERS) {
            stop = true
          }
        in terminatingChars -> stop = true
        else -> if (builder.isFollowedByNewline()) {
          stop = true
          builder.advanceLexer()
        }
      }

      if (!stop) {
        builder.advanceLexer()
      }

    }

    return builder.originalText.subSequence(start, builder.currentOffset)

  }

}