package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectNonGdxJsonFiles
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.json.JsonLanguage
import com.intellij.json.psi.*
import com.intellij.lang.Language
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.EditorNotificationPanel


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
class GdxJsonEditorNotificationProvider(project: Project) : FileTypeEditorNotificationProvider(
    project,
    LibGDXSkinLanguage.INSTANCE
) {

    override val messageKey = "json.file.detected"

    override fun onYes(file: VirtualFile) = project.markFileAsGdxJson(file)

    override fun onNo(file: VirtualFile) = project.markFileAsNonGdxJson(file)

    override fun onNever(settings: LibGDXPluginSettings) {
        settings.neverAskAboutJsonFiles = true
    }

    override fun shouldShowNotification(
        currentLanguage: Language?,
        file: VirtualFile,
        fileEditor: TextEditor,
        settings: LibGDXPluginSettings
    ): Boolean =
        showNotification(project, currentLanguage, file, settings)

    override fun getKey(): Key<EditorNotificationPanel> = KEY

    companion object {
        val KEY = key<EditorNotificationPanel>("json.file.detected")

        fun showNotification(
            project: Project,
            currentLanguage: Language?,
            file: VirtualFile,
            settings: LibGDXPluginSettings
        ): Boolean {

            if (settings.neverAskAboutJsonFiles) {
                return false
            } else if (currentLanguage != PlainTextLanguage.INSTANCE && currentLanguage != JsonLanguage.INSTANCE) {
                return false
            } else if (currentLanguage == LibGDXSkinLanguage.INSTANCE) {
                return false
            } else {

                val nonGdxJsonFiles = project.getComponent(LibGDXProjectNonGdxJsonFiles::class.java)

                if (nonGdxJsonFiles.contains(file)) {
                    return false
                } else {

                    if (currentLanguage == JsonLanguage.INSTANCE) {

                        var count = 0

                        (PsiManager.getInstance(project).findFile(file) as? JsonFile)
                            ?.childrenOfType<JsonValue>()
                            ?.forEach { value ->
                                if (value is JsonStringLiteral || value is JsonReferenceExpression) {

                                    if (!JsonPsiUtil.getElementTextWithoutHostEscaping(value).startsWith("\"")) {
                                        count++
                                    }

                                    if (count > 5) {
                                        return true
                                    }
                                }
                            }

                    }


                }
            }

            return false

        }

    }
}
