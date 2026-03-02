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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.DEFAULT_AI_IMPORTS
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.getClassReferences
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.getDefaultAiTaskClasses
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.PsiTreeTasknameImpl
import com.gmail.blueboxware.libgdxplugin.utils.asPsiClass
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType
import com.intellij.util.IncorrectOperationException
import org.jetbrains.kotlin.psi.KtClassOrObject

abstract class TreeTaskNameMixin(node: ASTNode) : PsiTreeTaskname, TreeElementImpl(node) {

    override fun getName(): @NlsSafe String = text.trimEnd('?', ' ', '\t')

    override fun isInImport(): Boolean = parentOfType<TreeImport>(false) != null

    override fun getReferences(): Array<out PsiReference?> {
        if (!isInImport()) {
            getDefiningImport()?.let {
                return arrayOf(this)
            }
            val ucName = name.replaceFirstChar { it.uppercase() }
            if (name != ucName && ucName in DEFAULT_AI_IMPORTS) {
                return arrayOf(this)
            }

        }

        return getClassReferences(name, 0)
    }

    override fun getReference(): PsiReference? = references.firstOrNull()

    override fun resolve(): PsiElement? = getDefiningImport() ?: resolveDefaultImportToClass()?.navigationElement

    override fun resolveToClass(): PsiClass? {
        references.filterNotNull().forEach { ref ->
            val res = ref.resolve()?.navigationElement
            if (res is PsiClass || res is KtClassOrObject) {
                return res.asPsiClass()
            } else if (res is PsiTreeAttribute) {
                res.value?.vstring?.references?.forEach {
                    val target = it.resolve()?.navigationElement
                    if (target is PsiClass || target is KtClassOrObject) {
                        return target.asPsiClass()
                    }
                }
            }
        }
        return null
    }

    override fun getElement(): PsiElement = this

    override fun getRangeInElement(): TextRange = TextRange(0, textLength)

    override fun getCanonicalText(): @NlsSafe String = name

    override fun handleElementRename(newElementName: String): PsiTreeTasknameImpl? {
        TreeElementFactory.createTaskname(project, newElementName)?.let {
            replace(it)
            return it
        }
        throw IncorrectOperationException()
    }

    override fun handleElementRename(newName: String, range: TextRange): PsiTreeTasknameImpl? {
        val newTaskName = text.replaceRange(range.startOffset, range.endOffset, newName)
        return handleElementRename(newTaskName)
    }

    override fun bindToElement(element: PsiElement): PsiElement? {
        if (element is PsiClass) {
            element.name?.let { name ->
                TreeElementFactory.createTaskname(project, name)?.let {
                    return replace(it)
                }
            }
        }
        throw IncorrectOperationException()
    }

    override fun isReferenceTo(element: PsiElement): Boolean =
        element is TreeAttributeName && element.text == name && containingFile == element.containingFile

    override fun isSoft(): Boolean = false

    private fun getDefiningImport(): TreeAttribute? = (containingFile as? TreeFile)?.let { file ->
        file.getImports(this)[name]?.let { (_, attribute) ->
            attribute
        }
    }

    private fun resolveDefaultImportToClass(): PsiClass? {
        val ucName = name.replaceFirstChar { it.uppercase() }
        if (ucName != name && ucName in DEFAULT_AI_IMPORTS) {
            return project.getDefaultAiTaskClasses()[ucName]
        }
        return null
    }

}
