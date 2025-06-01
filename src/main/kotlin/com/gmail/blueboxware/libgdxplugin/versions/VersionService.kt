package com.gmail.blueboxware.libgdxplugin.versions

import com.gmail.blueboxware.libgdxplugin.utils.SkinTagsModificationTracker
import com.gmail.blueboxware.libgdxplugin.utils.findClasses
import com.gmail.blueboxware.libgdxplugin.utils.getLibraryInfoFromIdeaLibrary
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTable
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.psi.PsiLiteralExpression
import com.intellij.util.concurrency.AppExecutorUtil
import com.jetbrains.rd.util.Callable
import kotlinx.coroutines.*
import org.jetbrains.kotlin.config.MavenComparableVersion
import java.util.concurrent.TimeUnit

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
@Service(Service.Level.PROJECT)
class VersionService(val project: Project, val coroutineScope: CoroutineScope) : Disposable {

    fun isLibGDXProject() = getUsedVersion(Libraries.LIBGDX) != null

    fun getUsedVersion(library: Libraries): MavenComparableVersion? = usedVersions[library]

    fun getLatestVersion(library: Libraries): MavenComparableVersion? = library.library.getLatestVersion(this)

    private val usedVersions = mutableMapOf<Libraries, MavenComparableVersion>()

    private var currentJob: Job? = null

    private fun schedule(delayMilliSeconds: Long) {
        currentJob?.cancel()
        currentJob = coroutineScope.launch {
            delay(delayMilliSeconds)
            while (true) {
                if (project.isLibGDXProject()) {
                    updateLatestVersions()
                    delay(SCHEDULED_UPDATE_INTERVAL)
                }
            }
        }
    }

    fun projectOpened() {
        updateUsedVersions {
            if (isLibGDXProject()) {
                coroutineScope.launch {
                    Libraries.LIBGDX.library.updateLatestVersion(this@VersionService, true)
                }
                schedule(LIBRARY_CHANGED_TIME_OUT)
            }
        }

        LibraryTablesRegistrar.getInstance().getLibraryTable(project).addListener(libraryListener)

    }

    fun projectClosed() {
        LibraryTablesRegistrar.getInstance().getLibraryTable(project).removeListener(libraryListener)
    }

    override fun dispose() {
    }

    private suspend fun updateLatestVersions() {
        var networkCount = 0

        Libraries.entries.sortedBy { it.library.lastUpdated }.forEach { lib ->
            yield()
            val networkAllowed = networkCount < BATCH_SIZE && usedVersions[lib] != null
            if (lib.library.updateLatestVersion(this, networkAllowed)) {
                LOG.debug("Updated latest version of ${lib.library.name}.")
                networkCount++
            }
        }

    }

    fun updateUsedVersions(doAfterUpdate: (() -> Unit)? = null) {

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
            val callable = Callable {
                project.findClasses("com.badlogic.gdx.Version").forEach { psiClass ->
                    ((psiClass.findFieldByName(
                        "VERSION", false
                    )?.initializer as? PsiLiteralExpression)?.value as? String)?.let(::MavenComparableVersion)
                        ?.let { usedVersions[Libraries.LIBGDX] = it }
                }
            }
            if (ApplicationManager.getApplication().isUnitTestMode) {
                DumbService.getInstance(project).runWhenSmart { callable.call() }
            } else {
                DumbService.getInstance(project).smartInvokeLater {
                    ReadAction.nonBlocking(callable).inSmartMode(project).coalesceBy(this).expireWith(this)
                        .submit(AppExecutorUtil.getAppExecutorService())
                }
            }
        }

        if (isLibGDXProject()) {
            SkinTagsModificationTracker.getInstance(project).incModificationCount()
            LOG.debug("libGDX detected")
        } else {
            LOG.debug("No libGDX detected")
        }

        doAfterUpdate?.invoke()

    }

    private val libraryListener = object : LibraryTable.Listener {
        override fun beforeLibraryRemoved(library: Library) {
        }

        override fun afterLibraryAdded(newLibrary: Library) {
            updateUsedVersions()
            schedule(LIBRARY_CHANGED_TIME_OUT)
        }

        override fun afterLibraryRemoved(library: Library) {
            updateUsedVersions()
        }
    }

    companion object {

        val LOG = Logger.getInstance(VersionService::class.java)

        var LIBRARY_CHANGED_TIME_OUT = TimeUnit.SECONDS.toMillis(30)
        var SCHEDULED_UPDATE_INTERVAL = TimeUnit.MINUTES.toMillis(15)

        var BATCH_SIZE = 20

    }

}