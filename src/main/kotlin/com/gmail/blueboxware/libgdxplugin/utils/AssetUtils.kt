package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import java.io.IOException

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
object AssetUtils {

  fun getAssociatedFiles(virtualFile: VirtualFile): List<VirtualFile> {

    val result = mutableListOf<VirtualFile>()

    VfsUtil.visitChildrenRecursively(virtualFile.parent, object : VirtualFileVisitor<Unit>() {
      override fun visitFile(file: VirtualFile): Boolean {
        if (!file.isDirectory && file != virtualFile) {
          result.add(file)
        }

        return true
      }
    })

    return result
  }

  fun getAssociatedAtlas(file: VirtualFile): VirtualFile? {

    val dot = file.name.lastIndexOf('.')
    if (dot > -1) {
      val atlasName = file.name.substring(0, dot) + ".atlas"
      return file.parent.findChild(atlasName)
    }

    return null
  }

  fun readImageNamesFromAtlas(file: VirtualFile): List<String> {

    val result = mutableListOf<String>()

    try {
      file.inputStream.use {
        var previousLine = ""

        it.reader().forEachLine { line ->
          if (line.firstOrNull() == ' ' && previousLine.firstOrNull() != ' ') {
            result.add(previousLine)
          }
          previousLine = line
        }

      }
    } catch (e: IOException) {
      // Do nothing
    }

    return result
  }

}