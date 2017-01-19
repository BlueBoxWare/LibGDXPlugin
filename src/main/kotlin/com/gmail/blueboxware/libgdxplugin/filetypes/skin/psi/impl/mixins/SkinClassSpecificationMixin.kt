package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField

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

  override fun getNameIdentifier(): SkinStringLiteral = className.stringLiteral

  override fun getClassNameAsString(): String = className.value

  override fun resolveClass(): PsiClass? = className.resolve() as? PsiClass

  override fun resolveProperty(property: SkinProperty): PsiField? = resolveClass()?.findFieldByName(property.name, true)

  override fun setName(name: String): PsiElement? {
    SkinElementFactory.createStringLiteral(project, name, nameIdentifier.quotationChar)?.let { newClassName ->
      newClassName.replace(newClassName)
      return newClassName
    }

    return null
  }

  override fun getResourcesAsList() = resources?.resourceList ?: listOf()

  override fun getResourceNames(): List<String> = resourcesAsList.map { it.name }

  override fun getResource(name: String) = resources?.resourceList?.firstOrNull { it.name == name }

  override fun getName() = nameIdentifier.value

  override fun getPresentation() = object: ItemPresentation {
    override fun getLocationString() = null

    override fun getIcon(unused: Boolean) = AllIcons.Nodes.Class

    override fun getPresentableText() = name.let { StringUtil.getShortName(it) }
  }

  companion object {
    fun removeDollarFromClassName(name: String) = name.split(".", "$").joinToString(".")

    fun putDollarInInnerClassName(clazz: PsiClass) = clazz.containingClass?.let { it.qualifiedName + "$" + clazz.name } ?: clazz.qualifiedName
  }

}