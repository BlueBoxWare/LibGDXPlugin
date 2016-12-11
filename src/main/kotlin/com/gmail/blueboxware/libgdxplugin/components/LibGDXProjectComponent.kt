package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.json.JsonFileType
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerAdapter
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTable
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileAdapter
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.jsonSchema.JsonSchemaFileType
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

  private val SKIN_FILE_REGEX = Regex("""com\.badlogic\.gdx[a-zA-Z.]+\s*:\s*\{""")

  private val usedLibraryVersions = mutableMapOf<GDXLibrary, String>()
  private val latestLibraryVersions = mutableMapOf<GDXLibrary, String>()

  private var latestLibraryVersionLastChecked = -1

  private val temporaryMarkedAsPlainText = mutableListOf<VirtualFile>()

  private val myFileChangeListener = object : VirtualFileAdapter() {
    override fun contentsChanged(event: VirtualFileEvent) {
      if (isJSON(event.file)) {
        makePlainTextIfSkin(event.file)
      } else if (temporaryMarkedAsPlainText.contains(event.file)) {
        if (!looksLikeSkinFile(event.file)) {
          EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(project, event.file)
        }
      }
    }

    override fun fileCreated(event: VirtualFileEvent) {
      if (isJSON(event.file)) {
        makePlainTextIfSkin(event.file)
      }
    }

  }

  val isLibGDXProject: Boolean
    get() { return usedLibraryVersions[GDXLibrary.GDX] != null || isTesting }


  // For testing only
  var isTesting = false

  override fun getComponentName() = "LibGDXProjectComponent"

  fun getUsedLibraryVersion(gdxLibrary: GDXLibrary) = usedLibraryVersions[gdxLibrary]

  override fun initComponent() { }

  override fun projectClosed() {  }

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
  }

  override fun disposeComponent() { }

  private fun installSkinFileSupport() {

    ProjectManager.getInstance().addProjectManagerListener(project, object : ProjectManagerAdapter() {
      override fun projectClosing(project: Project?) {
        removeSkinFileSupport()
        project?.save()
      }
    })

    if (ServiceManager.getService(project, LibGDXPluginSettings::class.java)?.disableJsonDiagnosticsForSkins == true) {
      val jsonFiles = mutableListOf<VirtualFile>()
      try {
        jsonFiles.addAll(FileTypeIndex.getFiles(JsonSchemaFileType.INSTANCE, GlobalSearchScope.projectScope(project)))
      } catch (e: NoClassDefFoundError) {
        // Do nothing
      }
      try {
        jsonFiles.addAll(FileTypeIndex.getFiles(JsonFileType.INSTANCE, GlobalSearchScope.projectScope(project)))
      } catch (e: NoClassDefFoundError) {
        // Do nothing
      }

      for (jsonFile in jsonFiles) {
        makePlainTextIfSkin(jsonFile)
      }

      VirtualFileManager.getInstance().addVirtualFileListener(myFileChangeListener)
    }

  }

  private fun removeSkinFileSupport() {
    EnforcedPlainTextFileTypeManager.getInstance().resetOriginalFileType(project, *temporaryMarkedAsPlainText.toTypedArray())
    VirtualFileManager.getInstance().removeVirtualFileListener(myFileChangeListener)
  }

  private fun makePlainTextIfSkin(file: VirtualFile) {
    if (looksLikeSkinFile(file)) {
      EnforcedPlainTextFileTypeManager.getInstance().markAsPlainText(project, file)
      temporaryMarkedAsPlainText.add(file)
    }
  }

  private fun isJSON(file: VirtualFile): Boolean {
    try {
      if (file.fileType == JsonSchemaFileType.INSTANCE) {
        return true
      }
    } catch (e: NoClassDefFoundError) {
      // Do nothing
    }

    try {
      if (file.fileType == JsonFileType.INSTANCE) {
        return true
      }
    } catch (e: NoClassDefFoundError) {
      // Do nothing
    }

    return false
  }

  private fun looksLikeSkinFile(file: VirtualFile): Boolean {

   try {
      if (file.inputStream.reader().readText().contains(SKIN_FILE_REGEX)) {
        return true
      }
    } catch (e: IOException) {
      // Do nothing
    }

    return false
  }

  private fun updateLibGDXLibraryVersions() {

    usedLibraryVersions.clear()

    val wasLibGDXProject = isLibGDXProject

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

    if (!wasLibGDXProject && isLibGDXProject) {
      installSkinFileSupport()
    } else if (wasLibGDXProject && !isLibGDXProject) {
      removeSkinFileSupport()
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

