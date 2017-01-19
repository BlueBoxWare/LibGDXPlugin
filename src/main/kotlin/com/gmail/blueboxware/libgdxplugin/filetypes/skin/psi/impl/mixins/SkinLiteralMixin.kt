package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPsiUtil
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.intellij.lang.ASTNode

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
abstract class SkinLiteralMixin(node: ASTNode) : SkinLiteral, SkinValueImpl(node) {

  fun isQuotedString() = node.findChildByType(SkinParserDefinition.STRING_LITERALS)

  override fun asString(): String = SkinPsiUtil.stripQuotes(text)

  override fun asFloat(): Float? = try {
    asString().toFloat()
  } catch (e: NumberFormatException) {
    null
  }

  override fun getQuotationChar(): Char? = text.firstOrNull()?.let { if (it == '"' || it == '\'')  it else null }

}