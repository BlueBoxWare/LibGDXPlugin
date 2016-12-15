package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.annotators.SkinHighlighter
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSettings
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSkinFiles
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsSkin
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications

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
class SkinEditorNotificationProvider(val project: Project, val notifications: EditorNotifications) : EditorNotifications.Provider<EditorNotificationPanel>() {

  companion object {
    val KEY = Key<EditorNotificationPanel>("com.gmail.blueboxware.libgdxplugin.skin.file.detected")
  }

  override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor): EditorNotificationPanel? {

    if (fileEditor !is TextEditor) return null

    file.extension?.let { extension ->
      if (extension !in listOf("json", "skin", "txt", "" )) {
        return null
      }
    }

    val settings = project.getComponent(LibGDXProjectSettings::class.java) ?: return null
    val skinFiles = project.getComponent(LibGDXProjectSkinFiles::class.java) ?: return null
    val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java) ?: return null

    if (settings.neverAskAboutSkinFiles == true
            || skinFiles.contains(file)
            || nonSkinFiles.contains(file)
            || !fileEditor.editor.document.text.contains(SkinHighlighter.SKIN_SIGNATURE)
    ) {
      return null
    }

    val editorNotificationPanel = EditorNotificationPanel()

    editorNotificationPanel.setText(message("skin.file.detected"))

    editorNotificationPanel.createActionLabel(message("skin.file.yes"), {
      markFileAsSkin(project, file, true)
      notifications.updateAllNotifications()
    })

    editorNotificationPanel.createActionLabel(message("skin.file.no"), {
      markFileAsSkin(project, file, false)
      notifications.updateAllNotifications()
    })

    editorNotificationPanel.createActionLabel(message("skin.file.do.not.bother"), {
      settings.neverAskAboutSkinFiles = true
      notifications.updateAllNotifications()
    })

    return editorNotificationPanel

  }

  override fun getKey() = KEY

}