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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.PsiTreeSubtreerefImpl
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.findParentOfType
import com.intellij.util.IncorrectOperationException

abstract class TreeSubtreerefMixin(node: ASTNode) : PsiTreeSubtreeref, TreeElementImpl(node) {

    override fun getName(): @NlsSafe String = text.trimStart('$')

    override fun resolve(): PsiElement? {
        (containingFile as? TreeFile)?.let { treeFile ->
            for (subtree in treeFile.getSubtrees(this)) {
                if (subtree.name == name) {
                    return subtree
                }
            }
        }

        return null
    }

    override fun handleElementRename(newElementName: String): PsiTreeSubtreerefImpl {
        TreeElementFactory.createSubtreeref(project, newElementName)?.let {
            replace(it)
            return it
        }
        throw IncorrectOperationException()
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        if (element is PsiTreeSubtree) {
            element.name?.let { name ->
                TreeElementFactory.createTaskname(project, name)?.let {
                    return replace(it)
                }
            }
        }
        throw IncorrectOperationException()
    }

    override fun isReferenceTo(element: PsiElement): Boolean = element.findParentOfType<TreeSubtree>(false)?.let {
        resolve() == it
    } ?: false

    override fun getCanonicalText(): @NlsSafe String = name

    override fun getReference(): PsiReference? = this

    override fun getElement(): PsiElement = this

    override fun getRangeInElement(): TextRange = TextRange(0, textLength)

    override fun isSoft(): Boolean = false

}
