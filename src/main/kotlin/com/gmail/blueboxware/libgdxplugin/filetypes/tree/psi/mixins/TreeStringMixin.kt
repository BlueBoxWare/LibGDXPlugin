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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.getClassReferences
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeVstring
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeValue
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.PsiTreeVstringImpl
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiReference

abstract class TreeStringMixin(node: ASTNode) : PsiTreeVstring, TreeElementImpl(node) {

    override fun getValue(): String = text.trim('"').trimEnd('?', '\t', ' ')

    override fun getName(): String? = getValue()

    override fun getReferences(): Array<out PsiReference?> {

        if (parent is TreeValue && parent.parent.parent is TreeImport) {
            return getClassReferences(getValue(), 1)
        }

        return super.getReferences()
    }

    override fun setName(name: @NlsSafe String): PsiTreeVstringImpl? {
        TreeElementFactory.createString(project, name)?.let {
            replace(it)
            return it
        }
        return null
    }

    override fun setName(newName: String, range: TextRange): PsiTreeVstringImpl? {
        val newContent = text.replaceRange(range.startOffset, range.endOffset, newName)
        TreeElementFactory.createString(project, newContent, false)?.let {
            replace(it)
            return it
        }
        return null
    }


}
