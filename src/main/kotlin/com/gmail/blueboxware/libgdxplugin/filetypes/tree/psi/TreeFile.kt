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
import com.intellij.psi.PsiElement
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.descendantsOfType
import org.jetbrains.kotlin.idea.base.psi.getLineNumber

class TreeFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, TreeLanguage) {

    private var levels: Map<Int, Int>? = null

    private var imports: Map<Int, Map<String, PsiTreeAttribute>>? = null

    override fun getFileType(): FileType = TreeFileType

    override fun toString(): String = TreeLanguage.NAME

    private fun getLevels() = levels ?: calculateLevels()

    private fun getLevel(line: Int) = getLevels().let { it[line] }

    fun getLevel(line: PsiTreeLine) = getLevel(line.getLineNumber())

    fun getImports(upto: PsiElement): Map<String, Pair<Int, PsiTreeAttribute>> {

        if (imports == null) calculateImports()

        val uptoLine = upto.getLineNumber()
        val result = mutableMapOf<String, Pair<Int, PsiTreeAttribute>>()

        imports?.forEach { (line, map) ->
            if (line < uptoLine) {
                map.forEach { (name, attribute) ->
                    result[name] = line to attribute
                }
            }
        }

        return result
    }

    private fun calculateLevels(): Map<Int, Int> {
        val result = mutableMapOf(0 to 0)

        for (line in childrenOfType<PsiTreeLine>()) {
            result[line.getLineNumber()] = line.calcLevel()
        }

        levels = result

        return result
    }

    private fun calculateImports() {
        val result = mutableMapOf<Int, Map<String, PsiTreeAttribute>>()

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
