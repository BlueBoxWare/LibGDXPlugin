package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.structureView

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile

/*
 * Copyright 2017 Blue Box Ware
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
class BitmapFontStructureViewModel(psiFile: PsiFile):
        StructureViewModelBase(psiFile, BitmapFontStructureViewElement(psiFile)),
        StructureViewModel.ElementInfoProvider {

  override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

  override fun isAlwaysLeaf(element: StructureViewTreeElement?) = false

  override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)

}