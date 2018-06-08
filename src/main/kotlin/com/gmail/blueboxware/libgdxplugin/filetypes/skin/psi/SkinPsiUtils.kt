package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition.Companion.SKIN_COMMENTARIES
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.getSkinTag2ClassMap
import com.gmail.blueboxware.libgdxplugin.utils.removeDollarFromClassName
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import java.lang.*

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
internal fun String.stripQuotes(): String {
  if (length > 0) {
    val firstChar = firstOrNull()
    val lastChar = lastOrNull()
    if (firstChar == '\"') {
      if (length > 1 && lastChar == '\"' && !isEscapedChar(length - 1)) {
        return substring(1, length - 1)
      }
      return substring(1)
    }
  }

  return this
}

internal fun String.isEscapedChar(position: Int): Boolean {
  var count = 0
  for (i in position - 1 downTo 0) {
    if (getOrNull(i) != '\\') break
    count++
  }
  return count % 2 != 0
}

internal fun String.isQuoted(): Boolean = length > 1 && firstOrNull() == '\"' && lastOrNull() == '\"' && !isEscapedChar(length - 1)

internal fun String.escape(): String = StringUtil.escapeStringCharacters(this)

internal fun String.makeSafe(): String =
  if (!isQuoted() && this.any { listOf('{', '}', ':', '[', ']', ',').contains(it) }) {
    "\"" + this.escape() + "\""
  } else {
    this.escape()
  }


internal fun String.unescape(onError: ((Int, Int) -> Unit)? = null): String {
  val result = StringBuilder()
  var i = 0
  while (i < length) {
    var c = get(i++)

    if (c != '\\') {
      result.append(c)
      continue
    }

    if (i == length) break
    c = get(i++)

    if (c == 'u') {
      try {
        result.append(Character.toChars(Integer.parseInt(substring(i, i + 4), 16)))
      } catch (e: Exception) {
        if (e is IllegalArgumentException || e is NumberFormatException || e is IndexOutOfBoundsException) {
          onError?.invoke(i - 2, minOf(i + 3, length))
        } else {
          throw e
        }
      }
      i += 4
      continue
    }

    when (c) {
      '"', '\\', '/' -> {
      }
      'b' -> c = '\b'
      'f' -> c = '\u000c'
      'n' -> c = '\n'
      'r' -> c = '\r'
      't' -> c = '\t'
      else -> {
        onError?.invoke(i - 2, i)
      }
    }

    result.append(c)
  }

  return result.toString()
}

fun findFurthestSiblingOfSameType(anchor: PsiElement, after: Boolean): PsiElement {

  var node = anchor.node
  val expectedType = node.elementType
  var lastSeen = node

  while (node != null) {
    val elementType = node.elementType
    if (elementType == expectedType) {
      lastSeen = node
    } else if (elementType == TokenType.WHITE_SPACE) {
      if (expectedType == SkinElementTypes.LINE_COMMENT && node.text.indexOf('\n', 1) != -1) {
        break
      }
    } else if (!SKIN_COMMENTARIES.contains(elementType) || SKIN_COMMENTARIES.contains(expectedType)) {
      break
    }
    node = if (after) node.treeNext else node.treePrev
  }

  return lastSeen.psi
}

fun findPreviousSibling(psiElement: PsiElement, matching: (PsiElement) -> Boolean): PsiElement? {
  var sibling = psiElement.prevSibling
  while (sibling != null) {
    if (matching(sibling)) {
      return sibling
    }
    sibling = sibling.prevSibling
  }

  return null
}

fun hasElementType(node: ASTNode, set: TokenSet) = set.contains(node.elementType)

fun hasElementType(node: ASTNode, vararg types: IElementType) = hasElementType(node, TokenSet.create(*types))

fun SkinClassSpecification.getRealClassNamesAsString() =
        (containingFile as? SkinFile)?.let { file ->
          file.project.getSkinTag2ClassMap()?.getClassNames(classNameAsString)
        } ?: listOf(classNameAsString.removeDollarFromClassName())

