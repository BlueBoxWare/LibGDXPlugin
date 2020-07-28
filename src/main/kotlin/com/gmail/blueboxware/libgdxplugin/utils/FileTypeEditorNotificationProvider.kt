package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.intellij.lang.Language
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotifications

/*
 * Copyright 2019 Blue Box Ware
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
abstract class FileTypeEditorNotificationProvider(
        protected val project: Project,
        private val forLanguage: Language
): EditorNotifications.Provider<EditorNotificationPanel>() {

  private val notifications: EditorNotifications? = EditorNotifications.getInstance(project)

  abstract val messageKey: String

  abstract fun onYes(file: VirtualFile)
  abstract fun onNo(file: VirtualFile)
  abstract fun onNever(settings: LibGDXPluginSettings)

  abstract fun shouldShowNotification(currentLanguage: Language?, file: VirtualFile, fileEditor: TextEditor, settings: LibGDXPluginSettings): Boolean

  override fun createNotificationPanel(file: VirtualFile, fileEditor: FileEditor, project: Project): EditorNotificationPanel? {

    if (fileEditor !is TextEditor) {
      return null
    }

    val currentLanguage = LanguageUtil.getLanguageForPsi(project, file)

    if (currentLanguage == forLanguage) {
      return null
    }

    val settings = ServiceManager.getService(project, LibGDXPluginSettings::class.java) ?: return null

    if (!shouldShowNotification(currentLanguage, file, fileEditor, settings)) {
      return null
    }

    return EditorNotificationPanel().apply {

      setText(message(messageKey, file.fileType.description))

      createActionLabel(message("filetype.yes")) {
        onYes(file)
        notifications?.updateAllNotifications()
      }

      createActionLabel(message("filetype.no")) {
        onNo(file)
        notifications?.updateAllNotifications()
      }

      createActionLabel(message("filetype.do.not.bother")) {
        onNever(settings)
        notifications?.updateAllNotifications()
      }

    }

  }

}