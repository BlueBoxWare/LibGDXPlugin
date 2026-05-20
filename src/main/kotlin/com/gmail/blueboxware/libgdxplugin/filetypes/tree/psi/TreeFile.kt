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
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.parentOfType
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.psi.psiUtil.prevSiblingOfSameType

class TreeFile(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, TreeLanguage) {

    override fun getFileType(): FileType = TreeFileType

    override fun toString(): String = TreeLanguage.NAME

    fun getTree(element: PsiElement): LineTree? =
        if (element is TreeFile) getTree() else (element as? PsiTreeLine)?.let { getTree().findLine(it) }

    fun getTree(): LineTree = CachedValuesManager.getCachedValue(this) {

        var line = lastChild.parentOfType<PsiTreeLine>(true)
        val pending = mutableListOf<Pair<LineTree, Int>>()
        var lastIndent = 0

        while (line != null) {

            if (line.isEmpty() && !line.hasComment()) {
                line = line.prevSiblingOfSameType()
                continue
            }

            val indent = if (line.isEmpty()) {
                lastIndent
            } else {
                lastIndent = line.getIndentSize()
                line.getIndentSize()
            }


            val tree = LineTree(null, line, mutableListOf())

            while (pending.isNotEmpty() && pending.last().second > indent) {
                val child = pending.removeAt(pending.lastIndex).first
                tree.children.add(child)
                child.parent = tree
            }

            pending.add(tree to indent)

            line = line.prevSiblingOfSameType()
        }

        val result = LineTree(null, null, pending.map { it.first }.reversed().toMutableList())
        return@getCachedValue CachedValueProvider.Result.create(result, this)

    }

    fun getImports(upto: PsiElement): Map<String, Pair<Int, PsiTreeAttribute>> {

        val uptoLine = upto.getLineNumber()
        val result = mutableMapOf<String, Pair<Int, PsiTreeAttribute>>()

        calculateImports().forEach { (line, map) ->
            if (line < uptoLine) {
                map.forEach { (name, attribute) ->
                    result[name] = line to attribute
                }
            }
        }

        return result
    }

    fun getLevelsByElement(): Map<PsiTreeLine, Int> = CachedValuesManager.getCachedValue(this) {
        return@getCachedValue CachedValueProvider.Result.create(getTree().getLevelsByElement(), this)
    }

    private fun calculateImports(): Map<Int, Map<String, PsiTreeAttribute>> = CachedValuesManager.getCachedValue(this) {
        val result = mutableMapOf<Int, Map<String, PsiTreeAttribute>>()

        for (import in descendantsOfType<TreeImport>()) {
            result[import.getLineNumber()] = import.calcImports()
        }

        return@getCachedValue CachedValueProvider.Result.create(result, this)

    }

}
