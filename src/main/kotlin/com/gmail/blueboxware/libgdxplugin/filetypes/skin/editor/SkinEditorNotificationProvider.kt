package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.json.JsonLanguage
import com.intellij.lang.Language
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel


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
class SkinEditorNotificationProvider(project: Project): FileTypeEditorNotificationProvider(
        project,
        LibGDXSkinLanguage.INSTANCE
) {

  override val messageKey = "skin.file.detected"

  override fun onYes(file: VirtualFile) = project.markFileAsSkin(file)

  override fun onNo(file: VirtualFile) = project.markFileAsNonSkin(file)

  override fun onNever(settings: LibGDXPluginSettings) {
    settings.neverAskAboutSkinFiles = true
  }

  override fun shouldShowNotification(
          currentLanguage: Language?,
          file: VirtualFile,
          fileEditor: TextEditor,
          settings: LibGDXPluginSettings
  ): Boolean =
          if (
                  currentLanguage != PlainTextLanguage.INSTANCE
                  && currentLanguage != JsonLanguage.INSTANCE
                  && currentLanguage != LibGDXJsonLanuage.INSTANCE
          ) {
            false
          } else if (settings.neverAskAboutSkinFiles) {
            false
          } else {
            val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java)
            if (nonSkinFiles.contains(file)) {
              false
            } else {
              fileEditor.editor.document.text.contains(SKIN_SIGNATURE)
            }
          }

  override fun getKey(): Key<EditorNotificationPanel> = KEY

  companion object {
    val KEY = key<EditorNotificationPanel>("skin.file.detected")
  }

}