package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.addCommentExt
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.addPropertyExt
import com.gmail.blueboxware.libgdxplugin.utils.color
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiField
import com.intellij.psi.codeStyle.CodeStyleManager
import java.awt.Color
import javax.swing.Icon

abstract class SkinObjectMixin(node: ASTNode) : SkinObject, SkinValueImpl(node) {

  override fun getPropertyNames() = propertyList.mapNotNull { it.name }

  override fun getProperty(name: String) = propertyList.firstOrNull { it.name == name }

  override fun asResource(): SkinResource? = parent as? SkinResource

  override fun resolveToField(property: SkinProperty): PsiField? = resolveToClass()?.findFieldByName(property.name, true)

  override fun addProperty(property: SkinProperty) = addPropertyExt(property)

  override fun addComment(comment: PsiComment) = addCommentExt(comment)

  override fun getPresentation() = object : ItemPresentation {
    override fun getPresentableText(): String? = "object"

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Object
  }

  private fun colorToString(color: Color) = String.format("#%02x%02x%02x%02x", color.red, color.green, color.blue, color.alpha)

  override fun setColor(color: Color): SkinObject? {

    val newObject = SkinElementFactory(project).createObject() ?: return null

    if (propertyNames.contains("hex") || (propertyNames.none { listOf("r", "g", "b", "a").contains(it) })) {

      var quotationChar = "\""

      (propertyList.find { it.name == "hex" }?.propertyValue?.value as? SkinStringLiteral)?.let { oldValue ->
        quotationChar = if (oldValue.isQuoted) "\"" else ""
      }

      newObject.addProperty(SkinElementFactory(project).createProperty("hex", quotationChar + colorToString(color) + quotationChar))

    } else {

      val components = color.getRGBComponents(null)

      for (rgb in listOf("r", "g", "b", "a")) {

        val value = when (rgb) {
          "r" -> components[0]
          "g" -> components[1]
          "b" -> components[2]
          else -> components[3]
        }
        val property = SkinElementFactory(project).createProperty(rgb, value.toString())

        newObject.addProperty(property)

      }

    }

    CodeStyleManager.getInstance(project).reformat(newObject)

    return newObject

  }

  override fun asColor(force: Boolean): Color? {

    var thisColor: Color? = null

    if (propertyList.size == 1 && propertyList.firstOrNull()?.name == "hex") {

      (propertyList.firstOrNull()?.value as? SkinStringLiteral)?.value?.let { string ->
        thisColor = color(string)
      }

    } else if (propertyList.size == 3 || propertyList.size == 4 || force) {

      var r: Float? = null
      var g: Float? = null
      var b: Float? = null
      var a = 1.0f

      for (property in propertyList) {

        (property.value as? SkinStringLiteral)?.value?.toFloatOrNull()?.let { d ->

          when (property.name) {
            "r" -> r = d
            "g" -> g = d
            "b" -> b = d
            "a" -> a = d
          }

        }
      }

      if (force || (r != null && g != null && b != null)) {

        thisColor = color(r ?: 1f, g ?: 1f, b ?: 1f, a)

      }

    }

    return thisColor

  }

}