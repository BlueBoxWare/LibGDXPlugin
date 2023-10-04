package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement


/*
 * Copyright 2022 Blue Box Ware
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
internal class Atlas2FindUsagesProvider : FindUsagesProvider {

    override fun getType(element: PsiElement) = when (element) {
        is Atlas2Region -> "atlas region"
        else -> ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean) = (element as? Atlas2Region)?.name ?: ""

    override fun getDescriptiveName(element: PsiElement) = (element as? Atlas2Region)?.name ?: ""

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun canFindUsagesFor(psiElement: PsiElement) = psiElement is Atlas2Region

    override fun getWordsScanner(): WordsScanner? = null

}
