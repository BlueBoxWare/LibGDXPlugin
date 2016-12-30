package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinNumberLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.gmail.blueboxware.libgdxplugin.utils.stringToColor
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import java.awt.Color
import javax.swing.Icon

abstract class SkinObjectMixin(node: ASTNode) : SkinObject, SkinValueImpl(node) {

  override fun getPresentation() = object : ItemPresentation {
    override fun getPresentableText(): String? = "object"

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Object
  }

  override fun asColor(): Color? {

    var color: Color? = null

    if (propertyList.size == 1 && propertyList.firstOrNull()?.name == "hex") {

      (propertyList.firstOrNull()?.value as? SkinStringLiteral)?.value?.let { string ->
        color = stringToColor(string)
      }

    } else if (propertyList.size == 3 || propertyList.size == 4) {

      var r: Float? = null
      var g: Float? = null
      var b: Float? = null
      var a: Float = 1.0f

      for (property in propertyList) {

        (property.value as? SkinNumberLiteral)?.value?.toFloat()?.let { d ->

          when (property.name) {
            "r" -> r = d
            "g" -> g = d
            "b" -> b = d
            "a" -> a = d
          }

        }
      }

      if (r != null && g != null && b != null) {

        try {
          color = Color(r ?: 0f, g ?: 0f, b ?: 0f, a)
        } catch (e: IllegalArgumentException) {
          // Do nothing
        }

      }

    }

    return color

  }

}