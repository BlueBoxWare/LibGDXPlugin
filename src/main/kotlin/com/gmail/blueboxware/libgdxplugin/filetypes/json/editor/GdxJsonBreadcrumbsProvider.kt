package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElement
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.getArrayIndexOfItem
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.isArrayElement
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider


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
class GdxJsonBreadcrumbsProvider: BreadcrumbsProvider {

  override fun getLanguages() = LANGUAGES

  override fun acceptElement(element: PsiElement): Boolean =
          element is GdxJsonProperty || (element as? GdxJsonElement)?.isArrayElement() == true

  override fun getElementInfo(element: PsiElement): String {
    if (element is GdxJsonProperty) {
      return element.propertyName.value
    } else if (element is GdxJsonValue && element.isArrayElement()) {
      element.getArrayIndexOfItem()?.let {
        return it.toString()
      }
    }

    return ""
  }

  companion object {
    val LANGUAGES = arrayOf(LibGDXJsonLanuage.INSTANCE)
  }

}