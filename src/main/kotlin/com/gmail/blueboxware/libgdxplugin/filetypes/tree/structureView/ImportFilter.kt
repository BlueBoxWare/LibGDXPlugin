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
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins.TreeImportMixin
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import org.jetbrains.annotations.NonNls

// TODO: Test
class ImportFilter : Filter {
    override fun isVisible(treeNode: TreeElement?): Boolean =
        ((treeNode as? TreeStructureViewElement)?.element as? PsiTreeLine)?.statement?.isImport() != true

    override fun isReverted(): Boolean = true

    override fun getPresentation(): ActionPresentation = ActionPresentationData(
        message("tree.structure.show.imports"), null,
        TreeImportMixin.ICON
    )

    override fun getName(): @NonNls String = "SHOW_IMPORTS"
}
