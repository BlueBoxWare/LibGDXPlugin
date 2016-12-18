package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiFile
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
class SkinElementGenerator(val project: Project) {

  fun createStringLiteral(unescapedContent: String): SkinStringLiteral = createValue('"' + StringUtil.escapeStringCharacters(unescapedContent) + "'")

  fun createDummyFile(content: String): PsiFile {
    val fileFactory = PsiFileFactory.getInstance(project)
    return fileFactory.createFileFromText("dummy." + LibGDXSkinFileType.INSTANCE.defaultExtension, LibGDXSkinFileType.INSTANCE, content)
  }

  fun <T: SkinValue> createValue(content: String): T {
    val file = createDummyFile("{\"foo\": " + content + "}")
    return (file.firstChild as? SkinObject)?.propertyList?.get(0)?.value as? T ?: throw AssertionError()
  }

}