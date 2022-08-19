package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFile
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.DummyItemPresentation
import com.intellij.icons.AllIcons
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import javax.swing.Icon

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
open class BitmapFontStructureViewElement(val element: PsiElement?) : StructureViewTreeElement, SortableTreeElement {

    override fun getPresentation() = (element as? NavigationItem)?.presentation ?: DummyItemPresentation()

    override fun getChildren(): Array<BitmapFontStructureViewElement> {

        val result = mutableListOf<BitmapFontStructureViewElement>()

        if (element is BitmapFontFile) {

            element.getInfoElement()?.let {
                result.add(BitmapFontStructureViewElement(it))
            }
            element.getCommonElement()?.let {
                result.add(BitmapFontStructureViewElement(it))
            }
            element.getPages().firstOrNull()?.let { firstPage ->
                result.add(
                    MyStructureViewElement(
                        element = firstPage,
                        presentableText = "Pages",
                        sortKey = "3",
                        icon = AllIcons.Nodes.Folder,
                        myChildren = element.getPages().map(::BitmapFontStructureViewElement)
                    )
                )
            }
            element.getCharsElement()?.let { charsElement ->
                result.add(
                    MyStructureViewElement(
                        element = charsElement,
                        presentableText = "Characters",
                        sortKey = "4",
                        myChildren = element.getCharacters().map(::BitmapFontStructureViewElement)
                    )
                )
            }
            element.getKerningsElement()?.let { kerningsElement ->
                result.add(
                    MyStructureViewElement(
                        element = kerningsElement,
                        presentableText = "Kernings",
                        sortKey = "5",
                        icon = AllIcons.Nodes.Tag,
                        myChildren = element.getKernings().map(::BitmapFontStructureViewElement)
                    )
                )
            }

        } else if (element is PropertyContainer) {

            result.addAll(element.getPropertyList().map(::BitmapFontStructureViewElement))

        }

        return result.toTypedArray()

    }

    override fun canNavigate() = (element as? NavigationItem)?.canNavigate() == true

    override fun canNavigateToSource() = (element as? NavigationItem)?.canNavigateToSource() == true

    override fun navigate(requestFocus: Boolean) = (element as? NavigationItem)?.navigate(requestFocus) ?: Unit

    override fun getValue() = element

    override fun getAlphaSortKey(): String {

        when (element) {
            is BitmapFontFontChar -> {
                return String.format("%010d", element.character)
            }

            is BitmapFontPageDefinition -> {
                try {
                    element.getValue("id")?.toInt()?.let {
                        return String.format("%03d", it)
                    }
                } catch (e: NumberFormatException) {
                    // Nothing
                }
                return ""
            }

            is BitmapFontKerning -> {
                try {
                    val first = element.getValue("first")?.toInt()
                    val second = element.getValue("second")?.toInt()
                    if (first != null && second != null) {
                        return String.format("%010d%010d", first, second)
                    }
                } catch (e: NumberFormatException) {
                    // Nothing
                }
                return "Z"
            }

            is BitmapFontProperty -> {
                return element.key
            }

            is BitmapFontInfo -> {
                return "1"
            }

            is BitmapFontCommon -> {
                return "2"
            }

            else -> return ""
        }

    }
}

private class MyStructureViewElement(
    element: PsiElement?,
    val presentableText: String,
    val sortKey: String,
    val icon: Icon? = null,
    val myChildren: List<BitmapFontStructureViewElement> = listOf()
) : BitmapFontStructureViewElement(element) {

    override fun getPresentation() = object : ItemPresentation {
        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean) = this@MyStructureViewElement.icon

        override fun getPresentableText() = this@MyStructureViewElement.presentableText
    }

    override fun getChildren() = myChildren.toTypedArray()

    override fun canNavigate() = (element as? NavigationItem)?.canNavigate() == true

    override fun canNavigateToSource() = (element as? NavigationItem)?.canNavigateToSource() == true

    override fun navigate(requestFocus: Boolean) = (element as? NavigationItem)?.navigate(requestFocus) ?: Unit

    override fun getValue() = element

    override fun getAlphaSortKey() = sortKey
}
