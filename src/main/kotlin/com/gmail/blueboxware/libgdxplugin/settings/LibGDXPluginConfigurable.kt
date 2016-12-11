package com.gmail.blueboxware.libgdxplugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

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

@State(name = "LibGDXPluginConfigurable")
class LibGDXPluginConfigurable(val project: Project) : Configurable {

  private var form: LibGDXPluginSettingsPane? = null

  override fun isModified() = getForm()?.isModified ?: false

  override fun disposeUIResources() {
    form = null
  }

  override fun getDisplayName() = "LibGDXPlugin"

  override fun apply() {
    getForm()?.apply()
  }

  override fun createComponent(): JComponent? {
    val settings = ServiceManager.getService(project, LibGDXPluginSettings::class.java)
    return getForm()?.createPanel(settings)
  }

  override fun reset() {
    getForm()?.reset()
  }

  override fun getHelpTopic(): String? = null

  private fun getForm(): LibGDXPluginSettingsPane? {
    if (form == null) {
      form = LibGDXPluginSettingsPane()
    }
    return form
  }

}

@State(name = "LibGDXPluginSettings")
class LibGDXPluginSettings: PersistentStateComponent<LibGDXPluginSettings> {
  var enableColorAnnotations: Boolean = true
  var disableJsonDiagnosticsForSkins: Boolean = true

  override fun loadState(state: LibGDXPluginSettings?) {
    enableColorAnnotations = state?.enableColorAnnotations ?: true
    disableJsonDiagnosticsForSkins = state?.disableJsonDiagnosticsForSkins ?: true
  }

  override fun getState() = this
}

