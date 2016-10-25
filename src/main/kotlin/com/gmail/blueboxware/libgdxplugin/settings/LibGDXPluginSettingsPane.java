package com.gmail.blueboxware.libgdxplugin.settings;/*
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

import javax.swing.*;

public class LibGDXPluginSettingsPane {
    private JCheckBox showPreviewsOfColorCheckBox;
    private JPanel root;

    private LibGDXPluginSettings settings;

    public JComponent createPanel(LibGDXPluginSettings settings) {

        this.settings = settings;
        return root;
    }

    public void apply() {
        if (settings != null && showPreviewsOfColorCheckBox != null) {
            settings.setEnableColorAnnotations(showPreviewsOfColorCheckBox.isSelected());
        }
    }

    public void reset() {
        if (showPreviewsOfColorCheckBox != null && settings != null) {
            showPreviewsOfColorCheckBox.setSelected(settings.getEnableColorAnnotations());
        }
    }

    public boolean isModified() {
        if (showPreviewsOfColorCheckBox != null && settings != null) {
            return showPreviewsOfColorCheckBox.isSelected() != settings.getEnableColorAnnotations();
        }

        return false;
    }

}
