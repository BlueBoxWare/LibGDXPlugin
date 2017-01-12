package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.SKIN_SIGNATURE
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsNonSkin
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsSkin
import com.intellij.json.JsonLanguage
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileTypes.PlainTextLanguage
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

    val currentLanguage = LanguageUtil.getLanguageForPsi(project, file)

    if (currentLanguage == LibGDXSkinLanguage.INSTANCE) return null

    if (currentLanguage != PlainTextLanguage.INSTANCE && currentLanguage != JsonLanguage.INSTANCE) return null

    val settings = project.getComponent(LibGDXProjectSettings::class.java) ?: return null
    val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java) ?: return null

    if (settings.neverAskAboutSkinFiles || nonSkinFiles.contains(file) || !fileEditor.editor.document.text.contains(SKIN_SIGNATURE)
    ) {
      return null
    }

    val editorNotificationPanel = EditorNotificationPanel()

    editorNotificationPanel.setText(message("skin.file.detected", params = file.fileType.description))

    editorNotificationPanel.createActionLabel(message("skin.file.yes"), {
      markFileAsSkin(project, file)
      notifications.updateAllNotifications()
    })

    editorNotificationPanel.createActionLabel(message("skin.file.no"), {
      markFileAsNonSkin(project, file)
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