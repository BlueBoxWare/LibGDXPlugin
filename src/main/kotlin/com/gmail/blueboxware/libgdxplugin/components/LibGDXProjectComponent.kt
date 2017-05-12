package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectSkinFiles
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentAdapter
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
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
class LibGDXProjectComponent(val project: Project): ProjectComponent {

  override fun getComponentName() = "LibGDXProjectComponent"

  override fun initComponent() { }

  override fun projectClosed() { }

  override fun projectOpened() {

    EditorFactory.getInstance().eventMulticaster.addDocumentListener(documentListener)

    project.getComponent(LibGDXProjectSkinFiles::class.java)?.let { skins ->
      for (skinFile in skins.files) {
        EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(project, skinFile)
      }
    }
  }

  override fun disposeComponent() {

    EditorFactory.getInstance().eventMulticaster.removeDocumentListener(documentListener)

  }

  private val documentListener = object : DocumentAdapter() {

    override fun documentChanged(event: DocumentEvent?) {

      if (project.isDisposed) return

      val document = event?.document ?: return

      val virtualFile = FileDocumentManager.getInstance().getFile(document) ?: return
      val settings = ServiceManager.getService(project, LibGDXPluginSettings::class.java) ?: return
      val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java)?.files ?: return

      if (!nonSkinFiles.contains(virtualFile) && !settings.neverAskAboutSkinFiles) {
        EditorNotifications.getInstance(project).updateNotifications(virtualFile)
      }
    }

  }


}

