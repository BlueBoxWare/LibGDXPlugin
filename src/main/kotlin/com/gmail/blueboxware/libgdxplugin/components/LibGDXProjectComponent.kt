package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.*
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.kohsuke.github.RateLimitHandler
import java.io.IOException
import java.util.*

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

  private val LOG = Logger.getInstance("#com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent")

  private val usedLibraryVersions = mutableMapOf<GDXLibrary, String>()
  private val latestLibraryVersions = mutableMapOf<GDXLibrary, String>()

  private var latestLibraryVersionLastChecked = -1

  val isLibGDXProject: Boolean
    get() { return usedLibraryVersions[GDXLibrary.GDX] != null || isTesting }

  // For testing only
  var isTesting = false

  override fun getComponentName() = "LibGDXProjectComponent"

  fun getUsedLibraryVersion(gdxLibrary: GDXLibrary) = usedLibraryVersions[gdxLibrary]

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

    usedLibraryVersions.clear()

    for (lib in ProjectLibraryTable.getInstance(project).libraryIterator) {
      lib.name?.let { name ->

        for (library in GDXLibrary.values()) {
          val regex = Regex("${mavenCoordMap[library]}:($versionStringRegex)")
          val matchResult = regex.find(name)
          if (matchResult?.groupValues?.get(1) != null) {
            usedLibraryVersions[library] = matchResult?.groupValues?.get(1) ?: continue
          }
        }

      }
    }

    // not found in project library table, check build.gradle files manually

    val files = FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.allScope(project))

    for (file in files) {
      file.accept(GroovyPsiElementVisitor(object: GradleBuildFileVersionsVisitor() {

        override fun onVersionFound(library: GDXLibrary, version: String, element: PsiElement) {
          usedLibraryVersions[library] = version
        }
      }))

    }

  }

  fun getLatestLibraryVersion(library: GDXLibrary, fromCache: Boolean = false): String? {

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    if (currentHour != latestLibraryVersionLastChecked) {
      retrieveLatestVersionsFromGitHub()
      latestLibraryVersionLastChecked = currentHour
    }

    return latestLibraryVersions[library]
  }

  private fun retrieveLatestVersionsFromGitHub() {

    val gitHub: GitHub = try {
      GitHubBuilder().withRateLimitHandler(RateLimitHandler.FAIL).build()
    } catch (e: IOException) {
      LOG.debug("Could not create GitHub connection", e)
      return
    }

    for (library in GDXLibrary.values()) {
      val version = retrieveLatestVersionFromGithub(gitHub, library)
      if (version != null) {
        latestLibraryVersions[library] = version
      }
    }

  }

  private fun retrieveLatestVersionFromGithub(gitHub: GitHub, library: GDXLibrary): String? {

    val repository = try {
      gitHub.getRepository(repoMap[library])
    } catch (e: IOException) {
      LOG.debug("Could not get repository from GitHub", e)
      return null
    }
    val tags = try {
      repository.listTags().asList()
    } catch (e: IOException) {
      LOG.debug("Could not get tags from GitHub", e)
      return null
    }

    var latestVersion: String? = null

    for (tag in tags) {
      var name = tag.name

      if (name.matches(Regex(".*-[0-9]+(\\.[0-9]+)*"))) {
        name = name.substring(name.lastIndexOf("-") + 1, name.length)
      }

      if (name.matches(versionStringRegex)) {
        if (latestVersion == null || compareVersionStrings(name, latestVersion) > 0) {
          latestVersion = name
        }
      }
    }

    return latestVersion

  }

}


