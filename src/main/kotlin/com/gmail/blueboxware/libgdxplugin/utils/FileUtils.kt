package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager

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
internal fun Project.getProjectBaseDir() =
        if (ApplicationManager.getApplication().isUnitTestMode) {
          VirtualFileManager.getInstance().findFileByUrl("temp:///")
        } else {
          guessProjectDir()
        }

internal fun fileNameToPathList(fileName: String): Array<String> = fileName.replace('\\', '/').split("/").toTypedArray()

internal fun VirtualFile.toPsiFile(project: Project) = PsiManager.getInstance(project).findFile(this)