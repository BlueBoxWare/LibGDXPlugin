package com.gmail.blueboxware.libgdxplugin.versions

import com.gmail.blueboxware.libgdxplugin.utils.findClasses
import com.gmail.blueboxware.libgdxplugin.utils.getLibraryInfoFromIdeaLibrary
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTable
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.psi.PsiLiteralExpression
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
@Service
class VersionService(val project: Project): Disposable {

  fun isLibGDXProject() = getUsedVersion(Libraries.LIBGDX) != null

  fun getUsedVersion(library: Libraries): MavenComparableVersion? = usedVersions[library]

  fun getLatestVersion(library: Libraries): MavenComparableVersion? = library.library.getLatestVersion(this)

  private val usedVersions = mutableMapOf<Libraries, MavenComparableVersion>()

  private val updateLatestVersionsAlarm = Alarm(Alarm.ThreadToUse.POOLED_THREAD, this)

  fun projectOpened() {
    updateUsedVersions {
      if (isLibGDXProject()) {
        Libraries.LIBGDX.library.updateLatestVersion(this, true)
        updateLatestVersions()
        updateLatestVersionsAlarm.addRequest({ scheduleUpdateLatestVersions() }, 2 * DateFormatUtil.MINUTE)
      }
    }

    LibraryTablesRegistrar.getInstance().getLibraryTable(project).addListener(libraryListener)

  }

  fun projectClosed() {
    updateLatestVersionsAlarm.cancelAllRequests()

    LibraryTablesRegistrar.getInstance().getLibraryTable(project).removeListener(libraryListener)
  }

  override fun dispose() {
  }

  private fun updateLatestVersions() {
    var networkCount = 0

    Libraries.values().sortedBy { it.library.lastUpdated }.forEach { lib ->
      val networkAllowed = networkCount < BATCH_SIZE && usedVersions[lib] != null
      if (lib.library.updateLatestVersion(this, networkAllowed)) {
        LOG.debug("Updated latest version of ${lib.library.name}.")
        networkCount++
      }
    }

  }

  fun updateUsedVersions(doAfterUpdate: (() -> Unit)? = null) {

    DumbService.getInstance(project).runWhenSmart {

      LOG.debug("Updating used library versions")

      usedVersions.clear()

      LibraryTablesRegistrar.getInstance().getLibraryTable(project).libraryIterator.let { libraryIterator ->
        for (lib in libraryIterator) {
          getLibraryInfoFromIdeaLibrary(lib)?.let { (libraries, version) ->
            usedVersions[libraries].let { registeredVersion ->
              if (registeredVersion == null || registeredVersion < version) {
                usedVersions[libraries] = version
              }
            }
          }
        }
      }

      if (usedVersions[Libraries.LIBGDX] == null) {
        project.findClasses("com.badlogic.gdx.Version").forEach { psiClass ->
          ((psiClass.findFieldByName("VERSION", false)?.initializer as? PsiLiteralExpression)?.value as? String)
                  ?.let(::MavenComparableVersion)
                  ?.let { usedVersions[Libraries.LIBGDX] = it }
        }
      }

      if (isLibGDXProject()) {
        LOG.debug("libGDX detected")
      } else {
        LOG.debug("No libGDX detected")
      }

      doAfterUpdate?.invoke()

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

    override fun afterLibraryAdded(newLibrary: Library) {
      updateUsedVersions()
      updateLatestVersionsAlarm.cancelAllRequests()
      updateLatestVersionsAlarm.addRequest({ scheduleUpdateLatestVersions() }, LIBRARY_CHANGED_TIME_OUT)
    }

    override fun afterLibraryRemoved(library: Library) {
      updateUsedVersions()
    }
  }

  companion object {

    val LOG = Logger.getInstance("#" + VersionService::class.java.name)

    var LIBRARY_CHANGED_TIME_OUT = 30 * DateFormatUtil.SECOND

    var BATCH_SIZE = 20

    var SCHEDULED_UPDATE_INTERVAL = 15 * DateFormatUtil.MINUTE

  }

}
