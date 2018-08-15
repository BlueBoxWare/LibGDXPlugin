package com.gmail.blueboxware.libgdxplugin.filetypes.atlas

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasPage
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.utils.FilePresentation
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import icons.Icons

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
class AtlasFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, LibGDXAtlasLanguage.INSTANCE), AtlasElement {

  fun getPages(): List<AtlasPage> = children.mapNotNull { it as? AtlasPage }

  @Suppress("unused")
  fun getRegions(name: String? = null): List<AtlasRegion> =
          getPages().flatMap { page ->
            page.regionList.filter { name == null || it.name == name }
          }

  @Suppress("unused")
  fun getRegion(name: String): AtlasRegion? {

    for (page in getPages()) {
      for (region in page.regionList) {
        if (region.name == name) {
          return region
        }
      }
    }

    return null

  }

  override fun getFileType() = viewProvider.fileType

  override fun toString() = "AtlasFile: " + (virtualFile?.name ?: "<unknown>")

  override fun getPresentation() = FilePresentation(project, virtualFile, name, Icons.ATLAS_FILETYPE)

}