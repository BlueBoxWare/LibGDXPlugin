package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement

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
internal class SkinFindUsagesProvider : FindUsagesProvider {

    override fun getType(element: PsiElement) = when (element) {
        is SkinResource -> "resource"
        else -> ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean) = (element as? PsiNamedElement)?.name ?: ""

    override fun getDescriptiveName(element: PsiElement) = when (element) {
        is SkinResource -> element.name + element.classSpecification?.classNameAsString?.let { "($it)" }
        else -> ""
    }

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun canFindUsagesFor(psiElement: PsiElement) = psiElement is SkinResource

    override fun getWordsScanner(): WordsScanner? = null
}
