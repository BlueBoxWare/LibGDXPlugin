package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.gmail.blueboxware.libgdxplugin.utils.childrenOfType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

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
class BitmapFontFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, BitmapFontLanguage.INSTANCE) {

  private fun getCharacterMap(): Map<Int, BitmapFontFontChar> {

    val map = mutableMapOf<Int, BitmapFontFontChar>()

    childrenOfType<BitmapFontFontChar>().forEach { fontChar ->
      fontChar.character?.let { id ->
        map.put(id, fontChar)
      }
    }

    return map

  }

  fun getKernings(): Collection<BitmapFontKerning> = childrenOfType()

  override fun getFileType() = viewProvider.fileType

  fun getInfoElement(): BitmapFontInfo? = childOfType()

  fun getCommonElement(): BitmapFontCommon? = childOfType()

  fun getCharsElement(): BitmapFontChars? = childOfType()

  fun getKerningsElement(): BitmapFontKernings? = childOfType()

  fun getPages(): MutableCollection<BitmapFontPageDefinition> = childrenOfType<BitmapFontPageDefinition>().toMutableList()

  fun getCharacters() = getCharacterMap().values

  fun getCharacter(id: Int): BitmapFontFontChar? = getCharacterMap()[id]

}