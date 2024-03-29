package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont

import com.intellij.openapi.fileTypes.LanguageFileType
import icons.Icons
import javax.swing.Icon

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
internal object BitmapFontFileType : LanguageFileType(BitmapFontLanguage.INSTANCE) {

    override fun getIcon(): Icon = Icons.FONT_FILETYPE

    override fun getName(): String = "libGDX Bitmap Font"

    override fun getDefaultExtension() = "fnt"

    @Suppress("DialogTitleCapitalization")
    override fun getDescription() = "libGDX bitmap font file"

}
