package com.gmail.blueboxware.libgdxplugin.filetypes.atlas

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
class LibGDXAtlasFileType private constructor() : LanguageFileType(LibGDXAtlasLanguage.INSTANCE) {

  companion object {
    val INSTANCE = LibGDXAtlasFileType()
  }

  override fun getIcon(): Icon?  = Icons.LIBGDX_ICON

  override fun getName(): String  = "LibGDX Atlas"

  override fun getDefaultExtension() = "atlas"

  override fun getDescription() = "LibGDX Texture Atlas file"



}