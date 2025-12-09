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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

@Suppress("PropertyName")
class TreeCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(TreeLanguage.id, container) {

    @JvmField
    var KEEP_INDENT = false

    @JvmField
    var KEEP_COMMENTS = false

    @JvmField
    var KEEP_INDENTS_ON_EMPTY_LINES = false
}
