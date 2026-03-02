/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.IDENTIFIERS
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.LITERALS
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLexerAdapter
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeImport
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.parentOfType

internal class TreeFindUsagesProvider : FindUsagesProvider {
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
        psiElement is TreeAttributeName && psiElement.parentOfType<TreeImport>(false) != null

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String = "task name"

    override fun getDescriptiveName(element: PsiElement): String = if (element is PsiTreeAttributeName) {
        element.text
    } else ""

    override fun getNodeText(
        element: PsiElement, useFullName: Boolean
    ): String = element.text

    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        TreeLexerAdapter(), IDENTIFIERS, TokenSet.create(TreeElementTypes.COMMENT), LITERALS
    )

}
