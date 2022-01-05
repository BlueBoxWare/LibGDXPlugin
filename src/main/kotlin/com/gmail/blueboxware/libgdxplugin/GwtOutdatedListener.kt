package com.gmail.blueboxware.libgdxplugin

import com.intellij.ide.highlighter.JavaClassFileType
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent


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
class GwtOutdatedListener(val project: Project) :
    BulkFileListener {

    private var balloon: Notification? = null

    private var gwtFile: VirtualFile? = null

    override fun after(events: MutableList<out VFileEvent>) {

        var doCheck = false

        for (event in events) {
            event.file?.let { file ->
                if (file.name.endsWith(".gwt.xml")) {
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

        if (doCheck || balloon?.isExpired == false) {
            ApplicationManager.getApplication().invokeLater {
                gwtFile?.let { gwtFile ->
                    VfsUtilCore.findContainingDirectory(gwtFile, "html")?.let { html ->
                        var outDated = false
                        html.findChild("build")?.let { buildDir ->
                            VfsUtilCore.processFilesRecursively(buildDir) { file ->
                                if (file.fileType == JavaClassFileType.INSTANCE) {
                                    if (file.timeStamp < gwtFile.timeStamp) {
                                        outDated = true
                                        return@processFilesRecursively false
                                    }
                                }
                                true
                            }
                        }
                        if (!outDated) {
                            html.findChild("war")?.let { warDir ->
                                VfsUtilCore.processFilesRecursively(warDir) { file ->
                                    if (!file.isDirectory && file.timeStamp < gwtFile.timeStamp) {
                                        outDated = true
                                        return@processFilesRecursively false
                                    }
                                    true
                                }
                            }
                        }
                        if (balloon?.isExpired == false) {
                            if (!outDated) {
                                balloon?.hideBalloon()
                                balloon = null
                            }
                        } else {
                            if (outDated) {
                                balloon = NotificationGroupManager
                                    .getInstance()
                                    ?.getNotificationGroup("GWT Outdated")
                                    ?.createNotification(
                                        message("gwt.build.outdated.title"),
                                        message("gwt.build.outdated.body"),
                                        NotificationType.WARNING
                                    )
                                balloon?.notify(project)
                            }
                        }
                        return@invokeLater
                    }
                }

                balloon?.hideBalloon()
            }

        }

    }

}

