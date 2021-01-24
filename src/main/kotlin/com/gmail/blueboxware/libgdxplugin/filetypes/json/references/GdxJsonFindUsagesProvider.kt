package com.gmail.blueboxware.libgdxplugin.filetypes.json.references

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement


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
class GdxJsonFindUsagesProvider: FindUsagesProvider {

  override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
          psiElement is PsiNamedElement

  override fun getHelpId(psiElement: PsiElement): String? = null

  override fun getType(element: PsiElement): String =
          if (element is GdxJsonProperty) {
            "property"
          } else {
            ""
          }

  override fun getDescriptiveName(element: PsiElement): String =
          (element as? PsiNamedElement)?.name ?: ""

  override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
          getDescriptiveName(element)

}