package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.utils.advanceLexer
import com.gmail.blueboxware.libgdxplugin.utils.isFollowedByNewline
import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
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

          mark.drop()
        }
      }
    }

    return true
  }

  @JvmStatic
  fun parseUnquotedNameString(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType == COLON) {
      builder.error("<property name> expected")
      return true
    } else if (builder.tokenType in UNQUOTED_NAME_STRING_INVALID_STARTERS || builder.eof()) {
      return false
    }

    return parseUnquotedString(builder, UNQUOTED_NAME_STRING_TERMINATORS).isNotBlank()

  }

  @JvmStatic
  fun parseQuotedString(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType != DOUBLE_QUOTE) {
      return false
    }

    val mark = builder.mark()
    builder.advanceLexer()
    builder.setTokenTypeRemapper { source, start, end, text ->
      if (source == LINE_COMMENT) {
        return@setTokenTypeRemapper ANY_CHAR
      }
      return@setTokenTypeRemapper source
    }

    while (!builder.eof() && builder.tokenType != DOUBLE_QUOTE) {
      if (builder.tokenType == BACK_SLASH) {
        builder.advanceLexer()
      }

      builder.advanceLexer()

    }

    if (builder.tokenType == DOUBLE_QUOTE) {
      builder.advanceLexer()
    }

    mark.done(STRING)
    builder.setTokenTypeRemapper(null)

    return true

  }

  @JvmStatic
  fun parseComment(builder: PsiBuilder, level: Int): Boolean {

    if (builder.tokenType !in COMMENT_STARTERS) {
      return false
    }

    val mark = builder.mark()

    builder.advanceLexer()

    if (builder.tokenType == SLASH) {
      while (!builder.eof()) {
        if (builder.isFollowedByNewline()) {
          builder.advanceLexer()
          break
        }
        builder.advanceLexer()
      }
      mark.done(LINE_COMMENT)
    } else if (builder.tokenType == ASTERIX) {
      while (!builder.eof()) {
        if (builder.tokenType == ASTERIX && builder.rawLookup(1) == SLASH) {
          builder.advanceLexer(2)
          break
        }
        builder.advanceLexer()
      }
      mark.done(BLOCK_COMMENT)
    } else {
      mark.drop()
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

  private fun parseUnquotedString(builder: PsiBuilder, terminatingChars: TokenSet): CharSequence {

    val start = builder.currentOffset

    while (!builder.eof()) {

      if (builder.tokenType in terminatingChars) {
        break
      } else if (builder.isFollowedByNewline() || builder.nextNonWhiteSpace() in GdxJsonParserDefinition.COMMENTS) {
        builder.advanceLexer()
        break
      }

      builder.advanceLexer()

    }

    return builder.originalText.subSequence(start, builder.currentOffset)

  }

  private fun PsiBuilder.nextNonWhiteSpace(): IElementType? {
    var i = 1
    var token = rawLookup(i)

    while (token != null) {
      if (token != WHITE_SPACE) {
        return token
      }
      i++
      token = rawLookup(i)
    }

    return null

  }

}