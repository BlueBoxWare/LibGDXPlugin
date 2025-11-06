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

import com.intellij.openapi.components.SerializablePersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State

@Service(Service.Level.PROJECT)
@State(name = "com.gmail.blueboxware.libgdxplugin")
class LibGDXPluginSettings : SerializablePersistentStateComponent<LibGDXPluginSettings.State>(State()) {

    var enableColorAnnotations: Boolean
        get() = state.enableColorAnnotations
        set(value) {
            updateState {
                it.copy(enableColorAnnotations = value)
            }
        }

    var enableColorAnnotationsInJson: Boolean
        get() = state.enableColorAnnotationsInJson
        set(value) {
            updateState {
                it.copy(enableColorAnnotationsInJson = value)
            }
        }

    var enableColorAnnotationsInSkin: Boolean
        get() = state.enableColorAnnotationsInSkin
        set(value) {
            updateState {
                it.copy(enableColorAnnotationsInSkin = value)
            }
        }

    var neverAskAboutJsonFiles: Boolean
        get() = state.neverAskAboutJsonFiles
        set(value) {
            updateState {
                it.copy(neverAskAboutJsonFiles = value)
            }
        }

    var neverAskAboutSkinFiles: Boolean
        get() = state.neverAskAboutSkinFiles
        set(value) {
            updateState {
                it.copy(neverAskAboutSkinFiles = value)
            }
        }

    data class State(
        var enableColorAnnotations: Boolean = true,
        var enableColorAnnotationsInJson: Boolean = true,
        var enableColorAnnotationsInSkin: Boolean = true,
        var neverAskAboutSkinFiles: Boolean = false,
        var neverAskAboutJsonFiles: Boolean = false,
    )

}
