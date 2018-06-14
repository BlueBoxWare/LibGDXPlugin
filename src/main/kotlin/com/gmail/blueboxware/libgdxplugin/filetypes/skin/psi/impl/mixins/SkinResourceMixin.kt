package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.utils.createColorIcon
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import java.awt.Color
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
abstract class SkinResourceMixin(node: ASTNode) : SkinResource, SkinElementImpl(node) {

  override fun getName() = resourceName.stringLiteral.value

  override fun getNameIdentifier() = resourceName

  override fun getObject(): SkinObject? = value as? SkinObject

  override fun getString(): SkinStringLiteral? = value as? SkinStringLiteral

  override fun getClassSpecification(): SkinClassSpecification? = PsiTreeUtil.findFirstParent(this, { it is SkinClassSpecification }) as? SkinClassSpecification

  override fun getUseScope() = GlobalSearchScope.allScope(project)

  override fun findDefinition(): SkinResource? {

    var element: SkinResource? = this

    while (element?.string != null) {
      element = element.string?.reference?.resolve() as? SkinResource
    }

    return element

  }

  override fun asColor(force: Boolean): Color? = (findDefinition()?.value as? SkinObject)?.asColor(force || classSpecification?.getRealClassNamesAsString()?.contains("com.badlogic.gdx.graphics.Color") == true)

  override fun setName(name: String): PsiElement? {
    SkinElementFactory.createResourceName(project, name, nameIdentifier.stringLiteral.isQuoted)?.let { newResourceName ->
      resourceName.replace(newResourceName)
      return newResourceName
    }

    return null
  }

  override fun getPresentation(): ItemPresentation  = object : ItemPresentation {
    override fun getLocationString(): String? = VfsUtil.getRelativeLocation(containingFile.virtualFile, project.baseDir)

    override fun getIcon(unused: Boolean): Icon? {
      val force = (PsiTreeUtil.findFirstParent(this@SkinResourceMixin, { it is SkinClassSpecification }) as? SkinClassSpecification)?.getRealClassNamesAsString()?.contains("com.badlogic.gdx.graphics.Color") ?: false
      return (value as? SkinObject)?.asColor(force)?.let { createColorIcon(it) } ?: AllIcons.FileTypes.Properties
    }

    override fun getPresentableText(): String?  = name
  }
}