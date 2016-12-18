package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementGenerator
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinPropertyNameReference
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.util.ArrayUtil
import org.jetbrains.annotations.NonNls

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/306d705e1829bd3c74afc2489bfb7ed59d686b84/json/src/com/intellij/json/psi/impl/JsonPropertyMixin.java
 *
 */
abstract class SkinPropertyMixin(node: ASTNode) : SkinElementImpl(node), SkinProperty {

  override fun setName(@NonNls name: String): PsiElement? {
    val generator = SkinElementGenerator(project)
    nameElement.replace(generator.createStringLiteral(StringUtil.unquoteString(name)))
    return this
  }

  override fun getReferences(): Array<out PsiReference> {
    val fromProviders = ReferenceProvidersRegistry.getReferencesFromProviders(this)
    return ArrayUtil.prepend(SkinPropertyNameReference(this), fromProviders)
  }

  override fun getReference(): PsiReference?  = SkinPropertyNameReference(this)
}
