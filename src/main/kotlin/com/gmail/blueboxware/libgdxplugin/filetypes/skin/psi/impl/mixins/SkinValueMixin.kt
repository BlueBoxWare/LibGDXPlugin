package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiArrayType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTreeUtil

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
abstract class SkinValueMixin(node: ASTNode): SkinValue, SkinElementImpl(node) {

  override fun getPropertyValue() = PsiTreeUtil.findFirstParent(this, { it is SkinPropertyValue }) as? SkinPropertyValue

  override fun getProperty() = propertyValue?.property

  override fun getActualType(): PsiType? =
          property?.resolveToType()?.let { type ->
            var elementType = type
            var arrayDepth = arrayDepth()

            while (arrayDepth > 0 && elementType is PsiArrayType) {
              elementType = elementType.componentType
              arrayDepth--
            }

            if (arrayDepth == 0) {
              return elementType
            } else {
              return null
            }
          }

  private fun arrayDepth(): Int {
    var depth = 0
    var element: PsiElement = this

    while (element.parent != null && element.parent !is SkinResource) {
      element = element.parent
      if (element is SkinArray) {
        depth++
      }
    }

    return depth
  }

  override fun getActualTypeString(): String? = actualType?.canonicalText


}