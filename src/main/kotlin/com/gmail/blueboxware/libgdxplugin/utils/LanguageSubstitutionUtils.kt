package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectGdxJsonFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonGdxJsonFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonSkinFiles
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectSkinFiles
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.impl.FoldingModelImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.LanguageSubstitutors
import com.intellij.ui.EditorNotifications
import com.intellij.util.FileContentUtilCore
import com.intellij.util.indexing.FileBasedIndex
import javax.swing.JComponent
import kotlin.reflect.KClass


/*
 * Copyright 2019 Blue Box Ware
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

internal fun resetSkinAssociations(project: Project, component: JComponent) =
    resetAssociations(
        project,
        component,
        LibGDXProjectSkinFiles::class,
        LibGDXProjectNonSkinFiles::class,
        "Skin"
    )

internal fun resetJsonAssociations(project: Project, component: JComponent) =
    resetAssociations(
        project,
        component,
        LibGDXProjectGdxJsonFiles::class,
        LibGDXProjectNonGdxJsonFiles::class,
        "LibGDX JSON"
    )


internal fun Project.markFileAsSkin(file: VirtualFile) {
    changeFileSubstitution(
        file,
        listOf(LibGDXProjectNonSkinFiles::class, LibGDXProjectGdxJsonFiles::class),
        LibGDXProjectSkinFiles::class
    )
}

internal fun Project.markFileAsNonSkin(file: VirtualFile) =
    changeFileSubstitution(
        file,
        listOf(LibGDXProjectSkinFiles::class),
        LibGDXProjectNonSkinFiles::class
    )

internal fun Project.markFileAsGdxJson(file: VirtualFile) {
    changeFileSubstitution(
        file,
        listOf(LibGDXProjectNonGdxJsonFiles::class, LibGDXProjectSkinFiles::class),
        LibGDXProjectGdxJsonFiles::class
    )
}

internal fun Project.markFileAsNonGdxJson(file: VirtualFile) =
    changeFileSubstitution(
        file,
        listOf(LibGDXProjectGdxJsonFiles::class),
        LibGDXProjectNonGdxJsonFiles::class
    )

private fun Project.changeFileSubstitution(
    file: VirtualFile,
    from: Collection<KClass<out PersistentFileSetManager>>,
    to: KClass<out PersistentFileSetManager>
) {

    if (isDisposed) {
        return
    }

    for (fromFile in from) {
        @Suppress("IncorrectServiceRetrieving")
        getService(fromFile.java)?.remove(file)
    }

    @Suppress("IncorrectServiceRetrieving")
    val toFiles = getService(to.java)

    toFiles.add(file)

    reset(file)
}

private fun Project.reset(file: VirtualFile) {
    LanguageUtil.getFileLanguage(file)?.let { currentLanguage ->
        LanguageSubstitutors.getInstance().substituteLanguage(currentLanguage, file, this)
    }

    DaemonCodeAnalyzer.getInstance(this).restart()
    FileBasedIndex.getInstance().requestReindex(file)
    FileContentUtilCore.reparseFiles(file)

    FileDocumentManager.getInstance().getDocument(file)?.let { document ->
        EditorFactory.getInstance().getEditors(document).forEach { editor ->
            (editor.foldingModel as? FoldingModelImpl)?.rebuild()
        }
    }

    EditorNotifications.getInstance(this).updateNotifications(file)
}

private fun resetAssociations(
    project: Project,
    component: JComponent,
    set1: KClass<out PersistentFileSetManager>,
    set2: KClass<out PersistentFileSetManager>,
    type: String
) {

    if (project == ProjectManager.getInstance().defaultProject) {
        Messages.showWarningDialog(
            component,
            "Cannot determine active project.",
            "Cannot Determine Active Project"
        )
        return
    }

    val result = Messages.showOkCancelDialog(
        component,
        "Reset all files marked as $type to their original file type for project '${project.name}'?",
        "Reset $type associations?",
        "Reset",
        "Cancel",
        null
    )

    if (result == Messages.OK) {

        val filesChanged = mutableSetOf<String>()

        @Suppress("IncorrectServiceRetrieving")
        project.getService(set1.java)?.let {
            it.files.forEach { file ->
                filesChanged.add(file)
            }
            it.removeAll()
        }

        @Suppress("IncorrectServiceRetrieving")
        project.getService(set2.java)?.let {
            it.files.forEach { file ->
                filesChanged.add(file)
            }
            it.removeAll()
        }

        val vfManager = VirtualFileManager.getInstance()
        filesChanged.forEach { url ->
            vfManager.findFileByUrl(url)?.let { file ->
                project.reset(file)
            }
        }

    }

}

