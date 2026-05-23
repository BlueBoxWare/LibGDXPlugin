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
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class TreeStructureViewModel(editor: Editor?, psiFile: PsiFile) :
    StructureViewModelBase(psiFile, editor, TreeStructureViewElement(psiFile)), StructureViewModel.ElementInfoProvider,
    StructureViewModel.ExpandInfoProvider {

    override fun getSorters(): Array<out Sorter?> = arrayOf(Sorter.ALPHA_SORTER)

    override fun getFilters(): Array<out Filter?> = arrayOf(ImportFilter())

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean = false

    override fun getSuitableClasses(): Array<out Class<*>?> = arrayOf(PsiTreeLine::class.java)

    override fun isAutoExpand(element: StructureViewTreeElement) =
        ApplicationManager.getApplication().isUnitTestMode

    override fun isSmartExpand(): Boolean = false

    @Suppress("UnstableApiUsage")
    override fun getMinimumAutoExpandDepth(): Int = 10


}
