package com.gmail.blueboxware.libgdxplugin.filetypes.json.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.StructureViewTreeElement.EMPTY_ARRAY
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon


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
class GdxJsonStructureViewElement(val element: GdxJsonElement): StructureViewTreeElement {

  override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

  override fun canNavigate(): Boolean = element.canNavigate()

  override fun getValue(): PsiElement = element

  override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

  override fun getPresentation(): ItemPresentation = element.presentation ?: object: ItemPresentation {
    override fun getLocationString(): String? = null
    override fun getIcon(unused: Boolean): Icon? = null
    override fun getPresentableText(): String? = element.text
  }

  override fun getChildren(): Array<out TreeElement> {

    var value: GdxJsonElement? = null

    if (element is GdxJsonFile) {
      value = element.childOfType<GdxJsonValue>()?.value
    } else if (element is GdxJsonProperty) {
      value = element.value?.value
    } else if (element is GdxJsonJobject || element is GdxJsonArray) {
      value = element
    }

    if (value is GdxJsonJobject) {
      return value.propertyList.map { GdxJsonStructureViewElement(it) }.toTypedArray()
    } else if (value is GdxJsonArray) {
      return value.valueList.mapNotNull { it.value }.mapNotNull {

        if (it is GdxJsonJobject && !it.propertyList.isEmpty()) {
          GdxJsonStructureViewElement(it)
        } else if (it is GdxJsonArray && PsiTreeUtil.findChildOfType(it, GdxJsonProperty::class.java) != null) {
          GdxJsonStructureViewElement(it)
        } else {
          null
        }

      }.toTypedArray()
    }

    return EMPTY_ARRAY

  }

}