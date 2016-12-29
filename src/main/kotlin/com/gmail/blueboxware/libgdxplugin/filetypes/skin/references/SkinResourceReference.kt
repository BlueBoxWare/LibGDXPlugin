package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.PsiTreeUtil
import icons.Icons

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
class SkinResourceReference(element: SkinResource, range: TextRange) : PsiReferenceBase<SkinResource>(element, range) {

  override fun resolve(): SkinResource {
    return element
  }

  override fun getVariants(): Array<out Any> {
    val variants = mutableListOf<LookupElement>()

    for (resource in PsiTreeUtil.findChildrenOfType(element.containingFile, SkinResource::class.java)) {
      variants.add(LookupElementBuilder.create(resource).withIcon(Icons.LIBGDX_ICON).withTypeText(resource.name))
    }

    return variants.toTypedArray()
  }

  override fun isReferenceTo(element: PsiElement?): Boolean {
    return super.isReferenceTo(element)
  }

  override fun getRangeInElement(): TextRange? {
    element.nameIdentifier?.let { nameIdentifier ->
      return ElementManipulators.getValueTextRange(nameIdentifier)
    }

    return null
  }
}
