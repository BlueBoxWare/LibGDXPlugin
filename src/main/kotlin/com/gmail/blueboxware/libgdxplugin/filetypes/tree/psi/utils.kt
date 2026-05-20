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

import com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.idea.base.psi.getLineNumber
import org.jetbrains.kotlin.psi.psiUtil.nextSiblingOfSameType
import org.jetbrains.kotlin.psi.psiUtil.prevSiblingOfSameType

class LineTree(var parent: LineTree?, val line: PsiTreeLine?, val children: MutableList<LineTree>) {

    fun accept(visitor: LineTreeVisitor) {
        visitor.visit(this)
    }

    fun textRange(): TextRange? {
        val start = line?.textRange?.startOffset ?: children.firstOrNull()?.textRange()?.startOffset ?: return null
        val end = children.lastOrNull()?.textRange()?.endOffset ?: line?.textRange?.endOffset ?: return null
        return TextRange(start, end)
    }

    fun findLine(l: PsiTreeLine): LineTree? {
        if (l == line) return this
        for (child in children) {
            val c = child.findLine(l)
            if (c != null) return c
        }
        return null
    }

    fun realChildren(): Int = children.filter { it.line?.isEmpty() != true }.size

    override fun toString(): String {
        val name = line?.statement?.task?.name ?: line?.text ?: "<empty>"
        val open = if (children.isEmpty()) "" else " ("
        val close = if (children.isEmpty()) "" else ")"
        return name + open + children.joinToString(", ") + close
    }

}

fun interface LineTreeVisitor {

    fun process(line: PsiTreeLine)

    fun visit(tree: LineTree) {
        tree.line?.let {
            process(it)
        }
        for (line in tree.children) {
            visit(line)
        }
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

fun PsiTreeLine.previousLine(includeComments: Boolean, includeEmpty: Boolean): PsiTreeLine? =
    sibling(this, true, includeComments, includeEmpty)

fun PsiTreeLine.nextLine(includeComments: Boolean, includeEmpty: Boolean): PsiTreeLine? =
    sibling(this, false, includeComments, includeEmpty)

private fun sibling(
    line: PsiTreeLine,
    reverse: Boolean,
    includeComments: Boolean,
    includeEmpty: Boolean
): PsiTreeLine? {
    var l: PsiTreeLine = line
    while (true) {
        l = (if (reverse) l.prevSiblingOfSameType() else l.nextSiblingOfSameType()) ?: return null
        if (l.isEmpty() && !l.hasComment() && includeEmpty) {
            return l
        } else if (l.isEmpty() && l.hasComment() && includeComments) {
            return l
        } else if (!l.isEmpty()) {
            return l
        }
    }
}
