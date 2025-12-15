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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.descendantsOfType
import org.jetbrains.kotlin.idea.base.psi.getLineNumber

class TreeFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, TreeLanguage) {

    private var levels: Map<Int, Int>? = null

    private var imports: Map<Int, Map<String, String?>>? = null

    override fun getFileType(): FileType = TreeFileType

    override fun toString(): String = TreeLanguage.NAME

    fun getLevels() = levels ?: calculateLevels()

    fun getLevel(line: Int) = levels?.let { it[line] } ?: calculateLevels()[line]

    fun getLevel(line: PsiTreeLine) = getLevel(line.getLineNumber())

    fun getImports(uptoLine: Int): Map<String, Pair<Int, String?>> {

        if (imports == null) calculateImports()

        val result = mutableMapOf<String, Pair<Int, String?>>()

        imports?.forEach { (line, map) ->
            if (line < uptoLine) {
                map.forEach { (name, fqn) ->
                    result[name] = line to fqn
                }
            }
        }

        return result
    }

    private fun calculateLevels(): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>(0 to 0)

        for (line in childrenOfType<PsiTreeLine>()) {
            result[line.getLineNumber()] = line.calcLevel()
        }

        levels = result

        return result
    }

    private fun calculateImports() {
        val result = mutableMapOf<Int, Map<String, String?>>()

        for (import in descendantsOfType<TreeImport>()) {
            result[import.getLineNumber()] = import.calcImports()
        }

        imports = result

    }

    override fun clearCaches() {
        levels = null
        imports = null
        super.clearCaches()
    }

}
