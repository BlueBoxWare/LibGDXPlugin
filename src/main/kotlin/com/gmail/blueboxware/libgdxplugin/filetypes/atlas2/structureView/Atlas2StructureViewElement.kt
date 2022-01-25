package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2FieldOwner
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Page
import com.gmail.blueboxware.libgdxplugin.utils.DummyItemPresentation
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement

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
class Atlas2StructureViewElement(val element: PsiElement) : StructureViewTreeElement, SortableTreeElement {

    override fun getPresentation() = (element as? NavigationItem)?.presentation ?: DummyItemPresentation()

    override fun getChildren(): Array<out TreeElement> {

        val items = ((element as? Atlas2FieldOwner)?.getFieldList() ?: emptyList()) + when (element) {
            is Atlas2File -> element.getPages()
            is Atlas2Page -> element.regionList
            else -> listOf()
        }

        return items.map(::Atlas2StructureViewElement).toTypedArray()

    }

    override fun canNavigate() = (element as? NavigationItem)?.canNavigate() == true

    override fun canNavigateToSource() = (element as? NavigationItem)?.canNavigateToSource() == true

    override fun navigate(requestFocus: Boolean) = (element as? NavigationItem)?.navigate(requestFocus) ?: Unit

    override fun getValue() = element

    override fun getAlphaSortKey() = (element as? NavigationItem)?.presentation?.presentableText ?: ""

}
