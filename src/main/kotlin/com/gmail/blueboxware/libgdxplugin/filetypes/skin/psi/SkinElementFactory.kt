package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.codeInspection.SuppressionUtil.createComment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

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
object SkinElementFactory {

  private const val dummyContent = """
{
    className: {
        resourceName: {
            propertyName: propertyValue
        }
    }
}
  """

  fun createSuppressionComment(project: Project, inspectionId: String): PsiComment? =
          createComment(project, " @Suppress($inspectionId)", LibGDXSkinLanguage.INSTANCE)

  fun createProperty(project: Project, name: String, value: String): SkinProperty? {
    val file = PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, """
    {
      class: {
        resource: {
          $name: $value
        }
      }
    }
    """) as SkinFile
    return PsiTreeUtil.findChildOfType(file, SkinProperty::class.java)
  }

  fun createComma(project: Project): PsiElement? {
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

  fun createNewLine(project: Project): PsiElement? {
    val content = """
    {

    }
    """
    val file = PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as SkinFile

    return file.findElementAt(content.indexOf('\n'))
  }

  fun createObject(project: Project): SkinObject? {
    return createElement(project, "propertyName: propertyValue", "", SkinObject::class.java, false)
  }

  fun createPropertyName(project: Project, name: String, quote: Boolean): SkinPropertyName? {
    return createElement(project, "propertyName", name, SkinPropertyName::class.java, quote)
  }

  fun createResourceName(project: Project, name: String, quote: Boolean): SkinResourceName? {
    return createElement(project, "resourceName", name, SkinResourceName::class.java, quote)
  }

  fun createStringLiteral(project : Project, value : String, quote: Boolean): SkinStringLiteral? {
    val propertyValue = createElement(project, "propertyValue", value, SkinPropertyValue::class.java, quote)
    return propertyValue?.value as? SkinStringLiteral
  }

  private fun <T: SkinElement> createElement(project: Project, replace: String, with: String, type: Class<T>, quote: Boolean): T? {
    val quoteChar = if (quote) "\"" else ""
    val replacement = if (quote) StringUtil.escapeStringCharacters(with) else with
    val content = dummyContent.replace(replace, quoteChar + replacement + quoteChar)
    val file = createFile(project, content)
    return PsiTreeUtil.findChildOfType(file, type)
  }

  private fun createFile(project: Project, content: String) = PsiFileFactory.getInstance(project).createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as SkinFile

}