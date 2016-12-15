package com.gmail.blueboxware.libgdxplugin.actions

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSkinFiles
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsSkin
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import icons.Icons

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
class MarkAsSkinAction : AnAction() {

  override fun update(event: AnActionEvent?) {

    // T getData(@NotNull DataKey<T> key) was added in 162.426: not yet available in Android Studio
    val file = event?.dataContext?.getData(PlatformDataKeys.VIRTUAL_FILE.name)

    val presentation = event?.presentation

    if (file is VirtualFile && !file.isDirectory) {

      presentation?.isEnabled = true

      if (event?.project?.getComponent(LibGDXProjectSkinFiles::class.java)?.contains(file) == true && EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(file)) {

        presentation?.text = message("context.menu.mark.as.non.skin")
        presentation?.icon = IconLoader.getDisabledIcon(Icons.LIBGDX_ICON)

      } else {

        presentation?.text = message("context.menu.mark.as.skin")
        presentation?.icon = Icons.LIBGDX_ICON

      }

    } else {

      presentation?.isEnabled = false

    }

  }

  override fun actionPerformed(event: AnActionEvent?) {

    if (event == null) return

    val project = event.project ?: return
    val file = event.dataContext.getData(PlatformDataKeys.VIRTUAL_FILE.name) ?: return
    val text = event.presentation.text ?: return

    if (file !is VirtualFile) return

    if (text == message("context.menu.mark.as.skin")) {

      markFileAsSkin(project, file, true)

    } else {

      markFileAsSkin(project, file, false)

    }

  }


}