package com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.StructureViewTreeElement.EMPTY_ARRAY
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ArrayUtil
import com.intellij.util.Function
import com.intellij.util.containers.ContainerUtil
import javax.swing.Icon

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
class SkinStructureViewElement(val element: PsiElement): StructureViewTreeElement, SortableTreeElement {

  override fun getPresentation() = object: ItemPresentation {
    override fun getLocationString(): String? {
      if (element is SkinObject) {
        return element.resolveToTypeString()
      }

      return null
    }

    override fun getIcon(unused: Boolean): Icon? = (element as? NavigationItem)?.presentation?.getIcon(true)

    override fun getPresentableText(): String? = (element as? NavigationItem)?.presentation?.presentableText
  }

  override fun canNavigate() = element is NavigationItem && element.canNavigate()

  override fun canNavigateToSource() = element is NavigationItem && element.canNavigateToSource()

  override fun navigate(requestFocus: Boolean) = (element as? NavigationItem)?.navigate(requestFocus) ?: Unit

  override fun getValue() = element

  override fun getAlphaSortKey() = (element as? PsiNamedElement)?.name ?: ""

  //
  // Adapted from https://github.com/JetBrains/intellij-community/blob/ab08c979a5826bf293ae03cd67463941b0066eb8/json/src/com/intellij/json/structureView/JsonStructureViewElement.java
  //
  override fun getChildren(): Array<out TreeElement> {
    var value: Any? = null

    if (element is SkinFile) {
      return ContainerUtil.map2Array(
              element.getClassSpecifications(),
              TreeElement::class.java,
              Function(::SkinStructureViewElement)
      )
    } else if (element is SkinProperty) {
      value = element.value
    } else if (element is SkinElement && PsiTreeUtil.instanceOf(element, SkinObject::class.java, SkinArray::class.java)) {
      value = element
    } else if (element is SkinClassSpecification) {
      value = element.resourcesAsList
    } else if (element is SkinResource) {
      value = element.`object`
    }

    if (value is SkinObject) {
      return ContainerUtil.map2Array(
              value.propertyList,
              TreeElement::class.java,
              Function(::SkinStructureViewElement)
      )
    } else if (value is SkinArray) {
      val childObjects: List<SkinStructureViewElement?> = ContainerUtil.mapNotNull(value.valueList,  { value1 ->
        if (value1 is SkinObject) {
          SkinStructureViewElement(value1)
        } else if (value1 is SkinArray) {
          SkinStructureViewElement(value1)
        } else {
          null
        }
      })

      return ArrayUtil.toObjectArray(childObjects, TreeElement::class.java)
    } else if (value is List<*>) {
      return value.mapNotNull { (it as? SkinElement)?.let(::SkinStructureViewElement) }.toTypedArray()
    }

    return EMPTY_ARRAY
  }
}