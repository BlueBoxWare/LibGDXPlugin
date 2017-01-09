package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.intellij.openapi.project.Project
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
    "string_literal: {
        resourceName: {
            propertyName: propertyValue
        }
    }
}
  """

  fun createPropertyName(project: Project, name: String): SkinPropertyName? {
    val content = dummyContent.replace("propertyName", name)
    return createElement(project, content, SkinPropertyName::class.java)
  }

  fun createResourceName(project: Project, name: String): SkinResourceName? {
    val content = dummyContent.replace("resourceName", name)
    return createElement(project, content, SkinResourceName::class.java)
  }

  fun createStringLiteral(project : Project, value : String): SkinStringLiteral? {
    val content = dummyContent.replace("string_literal", value)
    return createElement(project, content, SkinStringLiteral::class.java)
  }

  private fun <T: SkinElement> createElement(project: Project, content: String, type: Class<T>): T? {
    val file = createFile(project, content)
    return PsiTreeUtil.findChildOfType(file, type)
  }

  private fun createFile(project: Project, content: String) = PsiFileFactory.getInstance(project).createFileFromText("dummy.json", LibGDXSkinFileType.INSTANCE, content) as SkinFile

}