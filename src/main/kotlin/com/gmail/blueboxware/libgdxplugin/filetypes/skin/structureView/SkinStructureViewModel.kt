package com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiFile

/*
 * Copyright 2016 Blue Box Ware
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
class SkinStructureViewModel(file: PsiFile) :
    StructureViewModelBase(
        file,
        SkinStructureViewElement(file)
    ),
    StructureViewModel.ElementInfoProvider,
    StructureViewModel.ExpandInfoProvider {

    init {
        withSuitableClasses(
            SkinFile::class.java,
            SkinClassSpecification::class.java,
            SkinResource::class.java,
            SkinObject::class.java,
            SkinArray::class.java,
            SkinProperty::class.java
        )
    }

    override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?) = element is SkinFile

    override fun isSmartExpand() = false

    override fun isAutoExpand(element: StructureViewTreeElement) =
        element is SkinFile || ApplicationManager.getApplication().isUnitTestMode
}
