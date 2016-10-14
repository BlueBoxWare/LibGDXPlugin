package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.inspections.utils.GDXLibrary
import com.gmail.blueboxware.libgdxplugin.inspections.utils.GradleBuildFileVersionsVisitor
import com.gmail.blueboxware.libgdxplugin.inspections.utils.mavenCoordMap
import com.gmail.blueboxware.libgdxplugin.inspections.utils.versionStringRegex
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.*
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor

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

  private val libraryVersions = mutableMapOf<GDXLibrary, String>()

  val isLibGDXProject: Boolean
    get() { return libraryVersions[GDXLibrary.GDX] != null || isTesting }

  // For testing only
  var isTesting = false

  override fun getComponentName() = "LibGDXProjectComponent"

  fun getLibraryVersion(gdxLibrary: GDXLibrary) = libraryVersions[gdxLibrary]

  override fun initComponent() { }

  override fun projectClosed() { }

  override fun projectOpened() {
    VirtualFileManager.getInstance().addVirtualFileListener(object: VirtualFileAdapter() {

      private fun handleEvent(event: VirtualFileEvent) {
        if (event.file.name == "build.gradle") {
          updateLibGDXLibraryVersions()
        }
      }

      override fun fileCreated(event: VirtualFileEvent) {
        handleEvent(event)
      }

      override fun contentsChanged(event: VirtualFileEvent) {
        handleEvent(event)
      }

      override fun fileDeleted(event: VirtualFileEvent) {
        handleEvent(event)
      }

      override fun propertyChanged(event: VirtualFilePropertyEvent) {
        if (event.propertyName == VirtualFile.PROP_NAME && event.newValue == "build.gradle") {
          updateLibGDXLibraryVersions()
        }
      }
    })

    StartupManager.getInstance(project).registerPostStartupActivity {
      updateLibGDXLibraryVersions()
    }
  }

  override fun disposeComponent() { }

  private fun updateLibGDXLibraryVersions() {

    libraryVersions.clear()

    for (lib in ProjectLibraryTable.getInstance(project).libraryIterator) {
      lib.name?.let { name ->

        for (library in GDXLibrary.values()) {
          val regex = Regex("${mavenCoordMap[library]}:(${versionStringRegex})")
          val matchResult = regex.find(name)
          if (matchResult?.groupValues?.get(1) != null) {
            libraryVersions[library] = matchResult?.groupValues?.get(1) ?: continue
          }
        }

      }
    }

    // not found in project library table, check build.gradle files manually

    val files = FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.allScope(project))

    for (file in files) {
      file.accept(GroovyPsiElementVisitor(object: GradleBuildFileVersionsVisitor() {

        override fun onVersionFound(lib: GDXLibrary, version: String, element: PsiElement) {
          libraryVersions[lib] = version
        }
      }))

    }

  }

}


