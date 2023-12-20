package com.gmail.blueboxware.libgdxplugin.settings

import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.utils.PersistentFileSetManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.impl.FileTypeOverrider
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile

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
@State(name = "LibGDXSkins")
@Service(Service.Level.PROJECT)
class LibGDXProjectSkinFiles : PersistentFileSetManager()

@State(name = "LibGDXNonSkins")
@Service(Service.Level.PROJECT)
class LibGDXProjectNonSkinFiles : PersistentFileSetManager()

@State(name = "LibGDXGdxJsonFiles")
@Service(Service.Level.PROJECT)
class LibGDXProjectGdxJsonFiles : PersistentFileSetManager()

@State(name = "LibGDXNonGdxJsonFiles")
@Service(Service.Level.PROJECT)
class LibGDXProjectNonGdxJsonFiles : PersistentFileSetManager()

@Suppress("UnstableApiUsage")
internal class LibGDXFileTypeOverrider : FileTypeOverrider {

    override fun getOverriddenFileType(file: VirtualFile): FileType? {

        val locator = ProjectLocator.getInstance()
        val project = try {
            locator.guessProjectForFile(file)
        } catch (e: UnsupportedOperationException) {
            null
        } ?: return null

        val url = VfsUtilCore.pathToUrl(file.path)
        project.getService(LibGDXProjectSkinFiles::class.java)?.let {
            if (it.contains(url)) {
                return LibGDXSkinFileType
            }
        }
        project.getService(LibGDXProjectGdxJsonFiles::class.java)?.let {
            if (it.contains(url)) {
                return LibGDXJsonFileType
            }
        }

        return null
    }

}

