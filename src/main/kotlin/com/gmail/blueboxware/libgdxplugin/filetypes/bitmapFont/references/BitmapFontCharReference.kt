package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.references

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFile
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.BitmapFontKerning
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*

/*
 * Copyright 2017 Blue Box Ware
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
class BitmapFontCharReference<T: BitmapFontKerning>(element: T) : PsiReferenceBase<T>(element, element.textRange), PsiPolyVariantReference {

  override fun getVariants(): Array<out Any> = arrayOf()

  override fun resolve(): PsiElement?  = null

  override fun getRangeInElement(): TextRange = ElementManipulators.getValueTextRange(element)

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

    val result = mutableListOf<ResolveResult>()

    element.getValue("first")?.toIntOrNull()?.let { id ->
      (element.containingFile as? BitmapFontFile)?.getCharacter(id)?.let { ch ->
        result.add(PsiElementResolveResult(ch))
      }
    }

    element.getValue("second")?.toIntOrNull()?.let { id ->
      (element.containingFile as? BitmapFontFile)?.getCharacter(id)?.let { ch ->
        result.add(PsiElementResolveResult(ch))
      }
    }

    return result.toTypedArray()

  }
}