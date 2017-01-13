package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinResourceReference
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.conversion.copy.range

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
abstract class SkinPropertyValueMixin(node: ASTNode) : SkinPropertyValue, SkinElementImpl(node) {

  override fun getProperty(): SkinProperty? = PsiTreeUtil.findFirstParent(this, { it is SkinProperty }) as? SkinProperty

  override fun getReference(): PsiReference? {
    if (value is SkinStringLiteral) {
      return SkinResourceReference(this, range)
    }

    return null
  }

  override fun setValueAsString(string: String, quotationChar: Char?) {
      SkinElementFactory.createStringLiteral(project, string, quotationChar)?.let {
        value.replace(it)
      }
  }

}