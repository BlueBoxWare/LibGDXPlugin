package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.*
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

  override fun getContainingObject(): SkinObject? = PsiTreeUtil.findFirstParent(this) { it is SkinObject } as? SkinObject

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
        val clazz = JavaPsiFacade.getInstance(project).findClass("java.lang.Integer", GlobalSearchScope.allScope(project)) ?: return null
        return PsiTypesUtil.getClassType(clazz)
      } else if (name == PROPERTY_NAME_FONT_MARKUP || name == PROPERTY_NAME_FONT_FLIP) {
        val clazz = JavaPsiFacade.getInstance(project).findClass("java.lang.Boolean", GlobalSearchScope.allScope(project)) ?: return null
        return PsiTypesUtil.getClassType(clazz)
      }
    }

    return null
  }

  override fun resolveToTypeString(): String? = resolveToType()?.canonicalText

  override fun setName(@NonNls name: String): PsiElement? {
    SkinElementFactory(project).createPropertyName(name, nameIdentifier.stringLiteral.isQuoted)?.let { newPropertyName ->
      propertyName.replace(newPropertyName)
      return newPropertyName
    }

    return null
  }

  override fun getPresentation() = object : ItemPresentation {
    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon {

      val force = (PsiTreeUtil.findFirstParent(this@SkinPropertyMixin) { it is SkinClassSpecification } as? SkinClassSpecification)?.getRealClassNamesAsString()?.contains("com.badlogic.gdx.graphics.Color") ?: false

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
