package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.lang.ASTNode
import com.intellij.psi.*
import com.intellij.psi.util.PsiTypesUtil

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

  override fun getProperty() = firstParent<SkinProperty>()

  override fun isBoolean(): Boolean = false

  override fun resolveToClass(): PsiClass? {

    (parent as? SkinResource)?.let { resource ->
      return resource.classSpecification?.resolveClass()
    }

    return (resolveToType() as? PsiClassType)?.resolve()

  }

  override fun resolveToTypeString(): String? = resolveToType()?.canonicalText

  override fun resolveToType(): PsiType? {

    (parent as? SkinResource)?.let { resource ->
      resource.classSpecification?.resolveClass()?.let { clazz ->
        return PsiTypesUtil.getClassType(clazz)
      }
      return null
    }

    property?.resolveToType()?.let { type ->
      var elementType = type
      var arrayDepth = arrayDepth()

      while (arrayDepth > 0 && elementType is PsiArrayType) {
        elementType = elementType.componentType
        arrayDepth--
      }

      return if (arrayDepth == 0) {
        elementType
      } else {
        null
      }
    }

    return null

  }

  private fun arrayDepth(): Int {
    var depth = 0
    var element: PsiElement = this

    while (element.parent is SkinArray) {
      element = element.parent
      depth++
    }

    return depth
  }

}