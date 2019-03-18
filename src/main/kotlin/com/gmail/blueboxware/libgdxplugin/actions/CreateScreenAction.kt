package com.gmail.blueboxware.libgdxplugin.actions

import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiDirectory
import org.jetbrains.kotlin.idea.KotlinFileType

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
class CreateScreenAction: CreateFileFromTemplateAction("LibGDX Screen", "Creates new LibGDX Screen", IconLoader.getIcon("/runConfigurations/application.png")) {

  override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?) = "LibGDX Screen"

  override fun buildDialog(project: Project?, directory: PsiDirectory?, builder: CreateFileFromTemplateDialog.Builder) {
    builder.setTitle("New LibGDX Screen")
            .addKind("Java", JavaFileType.INSTANCE.icon, "LibGDX Screen (Java)")
            .addKind("Kotlin", KotlinFileType.INSTANCE.icon, "LibGDX Screen (Kotlin)")
  }

  override fun isAvailable(dataContext: DataContext): Boolean {
    if (super.isAvailable(dataContext)) {

      val ideView = LangDataKeys.IDE_VIEW.getData(dataContext) ?: return false
      val project = PlatformDataKeys.PROJECT.getData(dataContext) ?: return false
      val projectFileIndex = ProjectRootManager.getInstance(project).fileIndex

      return project.isLibGDXProject() && ideView.directories.any {
        projectFileIndex.isInSourceContent(it.virtualFile)
      }

    }

    return false
  }
}
