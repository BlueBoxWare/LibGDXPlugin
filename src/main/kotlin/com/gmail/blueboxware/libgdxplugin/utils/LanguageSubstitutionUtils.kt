package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectGdxJsonFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonGdxJsonFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectSkinFiles
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.impl.FoldingModelImpl
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.ui.EditorNotifications
import com.intellij.util.FileContentUtilCore
import com.intellij.util.indexing.FileBasedIndex
import kotlin.reflect.KClass


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

internal fun Project.markFileAsSkin(file: VirtualFile) {
  EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(this, file)
  changeFileSubstitution(
          file,
          LibGDXProjectNonSkinFiles::class,
          LibGDXProjectSkinFiles::class
  )
}

internal fun Project.markFileAsNonSkin(file: VirtualFile) =
        changeFileSubstitution(
                file,
                LibGDXProjectSkinFiles::class,
                LibGDXProjectNonSkinFiles::class
        )

internal fun Project.markFileAsGdxJson(file: VirtualFile) {
  EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(this, file)
  changeFileSubstitution(
          file,
          LibGDXProjectNonGdxJsonFiles::class,
          LibGDXProjectGdxJsonFiles::class
  )
}

internal fun Project.markFileAsNonGdxJson(file: VirtualFile) =
        changeFileSubstitution(
                file,
                LibGDXProjectGdxJsonFiles::class,
                LibGDXProjectNonGdxJsonFiles::class
        )

private fun Project.changeFileSubstitution(
        file: VirtualFile,
        from: KClass<out PersistentFileSetManager>,
        to: KClass<out PersistentFileSetManager>
) {

  if (isDisposed) {
    return
  }

  val fromFiles = getComponent(from.java)
  val toFiles = getComponent(to.java)

  fromFiles.remove(file)
  toFiles.add(file)

  LanguageUtil.getFileLanguage(file)?.let { currentLanguage ->
    LanguageSubstitutors.INSTANCE.substituteLanguage(currentLanguage, file, this)
  }

  DaemonCodeAnalyzer.getInstance(this).restart()
  FileBasedIndex.getInstance().requestReindex(file)
  FileContentUtilCore.reparseFiles(file)

  FileDocumentManager.getInstance().getDocument(file)?.let { document ->
    EditorFactory.getInstance().getEditors(document).forEach { editor ->
      (editor.foldingModel as? FoldingModelImpl)?.rebuild()
    }
  }

  EditorNotifications.getInstance(this).updateNotifications(file)

}