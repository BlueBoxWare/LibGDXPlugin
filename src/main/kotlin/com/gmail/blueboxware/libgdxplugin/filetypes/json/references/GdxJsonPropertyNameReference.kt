package com.gmail.blueboxware.libgdxplugin.filetypes.json.references

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference


/*
 * Copyright 2021 Blue Box Ware
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
class GdxJsonPropertyNameReference(val property: GdxJsonProperty): PsiReference {

  override fun getElement(): PsiElement = property

  override fun getRangeInElement(): TextRange =
          ElementManipulators.getValueTextRange(property.propertyName)

  override fun resolve(): PsiElement = property

  override fun getCanonicalText(): String = property.propertyName.getValue()

  override fun handleElementRename(newElementName: String): PsiElement =
          property.setName(newElementName)

  override fun bindToElement(element: PsiElement): PsiElement? = null

  override fun isReferenceTo(element: PsiElement): Boolean =
          (element as? GdxJsonProperty)?.propertyName?.getValue() == property.propertyName.getValue()
                  && element != property

  override fun isSoft(): Boolean = true

}