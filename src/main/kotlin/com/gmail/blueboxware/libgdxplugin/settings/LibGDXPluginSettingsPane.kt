package com.gmail.blueboxware.libgdxplugin.settings

import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

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
class LibGDXPluginSettingsPane {

  lateinit private var root: JPanel
  lateinit private var showPreviewsOfColorCheckBox: JCheckBox
  lateinit private var neverAskAboutSkinFiles: JCheckBox

  private var settings = LibGDXPluginSettings()

  fun createPanel(settings: LibGDXPluginSettings): JComponent {
    this.settings = settings
    return root
  }

  fun apply() {
    settings.enableColorAnnotations = showPreviewsOfColorCheckBox.isSelected
    settings.neverAskAboutSkinFiles = neverAskAboutSkinFiles.isSelected
  }

  fun reset() {
    showPreviewsOfColorCheckBox.isSelected = settings.enableColorAnnotations
    neverAskAboutSkinFiles.isSelected = settings.neverAskAboutSkinFiles
  }

  fun isModified(): Boolean {
    return showPreviewsOfColorCheckBox.isSelected != settings.enableColorAnnotations
      || neverAskAboutSkinFiles.isSelected != settings.neverAskAboutSkinFiles
  }
}