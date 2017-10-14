package com.gmail.blueboxware.libgdxplugin.filetypes.atlas

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasPage
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.FileViewProvider
import icons.Icons
import java.io.File

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
  fun getRegions(name: String? = null): List<AtlasRegion> = getPages().flatMap { it.regionList.filter { name == null || it.name == name } }

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

  override fun getPresentation() = object: ItemPresentation {

    override fun getLocationString(): String {
      project.baseDir?.let { baseDir ->
        virtualFile?.let { virtualFile ->
          return VfsUtil.getPath(baseDir, virtualFile, File.separatorChar) ?: ""
        }
      }

      return ""
    }

    override fun getIcon(unused: Boolean) = Icons.ATLAS_FILETYPE

    override fun getPresentableText() = name
  }
}