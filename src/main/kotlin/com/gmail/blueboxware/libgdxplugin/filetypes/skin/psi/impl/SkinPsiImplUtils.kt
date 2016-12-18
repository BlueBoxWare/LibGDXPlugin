package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.intellij.icons.AllIcons
import com.intellij.json.psi.impl.JsonPsiImplUtils
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.PlatformIcons
import javax.swing.Icon

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
class SkinPsiImplUtils {

  companion object {

    private val STRING_FRAGMENTS = Key<List<Pair<TextRange, String>>>("LibGDX Skin string fragments")

    @JvmStatic
    fun getName(property: SkinProperty) = StringUtil.unescapeStringCharacters(SkinPsiUtil.stripQuotes(property.nameElement.text))

    /**
     * Actually only JSON string literal should be accepted as valid name of property according to standard,
     * but for compatibility with JavaScript integration any JSON literals as well as identifiers (unquoted words)
     * are possible and highlighted as error later.
     *
     * @see JsonStandardComplianceInspection
     */
    @JvmStatic
    fun getNameElement(property: SkinProperty): SkinValue {
      val firstChild = property.firstChild
      if (firstChild is SkinLiteral || firstChild is SkinReferenceExpression) {
        return firstChild as SkinValue
      } else {
        throw AssertionError()
      }
    }

    @JvmStatic
    fun getValue(property: SkinProperty) = PsiTreeUtil.getNextSiblingOfType(getNameElement(property), SkinValue::class.java)

    @JvmStatic
    fun getValue(literal: SkinStringLiteral) = StringUtil.unescapeStringCharacters(SkinPsiUtil.stripQuotes(literal.text))

    @JvmStatic
    fun getValue(literal: SkinBooleanLiteral) = literal.textMatches("true")

    @JvmStatic
    fun getValue(literal: SkinNumberLiteral) = try {
      literal.text.toDouble()
    } catch (e: NumberFormatException) {
      0.0
    }

    @JvmStatic
    fun getPresentation(property: SkinProperty) = object : ItemPresentation {
      override fun getLocationString() = null

      override fun getIcon(unused: Boolean): Icon {
        if (property.getValue() is SkinArray) {
          return AllIcons.Json.Property_brackets
        }
        if (property.getValue() is SkinObject) {
          return AllIcons.Json.Property_braces
        }
        return PlatformIcons.PROPERTY_ICON
      }

      override fun getPresentableText() = property.name
    }

    @JvmStatic
    fun getPresentation(array: SkinArray) = object : ItemPresentation {
      override fun getPresentableText() = "array"

      override fun getLocationString(): String? = null

      override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Array
    }

    @JvmStatic
    fun getPresentation(obj: SkinObject) = object : ItemPresentation {
      override fun getPresentableText(): String? = "object"

      override fun getLocationString(): String? = null

      override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Object
    }

    @JvmStatic
    fun isQuotedString(literal: SkinLiteral) = literal.node.findChildByType(SkinParserDefinition.STRING_LITERALS)

    @JvmStatic
    fun getTextFragments(literal: SkinStringLiteral) = JsonPsiImplUtils.getTextFragments(literal.toJsonStringLiteral())

//    @JvmStatic
//    fun getTextFragments(literal: SkinStringLiteral): List<Pair<TextRange, String>> {
//
//        val ourEscapesTable = """\"\"\\\\//b\bf\fn\nr\rt\t"""
//
//      var result = literal.getUserData(STRING_FRAGMENTS)
//
//      if (result == null) {
//        result = mutableListOf<Pair<TextRange, String>>()
//
//        val text = literal.text
//        val length = text.length
//        var pos = 1
//        var unescapedSequenceStart = 1
//
//        while (pos < length) {
//
//          if (text[pos] == '\\') {
//            if (unescapedSequenceStart != pos) {
//              result.add(TextRange(unescapedSequenceStart, pos) to text.substring(unescapedSequenceStart, pos))
//            }
//            if (pos == length - 1) {
//              result.add(TextRange(pos, pos + 1) to "\\")
//              break
//            }
//
//            val next = text[pos + 1]
//            when (next) {
//              '"', '\\', '/', 'b', 'f', 'n', 'r', 't'   -> {
//                val idx = ourEscapesTable.indexOf(next)
//                result.add(TextRange(pos, pos + 2) to ourEscapesTable.substring(idx + 1, idx + 2))
//                pos += 2
//              }
//              'u' -> {
//                for (i in pos + 2 .. pos + 5) {
//                  if (i == length || !StringUtil.isHexDigit(text[i])) {
//                    result.add(TextRange(pos, i) to text.substring(pos, i))
//                    pos = i
//                    break
//                  }
//                }
//              }
//              else -> {
//                result.add(TextRange(pos, pos + 2) to text.substring(pos, pos + 2))
//                pos += 2
//              }
//            }
//            unescapedSequenceStart = pos
//          } else {
//            pos++
//          }
//
//        }
//        literal.putUserData(STRING_FRAGMENTS, result)
//      }
//
//      return result
//    }
//
//  }
  }
}