package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinPropertyNameReference
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.util.ArrayUtil
import com.intellij.util.PlatformIcons
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.UIUtil
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/306d705e1829bd3c74afc2489bfb7ed59d686b84/json/src/com/intellij/json/psi/impl/JsonPropertyMixin.java
 *
 */
abstract class SkinPropertyMixin(node: ASTNode) : SkinProperty, SkinElementImpl(node) {

  override fun getName(): String = propertyName.stringLiteral.value

  override fun getValue(): SkinValue = propertyValue.value

  override fun getNameIdentifier(): PsiElement = propertyName

  override fun setName(@NonNls name: String): PsiElement? {
    SkinElementFactory.createPropertyName(project, name)?.let { newPropertyName ->
      propertyName.replace(newPropertyName)
      return newPropertyName
    }

    return null
  }

  override fun getReferences(): Array<out PsiReference> {
    val fromProviders = ReferenceProvidersRegistry.getReferencesFromProviders(this)
    return ArrayUtil.prepend(SkinPropertyNameReference(this), fromProviders)
  }

  override fun getReference(): PsiReference? = SkinPropertyNameReference(this)

  override fun getPresentation() = object : ItemPresentation {
    override fun getLocationString() = null

    override fun getIcon(unused: Boolean): Icon {

      (value as? SkinObject)?.asColor()?.let { color ->
        return ColorIcon(if (UIUtil.isRetina()) 26 else 13, color, true)
      }

      if (value is SkinArray) {
        return AllIcons.Json.Property_brackets
      }
      if (value is SkinObject) {
        return AllIcons.Json.Property_braces
      }
      return PlatformIcons.PROPERTY_ICON
    }

    override fun getPresentableText() = name
  }

}
