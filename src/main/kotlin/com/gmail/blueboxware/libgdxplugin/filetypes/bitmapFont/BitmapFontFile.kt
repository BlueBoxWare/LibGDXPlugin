package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.*
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil

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

  fun getCharacterMap(): Map<Int, BitmapFontFontChar> {

    val map = mutableMapOf<Int, BitmapFontFontChar>()

    PsiTreeUtil.findChildrenOfType(this, BitmapFontFontChar::class.java).forEach { fontChar ->
      fontChar.character?.let { id ->
        map.put(id, fontChar)
      }
    }

    return map

  }

  fun getKernings(): Collection<BitmapFontKerning> = PsiTreeUtil.findChildrenOfType(this, BitmapFontKerning::class.java)

  override fun getFileType() = viewProvider.fileType

  fun getInfoElement(): BitmapFontInfo? = PsiTreeUtil.findChildOfType(this, BitmapFontInfo::class.java)

  fun getCommonElement(): BitmapFontCommon? = PsiTreeUtil.findChildOfType(this, BitmapFontCommon::class.java)

  fun getCharsElement(): BitmapFontChars? = PsiTreeUtil.findChildOfType(this, BitmapFontChars::class.java)

  fun getKerningsElement(): BitmapFontKernings? = PsiTreeUtil.findChildOfType(this, BitmapFontKernings::class.java)

  fun getPages() = PsiTreeUtil.findChildrenOfType(this, BitmapFontPageDefinition::class.java)

  fun getCharacters() = getCharacterMap().values

  fun getCharacter(id: Int): BitmapFontFontChar? = getCharacterMap()[id]

}