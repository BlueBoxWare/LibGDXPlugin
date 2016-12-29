package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon

abstract class SkinObjectMixin(node: ASTNode) : SkinObject, SkinValueImpl(node) {

  override fun getPresentation() = object : ItemPresentation {
    override fun getPresentableText(): String? = "object"

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon? = AllIcons.Json.Object
  }

}