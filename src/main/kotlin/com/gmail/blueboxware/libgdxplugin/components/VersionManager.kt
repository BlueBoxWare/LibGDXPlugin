package com.gmail.blueboxware.libgdxplugin.components

import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTable
import com.intellij.util.Alarm
import com.intellij.util.text.DateFormatUtil
import org.jetbrains.kotlin.config.MavenComparableVersion

/*
 * Copyright 2017 Blue Box Ware
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
class VersionManager(project: Project) : AbstractProjectComponent(project) {

  companion object {

    val LOG = Logger.getInstance("#" + VersionManager::class.java.name)

    val LIBRARY_CHANGED_TIME_OUT = 30 * DateFormatUtil.SECOND

    var BATCH_SIZE = 7

    var SCHEDULED_UPDATE_INTERVAL = 15 * DateFormatUtil.MINUTE

  }

  val isLibGDXProject: Boolean
    get() = getUsedVersion(Libraries.LIBGDX) != null

  fun getUsedVersion(library: Libraries): MavenComparableVersion? = usedVersions[library]

  fun getLatestVersion(library: Libraries): MavenComparableVersion? = library.library.getLatestVersion(this)

  private val usedVersions = mutableMapOf<Libraries, MavenComparableVersion>()

  private val updateLatestVersionsAlarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, project)

  override fun projectOpened() {
    updateUsedVersions()
    if (isLibGDXProject) {
      Libraries.LIBGDX.library.updateLatestVersion(this, true)
      updateLatestVersions()
      updateLatestVersionsAlarm.addRequest({ scheduleUpdateLatestVersions() }, 2 * DateFormatUtil.MINUTE)
    }

    ServiceManager.getService(myProject, ProjectLibraryTable::class.java)?.addListener(libraryListener)

  }

  override fun projectClosed() {
    updateLatestVersionsAlarm.cancelAllRequests()

    ServiceManager.getService(myProject, ProjectLibraryTable::class.java)?.removeListener(libraryListener)
  }


  private fun updateLatestVersions() {
    var networkCount = 0

    Libraries.values().sortedBy { it.library.lastUpdated }.forEach { lib ->
      val networkAllowed = networkCount < BATCH_SIZE && usedVersions[lib] != null
      VersionManager.LOG.debug("Updating latest version of ${lib.library.name}. Network allowed: $networkAllowed.")
      if (lib.library.updateLatestVersion(this, networkAllowed)) {
        networkCount++
      }
    }

  }

  fun updateUsedVersions() {
    usedVersions.clear()

    ServiceManager.getService(myProject, ProjectLibraryTable::class.java)?.libraryIterator?.let { libraryIterator ->
      for (lib in libraryIterator) {
        Libraries.extractLibraryInfoFromIdeaLibrary(lib)?.let { (libraries, version) ->
          usedVersions[libraries] = version
        }
      }
    }

  }

  private fun scheduleUpdateLatestVersions() {
    updateLatestVersionsAlarm.cancelAllRequests()
    updateLatestVersions()
    updateLatestVersionsAlarm.addRequest({
      LOG.debug("Scheduled update of latest versions")
      scheduleUpdateLatestVersions()
    }, SCHEDULED_UPDATE_INTERVAL)
  }

  private val libraryListener = object: LibraryTable.Listener {
    override fun beforeLibraryRemoved(library: Library) {
    }

    override fun afterLibraryRenamed(library: Library) {
      updateUsedVersions()
    }

    override fun afterLibraryAdded(newLibrary: Library) {
      updateUsedVersions()
      updateLatestVersionsAlarm.cancelAllRequests()
      updateLatestVersionsAlarm.addRequest({ scheduleUpdateLatestVersions() }, LIBRARY_CHANGED_TIME_OUT)
    }

    override fun afterLibraryRemoved(library: Library) {
      updateUsedVersions()
    }
  }


}
