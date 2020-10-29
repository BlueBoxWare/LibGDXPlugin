package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.factory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.createColorIcon
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.search.allScope
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
abstract class SkinResourceMixin(node: ASTNode): SkinResource, SkinElementImpl(node) {

  override fun getName() = resourceName.stringLiteral.value

  override fun getNameIdentifier() = resourceName

  override fun getObject(): SkinObject? = value as? SkinObject

  override fun getString(): SkinStringLiteral? = value as? SkinStringLiteral

  override fun getClassSpecification(): SkinClassSpecification? = firstParent()

  override fun getUseScope() = project.allScope()

  override fun findDefinition(): SkinResource? {

    var element: SkinResource? = this

    while (element?.string != null) {
      element = element.string?.reference?.resolve() as? SkinResource
    }

    return element

  }

  override fun asColor(force: Boolean): Color? =
          (findDefinition()?.value as? SkinObject)
                  ?.asColor(
                          force || classSpecification?.getRealClassNamesAsString()?.contains(COLOR_CLASS_NAME) == true
                  )

  override fun setName(name: String): PsiElement? {
    factory()?.createResourceName(name, nameIdentifier.stringLiteral.isQuoted)?.let { newResourceName ->
      resourceName.replace(newResourceName)
      return newResourceName
    }

    return null
  }

  override fun getPresentation(): ItemPresentation = object: ItemPresentation {
    override fun getLocationString(): String? =
            project.guessProjectDir()?.let {
              VfsUtil.getRelativeLocation(containingFile.virtualFile, it)
            }

    override fun getIcon(unused: Boolean): Icon {
      val force = this@SkinResourceMixin
              .firstParent<SkinClassSpecification>()
              ?.getRealClassNamesAsString()
              ?.contains(COLOR_CLASS_NAME)
              ?: false
      return (value as? SkinObject)?.asColor(force)?.let { createColorIcon(it) } ?: AllIcons.FileTypes.Properties
    }

    override fun getPresentableText(): String = name
  }

  override fun toString(): String = "SkinResource($name)"

}