package com.gmail.blueboxware.libgdxplugin

import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.readAction
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.util.ui.update.DebouncedUpdates
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration.Companion.milliseconds


/*
 * Copyright 2021 Blue Box Ware
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
class GwtOutdatedListener(val project: Project) : BulkFileListener {

    override fun after(events: MutableList<out VFileEvent>) {

        project.service<ProcessingServicce>().add(events)

    }

}

@Suppress("UnstableApiUsage")
@Service(Service.Level.PROJECT)
class ProcessingServicce(project: Project, scope: CoroutineScope) {
    private var balloon: Notification? = null

    private var gwtFile: VirtualFile? = null

    private val queue =
        DebouncedUpdates.forScope<VFileEvent>(scope, "GWT outdated changed files processor", 500.milliseconds)
            .restartTimerOnAdd(true).runBatched { events ->

                var doCheck = false

                for (event in events) {
                    event.file?.let { file ->
                        if (file.name.endsWith(".gwt.xml")) {
                            readAction {
                                VfsUtilCore.findContainingDirectory(file, "html")?.let { html ->
                                    if (ProjectFileIndex.getInstance(project).isInContent(html)) {
                                        if (file.timeStamp >= (gwtFile?.timeStamp ?: 0)) {
                                            gwtFile = file
                                            doCheck = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (doCheck || balloon?.isExpired == false) {
                    gwtFile?.let { gwtFile ->
                        VfsUtilCore.findContainingDirectory(gwtFile, "html")?.let { html ->
                            var outDated = false
                            html.findChild("build")?.let { buildDir ->
                                VfsUtilCore.processFilesRecursively(buildDir) { file ->
                                    if (file.name.endsWith(".java")) {
                                        if (file.timeStamp < gwtFile.timeStamp) {
                                            outDated = true
                                            return@processFilesRecursively false
                                        }
                                    }
                                    true
                                }

                                if (!outDated) {
                                    buildDir.findChild("tmp")?.findChild("war")?.let { warDir ->
                                        VfsUtilCore.processFilesRecursively(warDir) { file ->
                                            if (!file.isDirectory && file.timeStamp < gwtFile.timeStamp) {
                                                outDated = true
                                                return@processFilesRecursively false
                                            }
                                            true
                                        }
                                    }
                                }
                            }
                            balloon?.hideBalloon()
                            balloon = null
                            if (outDated) {
                                balloon =
                                    NotificationGroupManager.getInstance()?.getNotificationGroup("GWT Outdated")
                                        ?.createNotification(
                                            message("gwt.build.outdated.title"),
                                            message("gwt.build.outdated.body"),
                                            NotificationType.WARNING
                                        )
                                balloon?.notify(project)
                            }
                            return@runBatched
                        }

                        balloon?.hideBalloon()
                    }

                }
            }

    fun add(events: MutableList<out VFileEvent>) = events.forEach { queue.queue(it) }

}

