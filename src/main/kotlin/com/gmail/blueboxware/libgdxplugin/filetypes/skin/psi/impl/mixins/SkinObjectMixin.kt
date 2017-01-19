package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.gmail.blueboxware.libgdxplugin.utils.stringToColor
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.codeStyle.CodeStyleManager
import java.awt.Color
import javax.swing.Icon

abstract class SkinObjectMixin(node: ASTNode) : SkinObject, SkinValueImpl(node) {

  override fun getPropertyNames() = propertyList.mapNotNull { it.name }

  override fun getProperty(name: String) = propertyList.firstOrNull { it.name == name }

  override fun asResource(): SkinResource? = parent as? SkinResource

  override fun addProperty(property: SkinProperty) {
    if (firstChild?.text == "{") {
      addAfter(property, firstChild)?.let {
        if (propertyList.size > 1) {
          SkinElementFactory.createComma(project)?.let { comma ->
            addAfter(comma, it)
          }
        }
      }
    }
  }

  override fun getPresentation() = object : ItemPresentation {
    override fun getPresentableText(): String? = "object"

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Object
  }

  private fun colorToString(color: Color) = String.format("#%02x%02x%02x%02x", color.red, color.green, color.blue, color.alpha)

  override fun setColor(color: Color): SkinObject? {

    val newObject = SkinElementFactory.createObject(project) ?: return null

    if (propertyNames.contains("hex") || (propertyNames.none { listOf("r", "g", "b", "a").contains(it) })) {

      var quotationChar = "\""

      (propertyList.find { it.name == "hex" }?.propertyValue?.value as? SkinLiteral)?.let { oldValue ->
        quotationChar = oldValue.quotationChar?.toString() ?: ""
      }

      newObject.addProperty(SkinElementFactory.createProperty(project, "hex", quotationChar + colorToString(color) + quotationChar))

    } else {

      var quotationChar = ""

      for (property in propertyList) {
        if (listOf("r", "g", "b", "a").contains(property.name)) {
          (property.propertyValue?.value as? SkinLiteral)?.let {
            quotationChar = it.quotationChar?.toString() ?: ""
          }
        }
      }

      val components = color.getRGBComponents(null)

      for (rgb in listOf("r", "g", "b", "a")) {

        val value = when (rgb) {
          "r" -> components[0]
          "g" -> components[1]
          "b" -> components[2]
          else -> components[3]
        }
        val property = SkinElementFactory.createProperty(project, rgb, quotationChar + value + quotationChar)

        newObject.addProperty(property)

      }

    }

    CodeStyleManager.getInstance(project).reformat(newObject)

    return newObject

  }

  override fun asColor(force: Boolean): Color? {

    var color: Color? = null

    if (propertyList.size == 1 && propertyList.firstOrNull()?.name == "hex") {

      (propertyList.firstOrNull()?.value as? SkinLiteral)?.asString()?.let { string ->
        color = stringToColor(string)
      }

    } else if (propertyList.size == 3 || propertyList.size == 4 || force) {

      var r: Float? = null
      var g: Float? = null
      var b: Float? = null
      var a: Float = 1.0f

      for (property in propertyList) {

        (property.value as? SkinLiteral)?.asFloat()?.let { d ->

          when (property.name) {
            "r" -> r = d
            "g" -> g = d
            "b" -> b = d
            "a" -> a = d
          }

        }
      }

      if (force || (r != null && g != null && b != null)) {

        try {
          color = Color(r ?: 1f, g ?: 1f, b ?: 1f, a)
        } catch (e: IllegalArgumentException) {
          // Do nothing
        }

      }

    }

    return color

  }

}