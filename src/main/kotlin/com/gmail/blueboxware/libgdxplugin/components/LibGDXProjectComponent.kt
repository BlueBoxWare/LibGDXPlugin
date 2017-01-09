package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentAdapter
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTable
import com.intellij.openapi.startup.StartupManager
import com.intellij.ui.EditorNotifications
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

  val isLibGDXProject: Boolean
    get() { return usedLibraryVersions[GDXLibrary.GDX] != null || isTesting }

  private val LOG = Logger.getInstance("#com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent")

  private val usedLibraryVersions = mutableMapOf<GDXLibrary, String>()
  private val latestLibraryVersions = mutableMapOf<GDXLibrary, String>()

  private var latestLibraryVersionLastChecked = -1

  // For testing only
  var isTesting = false

  override fun getComponentName() = "LibGDXProjectComponent"

  fun getUsedLibraryVersion(gdxLibrary: GDXLibrary) = usedLibraryVersions[gdxLibrary]

  override fun initComponent() { }

  override fun projectClosed() { }

  override fun projectOpened() {

    StartupManager.getInstance(project).registerPostStartupActivity {
      updateLibGDXLibraryVersions()

      ProjectLibraryTable.getInstance(project).addListener(object: LibraryTable.Listener {
        override fun afterLibraryRenamed(library: Library?) {
          updateLibGDXLibraryVersions()
        }

        override fun afterLibraryAdded(newLibrary: Library?) {
          updateLibGDXLibraryVersions()
        }

        override fun afterLibraryRemoved(library: Library?) {
          updateLibGDXLibraryVersions()
        }

        override fun beforeLibraryRemoved(library: Library?) {
        }
      })

    }

    if (project.getComponent(LibGDXProjectSettings::class.java)?.neverAskAboutSkinFiles != true) {

      EditorFactory.getInstance().eventMulticaster.addDocumentListener(documentListener)

    }

    project.getComponent(LibGDXProjectSkinFiles::class.java)?.let { skins ->
      for (skinFile in skins.files) {
        EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(project, skinFile)
      }
    }
  }

  override fun disposeComponent() {

    EditorFactory.getInstance().eventMulticaster.removeDocumentListener(documentListener)

  }

  private fun updateLibGDXLibraryVersions() {

    usedLibraryVersions.clear()

    for (lib in ProjectLibraryTable.getInstance(project).libraryIterator) {
      val urls = lib.getUrls(OrderRootType.CLASSES)
      for (url in urls) {
        for ((gdxLib, mavenCoord) in mavenCoordMap.entries) {
          val regex = Regex("${mavenCoord.first}/${mavenCoord.second}/($versionStringRegex)")
          val matchResult = regex.find(url)
          if (matchResult?.groupValues?.get(1) != null) {
            usedLibraryVersions[gdxLib] = matchResult?.groupValues?.get(1) ?: continue
          }
        }
      }
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

  private val documentListener = object : DocumentAdapter() {

    override fun documentChanged(event: DocumentEvent?) {

      if (project.isDisposed) return

      val document = event?.document ?: return

      val virtualFile = FileDocumentManager.getInstance().getFile(document) ?: return
      val settings = project.getComponent(LibGDXProjectSettings::class.java) ?: return
      val nonSkinFiles = project.getComponent(LibGDXProjectNonSkinFiles::class.java)?.files ?: return

      if (!nonSkinFiles.contains(virtualFile) && !settings.neverAskAboutSkinFiles
      ) {
        EditorNotifications.getInstance(project).updateNotifications(virtualFile)
      }
    }

  }


}

