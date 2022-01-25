package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2

import com.intellij.openapi.fileTypes.LanguageFileType
import icons.Icons
import javax.swing.Icon


/*
 * Copyright 2022 Blue Box Ware
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
class LibGDXAtlas2FileType private constructor() : LanguageFileType(LibGDXAtlas2Language.INSTANCE) {

    companion object {
        val INSTANCE = LibGDXAtlas2FileType()
    }

    override fun getIcon(): Icon = Icons.ATLAS_FILETYPE

    override fun getName(): String = "libGDX Atlas"

    override fun getDefaultExtension() = "atlas"

    @Suppress("DialogTitleCapitalization")
    override fun getDescription() = "libGDX texture atlas file"

}