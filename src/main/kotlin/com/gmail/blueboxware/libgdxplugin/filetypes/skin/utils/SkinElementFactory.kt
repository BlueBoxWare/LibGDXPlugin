package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.codeInspection.SuppressionUtil.createComment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory

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
class SkinElementFactory(private val project: Project) {

  constructor(element: PsiElement): this(element.project)

  fun createSuppressionComment(inspectionId: String): PsiComment? =
          createComment(project, " @Suppress($inspectionId)", LibGDXSkinLanguage.INSTANCE)

  fun createProperty(name: String, value: String): SkinProperty? {
    val file = PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, """
    {
      class: {
        resource: {
          $name: $value
        }
      }
    }
    """) as SkinFile
    return file.childOfType()
  }

  fun createComma(): PsiElement? {
    val content = """
    {
      class: {
        resource: {
          a: a, b: b
        }
      }
    }
    """
    val file = PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as SkinFile

    return file.findElementAt(content.indexOf(','))
  }

  fun createNewLine(): PsiElement? {
    val content = """
    {

    }
    """
    val file = PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as SkinFile

    return file.findElementAt(content.indexOf('\n'))
  }

  fun createClassSpec(name: String): SkinClassSpecification? =
          createElement("""
            {
              $name: { }
            }
          """.trimIndent())

  fun createObject(): SkinObject? =
          createElement("propertyName: propertyValue", "", false)

  fun createPropertyName(name: String, quote: Boolean): SkinPropertyName? =
          createElement("propertyName", name, quote)

  fun createResourceName(name: String, quote: Boolean): SkinResourceName? =
          createElement("resourceName", name, quote)

  fun createStringLiteral(value: String, quote: Boolean): SkinStringLiteral? =
          createElement<SkinPropertyValue>("propertyValue", value, quote)?.let {
            it.value as? SkinStringLiteral
          }

  private inline fun <reified T: SkinElement> createElement(replace: String, with: String, quote: Boolean): T? {
    val quoteChar = if (quote) "\"" else ""
    val replacement = if (quote) StringUtil.escapeStringCharacters(with) else with
    val content = DUMMY_CONTENT.replace(replace, quoteChar + replacement + quoteChar)
    return createElement(content)
  }

  private inline fun <reified T: SkinElement> createElement(content: String): T? =
          createFile(content).childOfType()

  private fun createFile(content: String) =
          PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as SkinFile

  companion object {


    private val DUMMY_CONTENT = """
      {
          className: {
              resourceName: {
                  propertyName: propertyValue
              }
          }
      }
        """.trimIndent()

  }

}