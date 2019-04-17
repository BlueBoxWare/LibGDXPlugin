package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonFile
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace
import kotlin.text.Typography.quote


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonElementFactory(private val project: Project) {

  fun createQuotedValueString(value: String): GdxJsonValue? {
    val content = "$quote${StringUtil.escapeStringCharacters(value)}$quote"
    return createElement(content)
  }

  fun createQuotedPropertyName(name: String): GdxJsonPropertyName? {
    val content = "{ $quote${StringUtil.escapeStringCharacters(name)}$quote: bar }"
    return createElement(content)
  }

  fun createNewline(): PsiWhiteSpace? = createElement("f\n", '\n')

  private inline fun <reified T: PsiElement> createElement(content: String, character: Char): T? =
          createElement(content, content.indexOf(character))

  private inline fun <reified T: PsiElement> createElement(content: String, position: Int): T? =
          createFile(content)?.findElementAt(position) as? T

  private inline fun <reified T: PsiElement> createElement(content: String): T? =
          createFile(content)?.childOfType()

  private fun createFile(content: String) =
          PsiFileFactory.getInstance(project).createFileFromText("dummy.lson", LibGDXJsonFileType.INSTANCE, content) as? GdxJsonFile

}