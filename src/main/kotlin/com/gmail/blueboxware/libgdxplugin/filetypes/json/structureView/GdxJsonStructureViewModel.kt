package com.gmail.blueboxware.libgdxplugin.filetypes.json.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonFile
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.application.ApplicationManager


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonStructureViewModel(file: GdxJsonFile):
        StructureViewModelBase(file, GdxJsonStructureViewElement(file)),
        StructureViewModel.ElementInfoProvider,
        StructureViewModel.ExpandInfoProvider {

  init {
    withSuitableClasses(
            GdxJsonFile::class.java,
            GdxJsonProperty::class.java,
            GdxJsonJobject::class.java,
            GdxJsonArray::class.java
    )
    withSorters(Sorter.ALPHA_SORTER)
  }

  override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean = false

  override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean = false

  override fun isAutoExpand(element: StructureViewTreeElement): Boolean = element is GdxJsonFile || ApplicationManager.getApplication().isUnitTestMode

  override fun isSmartExpand(): Boolean = true
}