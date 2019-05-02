package com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.parentString
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement


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
class GdxJsonAddQuotesIntention: GdxJsonBaseIntention() {

  override fun getFamilyName(): String = "Wrap with double quotes"

  override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
          element.parentString()?.isQuoted == false

  override fun invoke(project: Project, editor: Editor?, element: PsiElement) {

    (element.parent as? GdxJsonPropertyName)?.let { propertyName ->
      val oldString = propertyName.value
      val newString = GdxJsonElementFactory(project).createQuotedPropertyName(oldString) ?: return
      propertyName.replace(newString)
      return
    }

    element.parentString()?.let { string ->
      val oldString = string.value
      val newString = GdxJsonElementFactory(project).createQuotedValueString(oldString) ?: return
      string.replace(newString)
    }

  }
}