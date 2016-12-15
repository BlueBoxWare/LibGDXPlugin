package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.utils.PersistentFileSetManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.State
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

@State(name = "LibGDXProjectSettings")
class LibGDXProjectSettings() : ProjectComponent, PersistentStateComponent<LibGDXProjectSettings> {

  var neverAskAboutSkinFiles: Boolean = false

  override fun loadState(state: LibGDXProjectSettings?) {
    neverAskAboutSkinFiles = state?.neverAskAboutSkinFiles ?: false
  }

  override fun getState(): LibGDXProjectSettings? {
    return this
  }

  override fun disposeComponent() { }

  override fun initComponent() { }

  override fun getComponentName() = "LibGDXProjectSettings"

  override fun projectClosed() { }

  override fun projectOpened() { }
}

open class LibGDXPersistentFileSetManager : PersistentFileSetManager() {

  fun add(file: VirtualFile) = addFile(file)

  fun remove(file: VirtualFile) = removeFile(file)

  fun contains(file: VirtualFile) = containsFile(file)

}

@State(name = "LibGDXSkins")
class LibGDXProjectSkinFiles : LibGDXPersistentFileSetManager()

@State(name = "LibGDXNonSkins")
class LibGDXProjectNonSkinFiles : LibGDXPersistentFileSetManager()