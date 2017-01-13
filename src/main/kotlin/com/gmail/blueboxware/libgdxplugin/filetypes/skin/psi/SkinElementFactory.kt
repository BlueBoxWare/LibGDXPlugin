package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
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

  val dummyContent = """
{
    className: {
        resourceName: {
            propertyName: propertyValue
        }
    }
}
  """

  fun createPropertyName(project: Project, name: String, quotationChar: Char?): SkinPropertyName? {
    return createElement(project, "propertyName", name, SkinPropertyName::class.java, quotationChar)
  }

  fun createResourceName(project: Project, name: String, quotationChar: Char?): SkinResourceName? {
    return createElement(project, "resourceName", name, SkinResourceName::class.java, quotationChar)
  }

  fun createStringLiteral(project : Project, value : String, quotationChar: Char?): SkinStringLiteral? {
    val propertyValue = createElement(project, "propertyValue", value, SkinPropertyValue::class.java, quotationChar)
    return propertyValue?.value as? SkinStringLiteral
  }

  private fun <T: SkinElement> createElement(project: Project, replace: String, with: String, type: Class<T>, quotationChar: Char?): T? {
    val quote = quotationChar?.toString() ?: ""
    val content = dummyContent.replace(replace, quote + StringUtil.escapeStringCharacters(with) + quote)
    val file = createFile(project, content)
    return PsiTreeUtil.findChildOfType(file, type)
  }

  private fun createFile(project: Project, content: String) = PsiFileFactory.getInstance(project).createFileFromText("dummy.json", LibGDXSkinFileType.INSTANCE, content) as SkinFile

}