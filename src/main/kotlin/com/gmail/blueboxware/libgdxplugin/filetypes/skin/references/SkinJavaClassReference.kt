package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*

/*
 * Copyright 2017 Blue Box Ware
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
class SkinJavaClassReference(element: SkinClassName) : PsiReferenceBase<SkinClassName>(element, element.textRange), PsiPolyVariantReference {

  override fun resolve(): PsiElement? = multiResolve(false).firstOrNull()?.element

  override fun getVariants(): Array<out Any> = arrayOf()

  override fun multiResolve(incompleteCode: Boolean) = element.multiResolve().map(::PsiElementResolveResult).toTypedArray()

  override fun getRangeInElement(): TextRange? = ElementManipulators.getValueTextRange(element)

}