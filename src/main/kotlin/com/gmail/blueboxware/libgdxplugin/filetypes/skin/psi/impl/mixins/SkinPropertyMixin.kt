package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.factory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
import com.intellij.psi.util.PsiTypesUtil
import com.intellij.util.PlatformIcons
import org.jetbrains.annotations.NonNls
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
abstract class SkinPropertyMixin(node: ASTNode) : SkinProperty, SkinElementImpl(node) {

  override fun getName(): String = propertyName.stringLiteral.value

  override fun getValue(): SkinValue? = propertyValue?.value

  override fun getNameIdentifier(): SkinPropertyName = propertyName

  override fun getContainingObject(): SkinObject? = firstParent()

  override fun resolveToField(): PsiField? = containingObject?.resolveToField(this)

  override fun resolveToType(): PsiType? {
    if (name == PROPERTY_NAME_PARENT && project.isLibGDX199()) {
      return containingObject?.resolveToClass()?.let { PsiTypesUtil.getClassType(it) }
    }

    resolveToField()?.let {
      return it.type
    }

    val objectType = containingObject?.resolveToTypeString()

    if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
      if (name == PROPERTY_NAME_FONT_SCALED_SIZE) {
        val clazz = project.findClass("java.lang.Integer") ?: return null
        return PsiTypesUtil.getClassType(clazz)
      } else if (name == PROPERTY_NAME_FONT_MARKUP || name == PROPERTY_NAME_FONT_FLIP) {
        val clazz = project.findClass("java.lang.Boolean") ?: return null
        return PsiTypesUtil.getClassType(clazz)
      }
    }

    return null
  }

  override fun resolveToTypeString(): String? = resolveToType()?.canonicalText

  override fun setName(@NonNls name: String): PsiElement? {
    factory()?.createPropertyName(name, nameIdentifier.stringLiteral.isQuoted)?.let { newPropertyName ->
      propertyName.replace(newPropertyName)
      return newPropertyName
    }

    return null
  }

  override fun getPresentation() = object : ItemPresentation {
    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon {

      val force =
              this@SkinPropertyMixin.firstParent<SkinClassSpecification>()
                      ?.getRealClassNamesAsString()?.contains(Assets.COLOR_CLASS_NAME) ?: false

      (value as? SkinObject)?.asColor(force)?.let { color ->
        return createColorIcon(color)
      }

      if (value is SkinArray) {
        return AllIcons.Json.Property_brackets
      }
      if (value is SkinObject) {
        return AllIcons.Json.Property_braces
      }
      return PlatformIcons.PROPERTY_ICON
    }

    override fun getPresentableText() = (value as? SkinStringLiteral)?.value?.let { "$name: $it" } ?: name
  }

}
