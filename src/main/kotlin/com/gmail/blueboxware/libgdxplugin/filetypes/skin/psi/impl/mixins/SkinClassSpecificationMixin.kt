package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement

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
abstract class SkinClassSpecificationMixin(node: ASTNode) : SkinClassSpecification, SkinElementImpl(node) {

  override fun getNameIdentifier(): SkinClassName?  = className

  override fun getClassNameAsString(): String = className.value

  override fun resolveClass(): PsiClass? = className.resolve()

  override fun setName(name: String): PsiElement? {
    SkinElementFactory.createClassName(project, name)?.let { newClassName ->
      newClassName.replace(newClassName)
      return newClassName
    }

    return null
  }

  override fun getResourcesAsList() = resources.resourceList

  override fun getName() = nameIdentifier?.value

  override fun getPresentation() = object: ItemPresentation {
    override fun getLocationString() = null

    override fun getIcon(unused: Boolean) = AllIcons.Nodes.Class

    override fun getPresentableText() = name?.let { StringUtil.getShortName(it) }
  }
}