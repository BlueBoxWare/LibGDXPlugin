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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage.ID
import com.intellij.lang.Language
import com.intellij.openapi.util.NlsSafe

object TreeLanguage : Language(ID) {
    private fun readResolve(): Any = TreeLanguage

    const val ID = "LibGDXAiTree"
    const val NAME = "libGDX Behaviour Tree"

    override fun isCaseSensitive(): Boolean = true

    override fun getDisplayName(): @NlsSafe String = NAME

    override fun getID(): @NlsSafe String = ID

}
