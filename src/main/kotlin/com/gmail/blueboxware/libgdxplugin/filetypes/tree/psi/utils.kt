/*
 * Copyright 2026 Blue Box Ware
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

import org.jetbrains.kotlin.idea.base.psi.getLineNumber

class LineTree(var parent: LineTree?, val line: PsiTreeLine?, val children: MutableList<LineTree>) {

    fun findLine(l: PsiTreeLine): LineTree? {
        if (l == line) return this
        for (child in children) {
            val c = child.findLine(l)
            if (c != null) return c
        }
        return null
    }

    override fun toString(): String {
        val name = line?.statement?.task?.name ?: line?.text ?: "<empty>"
        val open = if (children.isEmpty()) "" else " ("
        val close = if (children.isEmpty()) "" else ")"
        return name + open + children.joinToString(", ") + close
    }

}

fun LineTree.getLevelsByElement(level: Int = -1): Map<PsiTreeLine, Int> {
    val result = mutableMapOf<PsiTreeLine, Int>()
    line?.let {
        result[it] = level
    }

    for (child in children) {
        result.putAll(child.getLevelsByElement(level + 1))
    }

    return result
}

fun Map<PsiTreeLine, Int>.getLevelsByLineNumber(): Map<Int, Int> =
    entries.associate { (line, level) -> line.getLineNumber() to level }
