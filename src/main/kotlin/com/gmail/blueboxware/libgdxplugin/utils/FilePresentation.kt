package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import javax.swing.Icon


/*
 * Copyright 2018 Blue Box Ware
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
class FilePresentation(
        project: Project,
        virtualFile: VirtualFile?,
        val name: String,
        val icon: Icon
): ItemPresentation {

  private val location: String =
          project.baseDir?.let { baseDir ->
            virtualFile?.let { virtualFile ->
              @Suppress("DEPRECATION")
              // COMPAT: getPath() deprecated in 181, findRelativePath() introduced in 181
              VfsUtil.getPath(baseDir, virtualFile, File.separatorChar) ?: ""
            }
          } ?: ""

  override fun getLocationString(): String? = location

  override fun getIcon(unused: Boolean): Icon? = icon

  override fun getPresentableText(): String? = name
}