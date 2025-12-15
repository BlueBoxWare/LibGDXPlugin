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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeVstring
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeValue
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassListReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceSet

abstract class TreeStringMixin(node: ASTNode) : PsiTreeVstring, TreeElementImpl(node) {

    override fun getValue(): String = text.trim('"')

    override fun getReferences(): Array<out PsiReference?> {

        if (parent is TreeValue && parent.parent.parent is TreeImport) {
            val v = JavaClassReferenceSet(getValue(), this, 1, false, JavaClassListReferenceProvider().apply {
                setOption(JavaClassReferenceProvider.RESOLVE_QUALIFIED_CLASS_NAME, true)
            }).references
            return v
        }

        return super.getReferences()
    }

}
