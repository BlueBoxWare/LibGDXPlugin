package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinLiteralImpl
import com.intellij.json.psi.impl.JSStringLiteralEscaper
import com.intellij.json.psi.impl.JsonStringLiteralImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.impl.source.tree.LeafElement

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
open class SkinStringLiteralMixin(node: ASTNode) : SkinLiteralImpl(node), PsiLanguageInjectionHost {

  override fun updateText(text: String): PsiLanguageInjectionHost {
    val valueNode = node.firstChildNode as? LeafElement ?: throw AssertionError()

    valueNode.replaceWithText(text)

    return this

  }

  override fun createLiteralTextEscaper() = object : JSStringLiteralEscaper<PsiLanguageInjectionHost>(this) {
    override fun isRegExpLiteral() = false
  }

  override fun isValidHost() = true

  fun toJsonStringLiteral() = JsonStringLiteralImpl(node)
}