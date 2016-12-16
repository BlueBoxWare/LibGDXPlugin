package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSkinFiles
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPlainText

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
class SkinHighlighter : Annotator {

  companion object {

    private val identifier = """\p{javaJavaIdentifierStart}\p{javaJavaIdentifierPart}*"""
    private val className = """[\p{javaJavaIdentifierStart}&&[\p{Lu}]]\p{javaJavaIdentifierPart}*"""
    private val fqClassName = """$identifier(?:\.$identifier)*(?:\.$className)"""

    private val delimiters = """\s,:\{\}\[\]"""

    private val singleQuotedString = """'([^\\'\r\n]|\\[^\r\n])*'"""
    private val doubleQuotedString = """"([^\\"\r\n]|\\[^\r\n])*""""
    private val unquotedString = """\p{javaJavaIdentifierStart}[^$delimiters]*"""

    private val number = """(-\s*)?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]*)?"""

    val LINE_COMMENT = Regex("""//[^\r\n]*""")
    val BLOCK_COMMENT = Regex("""/\*(?:[^*]|\*+[^*/])*(?:\*+/)""")

    val STRING = Regex("""$singleQuotedString|$doubleQuotedString|$unquotedString""")

    val FQ_CLASS_NAME = Regex(fqClassName)

    val NUMBER = Regex("""[$delimiters]($number)[$delimiters]""")

    val SKIN_SIGNATURE = Regex("""com\.badlogic\.gdx\.$fqClassName\s*["']?\s*:\s*\{""")

  }

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if (element !is PsiPlainText) return

    element.containingFile?.virtualFile?.let { virtualFile ->
      if (element.project.getComponent(LibGDXProjectSkinFiles::class.java)?.contains(virtualFile) != true
        || !EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(virtualFile)
      ) {
        return
      }
    }

    val skipList = mutableMapOf<Int, Int>()

    fun annotate(start: Int, end: Int, attribute: String, defaultAttribute: TextAttributesKey) {
      if (skipList.any { it.key <= start && start < it.value }) return

      val annotation = holder.createInfoAnnotation(TextRange(start, end), null)
      annotation.textAttributes = TextAttributesKey.createTextAttributesKey(attribute, defaultAttribute)
      skipList.put(start, end)
    }

    // Line comments
    val lineComments = LINE_COMMENT.findAll(element.text)

    for (match in lineComments) {
      annotate(match.range.start, match.range.endInclusive + 1, "JSON.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    }

    // Block comments
    val blockComments = BLOCK_COMMENT.findAll(element.text)

    for (match in blockComments) {
      annotate(match.range.start, match.range.endInclusive + 1, "JSON.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
    }

    // Fully qualified class names
    val classNames = FQ_CLASS_NAME.findAll(element.text)

    for (match in classNames) {
      annotate(match.range.start, match.range.endInclusive + 1, "JSON.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    }

    // Strings
    val strings = STRING.findAll(element.text)

    for (match in strings) {
      if (!match.value.matches(FQ_CLASS_NAME)) {
        annotate(match.range.start, match.range.endInclusive + 1, "JSON.STRING", DefaultLanguageHighlighterColors.STRING)
      }
    }

    // Numbers
    val numbers = NUMBER.findAll(element.text)

    for (match in numbers) {
      match.groups.firstOrNull()?.let { group ->
        annotate(group.range.start, group.range.endInclusive + 1, "JSON.NUMBER", DefaultLanguageHighlighterColors.NUMBER)
      }
    }

    // Single characters
    var i = -1
    while (true) {

      i++

      while (skipList[i] != null) {
        i = skipList[i] ?: break
      }

      if (i >= element.text.length) break

      val textAttribute = when (element.text[i]) {
        '{', '}' -> TextAttributesKey.createTextAttributesKey("JSON.BRACES", DefaultLanguageHighlighterColors.BRACES)
        '[', ']' -> TextAttributesKey.createTextAttributesKey("JSON.BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        ',' -> TextAttributesKey.createTextAttributesKey("JSON.COMMA", DefaultLanguageHighlighterColors.COMMA)
        ':' -> TextAttributesKey.createTextAttributesKey("JSON.COLON", DefaultLanguageHighlighterColors.SEMICOLON)
        else -> null
      }

      textAttribute?.let { textAttribute ->
        val annotiation = holder.createInfoAnnotation(TextRange(i, i + 1), null)
        annotiation.textAttributes = textAttribute
      }

    }

  }

}
