package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName
import com.gmail.blueboxware.libgdxplugin.utils.collectTagsFromAnnotations
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult

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
class SkinJavaClassReference(element: SkinClassName) : SkinReference<SkinClassName>(element) {

    override fun multiResolve(incompleteCode: Boolean) =
        element.multiResolve().map(::PsiElementResolveResult).toTypedArray()

    override fun handleElementRename(newElementName: String): PsiElement = element

    override fun bindToElement(target: PsiElement): PsiElement {
        if (target is PsiClass) {
            if (element.project.collectTagsFromAnnotations().none { it.first == element.value.plainName }) {
                element.stringLiteral.value = DollarClassName(target).dollarName
            }
        } else {
            super.bindToElement(target)
        }

        return element
    }

}
