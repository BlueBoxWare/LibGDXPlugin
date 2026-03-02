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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.getTaskAttributes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeTask
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElementImpl
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import com.intellij.util.IncorrectOperationException
import org.jetbrains.kotlin.psi.KtProperty

abstract class TreeAttributeNameMixin(node: ASTNode) : PsiTreeAttributeName, TreeElementImpl(node) {

    override fun setName(name: @NlsSafe String): PsiElement? {
        throw IncorrectOperationException()
    }

    override fun getName(): String? = text

    override fun getReference(): PsiReference = this

    override fun getElement(): PsiElement = this

    override fun getRangeInElement(): TextRange = TextRange(0, textLength)

    override fun resolve(): PsiField? =
        parentOfType<PsiTreeTask>()?.resolveToClass()?.findFieldByName(name, false)

    override fun getCanonicalText(): @NlsSafe String = name ?: text

    override fun handleElementRename(newElementName: String): PsiElement? {
        TreeElementFactory.createAttributeName(project, newElementName)?.let {
            return replace(it)
        }
        throw IncorrectOperationException()
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        if (element is KtProperty || element is PsiField) {
            element.name?.let { name ->
                TreeElementFactory.createAttributeName(project, name)?.let {
                    return replace(it)
                }
            }
        }
        throw IncorrectOperationException()
    }

    override fun isReferenceTo(element: PsiElement): Boolean = resolve() == element

    // TODO: Other statements
    override fun isSoft(): Boolean = parentOfType<PsiTreeImport>(false) != null

    override fun getVariants(): Array<out Any?> {
        val task = parentOfType<PsiTreeTask>() ?: return emptyArray()
        val usedAttributes = task.getUsedAttributesNames()
        return task.resolveToClass()?.getTaskAttributes()?.filter { it.name !in usedAttributes }?.toTypedArray()
            ?: emptyArray()
    }

}
