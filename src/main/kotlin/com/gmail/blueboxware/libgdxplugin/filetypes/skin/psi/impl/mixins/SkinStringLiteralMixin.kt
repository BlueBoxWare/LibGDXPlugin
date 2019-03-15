package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinFileReference
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinResourceReference
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.factory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.stripQuotes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.unescape
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiReference

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
abstract class SkinStringLiteralMixin(node: ASTNode) : SkinStringLiteral, SkinValueImpl(node) {

  override fun getValue(): String = text.stripQuotes().unescape()

  override fun asPropertyName(): SkinPropertyName? = this.parent as? SkinPropertyName

  override fun isBoolean(): Boolean = text == "true" || text == "false"

  override fun getReference(): PsiReference? {

    val containingObjectType = property?.containingObject?.resolveToTypeString()

    if (
            (containingObjectType == BITMAPFONT_CLASS_NAME && property?.name == PROPERTY_NAME_FONT_FILE)
            || (containingObjectType == FREETYPE_FONT_PARAMETER_CLASS_NAME && property?.name == "font")
    ) {
      return SkinFileReference(this, containingFile)
    } else {
      property?.let { property ->
        property.resolveToType().let { type ->
          if (
                  isBoolean && type?.canonicalText == "java.lang.Boolean"
                  || type?.canonicalText == "java.lang.Integer"
                  || type is PsiPrimitiveType
                  || type == null
          ) {
            return null
          } else if (type.isStringType(property)) {
            if (containingObjectType != TINTED_DRAWABLE_CLASS_NAME || property.name != PROPERTY_NAME_TINTED_DRAWABLE_NAME) {
              return null
            }
          }
        }

      }

    }

    return SkinResourceReference(this)

  }

  override fun setValue(string: String) {
    factory()?.createStringLiteral(string, isQuoted)?.let {
      replace(it)
    }
  }

  override fun isQuoted(): Boolean = text.firstOrNull() == '\"'

}