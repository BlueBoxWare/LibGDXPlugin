package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiTypesUtil
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

  override fun getValue(): SkinValue? = propertyValue?.value

  override fun getNameIdentifier(): SkinPropertyName = propertyName

  override fun getContainingObject(): SkinObject? = PsiTreeUtil.findFirstParent(this, { it is SkinObject }) as? SkinObject

  override fun resolveToField(): PsiField? = containingObject?.resolveToField(this)

  override fun resolveToType(): PsiType? {
    val field = resolveToField()

    if (field != null) {
      return field.type
    }

    val objectType = containingObject?.resolveToTypeString()

    if (objectType == "com.badlogic.gdx.graphics.g2d.BitmapFont") {
      if (name == "scaledSize") {
        val clazz = JavaPsiFacade.getInstance(project).findClass("java.lang.Integer", GlobalSearchScope.allScope(project)) ?: return null
        return PsiTypesUtil.getClassType(clazz)
      } else if (name == "markupEnabled" || name == "flip") {
        val clazz = JavaPsiFacade.getInstance(project).findClass("java.lang.Boolean", GlobalSearchScope.allScope(project)) ?: return null
        return PsiTypesUtil.getClassType(clazz)
      }
    }

    return null
  }

  override fun resolveToTypeString(): String? = resolveToType()?.canonicalText

  override fun setName(@NonNls name: String): PsiElement? {
    SkinElementFactory.createPropertyName(project, name, nameIdentifier.stringLiteral.quotationChar)?.let { newPropertyName ->
      propertyName.replace(newPropertyName)
      return newPropertyName
    }

    return null
  }

  override fun getPresentation() = object : ItemPresentation {
    override fun getLocationString() = null

    override fun getIcon(unused: Boolean): Icon {

      val force = (PsiTreeUtil.findFirstParent(this@SkinPropertyMixin, { it is SkinClassSpecification }) as? SkinClassSpecification)?.classNameAsString == "com.badlogic.gdx.graphics.Color"

      (value as? SkinObject)?.asColor(force)?.let { color ->
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

    override fun getPresentableText() = (value as? SkinLiteral)?.asString()?.let { name + ": " + it } ?: name
  }

}
