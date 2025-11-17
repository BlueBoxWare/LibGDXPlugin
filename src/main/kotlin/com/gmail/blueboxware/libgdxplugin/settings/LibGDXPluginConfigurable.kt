/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.settings

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.resetJsonAssociations
import com.gmail.blueboxware.libgdxplugin.utils.resetSkinAssociations
import com.intellij.ide.ui.UISettings
import com.intellij.openapi.components.service
import com.intellij.openapi.observable.properties.AtomicBooleanProperty
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.EditorNotifications
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import kotlin.reflect.KMutableProperty0

internal class LibGDXPluginConfigurable(private val project: Project) : Configurable {

    private var component: DialogPanel? = null
    private val componentState: LibGDXPluginSettings.State = LibGDXPluginSettings.State()

    override fun getDisplayName(): @NlsContexts.ConfigurableName String = "LibGDXPlugin"

    override fun isModified(): Boolean {
        component?.apply()
        var isModified = false
        process { configurable, component ->
            if (configurable.get() != component.get()) {
                isModified = true
                return@process
            }
        }
        return isModified
    }

    override fun apply() {
        component?.apply()
        process { configurable, component ->
            configurable.set(component.get())
        }
        EditorNotifications.getInstance(project).updateAllNotifications()
        UISettings.getInstance().fireUISettingsChanged()
    }

    override fun reset() {
        process { configurable, component ->
            component.set(configurable.get())
        }
        component?.reset()
    }

    override fun createComponent(): JComponent? {

        val skinButtonEnabled =
            AtomicBooleanProperty(project.service<LibGDXProjectSkinFiles>().files.isNotEmpty() || project.service<LibGDXProjectNonSkinFiles>().files.isNotEmpty())
        val jsonButtonEnabled =
            AtomicBooleanProperty(project.service<LibGDXProjectGdxJsonFiles>().files.isNotEmpty() || project.service<LibGDXProjectNonGdxJsonFiles>().files.isNotEmpty())

        component = panel {
            group("Color Annotations") {
                row {
                    checkBox(message("settings.enable.color.previews")).bindSelected(componentState::enableColorAnnotations)
                }
                row {
                    checkBox(message("settings.enable.color.previews.skin")).bindSelected(componentState::enableColorAnnotationsInSkin)
                }
                row {
                    checkBox(message("settings.enable.color.previews.json")).bindSelected(componentState::enableColorAnnotationsInJson)
                }
            }
            group("Editor Notifications") {
                row {
                    checkBox(message("settings.never.ask.about.skin.files")).bindSelected(componentState::neverAskAboutSkinFiles)
                }
                row {
                    checkBox(message("settings.never.ask.about.json.files")).bindSelected(componentState::neverAskAboutJsonFiles)
                }
            }
            group("File Associations") {
                row {
                    button("Reset Skin File Associations") {
                        if (resetSkinAssociations(project)) {
                            skinButtonEnabled.set(false)
                        }
                    }.enabledIf(skinButtonEnabled)
                    button("Reset JSON File Associations") {
                        if (resetJsonAssociations(project)) {
                            jsonButtonEnabled.set(false)
                        }
                    }.enabledIf(jsonButtonEnabled)
                }
            }
        }

        return component

    }

    private fun process(f: (KMutableProperty0<Boolean>, KMutableProperty0<Boolean>) -> Unit) {
        val configurable = project.service<LibGDXPluginSettings>()
        val map: Map<KMutableProperty0<Boolean>, KMutableProperty0<Boolean>> = mapOf(
            configurable::enableColorAnnotations to componentState::enableColorAnnotations,
            configurable::enableColorAnnotationsInJson to componentState::enableColorAnnotationsInJson,
            configurable::enableColorAnnotationsInSkin to componentState::enableColorAnnotationsInSkin,
            configurable::neverAskAboutSkinFiles to componentState::neverAskAboutSkinFiles,
            configurable::neverAskAboutJsonFiles to componentState::neverAskAboutJsonFiles,
        )
        map.forEach { (p1, p2) ->
            f(p1, p2)
        }
    }
}
