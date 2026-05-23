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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeFile
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement

open class TreeStructureViewElement(val element: NavigatablePsiElement) : StructureViewTreeElement {
    override fun getValue(): Any? = element

    override fun getPresentation(): ItemPresentation = element.presentation ?: PresentationData()

    override fun getChildren(): Array<out TreeElement?> {
        if (element !is TreeFile && element !is PsiTreeLine) return emptyArray()
        val file = element.containingFile as? TreeFile ?: return emptyArray()
        val tree = file.getTree(element) ?: return emptyArray()
        val result = mutableListOf<TreeElement>()
        for (child in tree.children) {
            child.line?.let { line ->
                if (!line.isEmpty()) {
                    result.add(TreeStructureViewElement(line))
                }
            }
        }
        return result.toTypedArray()
    }

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

}
