package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.util.ArrayUtil

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
class SkinPropertyNameReference(val property: SkinProperty) : PsiReference {

  override fun getElement() = property

  override fun getRangeInElement(): TextRange {
    val nameElement = property.text
    return TextRange(1, 1)
  }

  override fun bindToElement(element: PsiElement) = null

  override fun isReferenceTo(element: PsiElement?): Boolean {
    if (element !is SkinProperty) {
      return false
    }

    val selfResolve = resolve()

    return element.name == canonicalText && selfResolve != element
  }

  override fun resolve() = property

  override fun getVariants() = ArrayUtil.EMPTY_OBJECT_ARRAY

  override fun getCanonicalText() = property.name

  override fun handleElementRename(newElementName: String) = property.setName(newElementName)

  override fun isSoft() = true
}