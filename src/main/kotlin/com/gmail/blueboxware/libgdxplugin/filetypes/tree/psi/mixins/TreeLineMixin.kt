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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeLine
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElement
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElementImpl
import com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.psiUtil.prevSiblingOfSameType

abstract class TreeLineMixin(node: ASTNode) : PsiTreeLine, TreeElementImpl(node) {

    private var level: Int? = null

    override fun isEmpty(): Boolean = statement == null && guardList.isEmpty()

    override fun hasComment(): Boolean = findChildByType<TreeElement>(TreeElementTypes.COMMENT) != null

    override fun level(): Int {
        level?.let { return it }

        var prev = prevSiblingOfSameType()
        while (true) {
            when {
                prev == null -> {
                    level = 0
                    break
                }

                prev.isEmpty() -> {

                }

                prev.indent.textLength == indent.textLength -> {
                    level = prev.level()
                    break
                }

                prev.indent.textLength < indent.textLength -> {
                    level = prev.level() + 1
                    break
                }
            }
            prev = prev.prevSiblingOfSameType()
        }

        return level ?: 0
    }

    override fun subtreeChanged() {
        level = null
    }

}
