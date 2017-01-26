package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectSkinFiles
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.impl.FoldingModelImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutors
import com.intellij.ui.EditorNotifications
import com.intellij.util.FileContentUtilCore
import com.intellij.util.indexing.FileBasedIndex

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
private val identifier = """\p{javaJavaIdentifierStart}\p{javaJavaIdentifierPart}*"""
private val className = """[\p{javaJavaIdentifierStart}&&[\p{Lu}]]\p{javaJavaIdentifierPart}*"""
private val fqClassName = """$identifier(?:\.$identifier)*(?:\.$className)"""

val SKIN_SIGNATURE = Regex("""com\.badlogic\.gdx\.$fqClassName\s*["']?\s*:\s*\{""")

fun markFileAsSkin(project: Project, file: VirtualFile) {


  if (project.isDisposed) return

  val skinFiles = project.getComponent(LibGDXProjectSkinFiles::class.java) ?: return
  val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java) ?: return

  skinFiles.add(file)
  nonSkinFiles.remove(file)

  val currentLanguage = LanguageUtil.getFileLanguage(file) ?: return
  LanguageSubstitutors.INSTANCE.substituteLanguage(currentLanguage, file, project)

  DaemonCodeAnalyzer.getInstance(project).restart()
  FileBasedIndex.getInstance().requestReindex(file)
  FileContentUtilCore.reparseFiles(file)
  FileDocumentManager.getInstance().getDocument(file)?.let { document ->
    for (editor in EditorFactory.getInstance().getEditors(document)) {
      (editor.foldingModel as? FoldingModelImpl)?.rebuild()
    }
  }
  EditorNotifications.getInstance(project).updateNotifications(file)

}

fun markFileAsNonSkin(project: Project, file: VirtualFile) {

  if (project.isDisposed) return

  val skinFiles = project.getComponent(LibGDXProjectSkinFiles::class.java) ?: return
  val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java) ?: return

  skinFiles.remove(file)
  nonSkinFiles.add(file)

  FileBasedIndex.getInstance().requestReindex(file)
  FileContentUtilCore.reparseFiles(file)
  EditorNotifications.getInstance(project).updateNotifications(file)

}



