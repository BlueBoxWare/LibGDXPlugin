/*
 * Copyright 2026 Blue Box Ware
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
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttribute
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeSubtree
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeString
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeSubtree
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.findParentOfType
import org.jetbrains.annotations.NonNls

internal class TreeSubtreeFindUsagesProvider : FindUsagesProvider {

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        if (psiElement is TreeSubtree) {
            return true
        } else if (psiElement is TreeString) {
            psiElement.findParentOfType<PsiTreeAttribute>()?.let { attribute ->
                if (attribute.attributeName.name == "name") {
                    return attribute.parent is PsiTreeSubtree
                }
            }
        }

        return false
    }

    override fun getHelpId(psiElement: PsiElement): @NonNls String? = null

    override fun getType(element: PsiElement): String = "subtree"

    override fun getDescriptiveName(element: PsiElement): String = (element as? PsiNamedElement)?.name ?: "subtree"

    override fun getNodeText(
        element: PsiElement, useFullName: Boolean
    ): String = element.text

    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        TreeLexerAdapter(), IDENTIFIERS, TokenSet.create(TreeElementTypes.COMMENT), LITERALS
    )
}
