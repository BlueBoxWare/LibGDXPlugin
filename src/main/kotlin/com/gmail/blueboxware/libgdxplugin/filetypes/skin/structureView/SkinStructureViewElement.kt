package com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.intellij.ide.structureView.StructureViewTreeElement.EMPTY_ARRAY

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
class SkinStructureViewElement(val element: com.intellij.psi.PsiElement): com.intellij.ide.structureView.StructureViewTreeElement, com.intellij.ide.util.treeView.smartTree.SortableTreeElement {

  override fun getPresentation() = (element as? com.intellij.navigation.NavigationItem)?.presentation ?: com.gmail.blueboxware.libgdxplugin.utils.DummyItemPresentation()

  override fun canNavigate() = element is com.intellij.navigation.NavigationItem && element.canNavigate()

  override fun canNavigateToSource() = element is com.intellij.navigation.NavigationItem && element.canNavigateToSource()

  override fun navigate(requestFocus: Boolean) = (element as? com.intellij.navigation.NavigationItem)?.navigate(requestFocus) ?: Unit

  override fun getValue() = element

  override fun getAlphaSortKey() = (element as? com.intellij.psi.PsiNamedElement)?.name ?: ""

  //
  // Adapted from https://github.com/JetBrains/intellij-community/blob/ab08c979a5826bf293ae03cd67463941b0066eb8/json/src/com/intellij/json/structureView/JsonStructureViewElement.java
  //
  override fun getChildren(): Array<out com.intellij.ide.util.treeView.smartTree.TreeElement> {
    var value: Any? = null

    if (element is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile) {
      return com.intellij.util.containers.ContainerUtil.map2Array(
              element.getClassSpecifications(),
              com.intellij.ide.util.treeView.smartTree.TreeElement::class.java,
              com.intellij.util.Function(::SkinStructureViewElement)
      )
    } else if (element is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty) {
      value = element.value
    } else if (element is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement && com.intellij.psi.util.PsiTreeUtil.instanceOf(element, com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject::class.java, com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray::class.java)) {
      value = element
    } else if (element is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification) {
      value = element.resourcesAsList
    } else if (element is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource) {
      value = element.`object`
    }

    if (value is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject) {
      return com.intellij.util.containers.ContainerUtil.map2Array(
              value.propertyList,
              com.intellij.ide.util.treeView.smartTree.TreeElement::class.java,
              com.intellij.util.Function(::SkinStructureViewElement)
      )
    } else if (value is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray) {
      val childObjects: List<com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView.SkinStructureViewElement?> = com.intellij.util.containers.ContainerUtil.mapNotNull(value.valueList,  { value1 ->
        if (value1 is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject && !value1.propertyList.isEmpty()) {
          com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView.SkinStructureViewElement(value1)
        } else if (value1 is com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray && com.intellij.psi.util.PsiTreeUtil.findChildOfType(value1, com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty::class.java) != null) {
          com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView.SkinStructureViewElement(value1)
        } else {
          null
        }
      })

      return com.intellij.util.ArrayUtil.toObjectArray(childObjects, com.intellij.ide.util.treeView.smartTree.TreeElement::class.java)
    } else if (value is List<*>) {
      return value.mapNotNull { (it as? SkinElement)?.let(::SkinStructureViewElement) }.toTypedArray()
    }

    return EMPTY_ARRAY
  }
}