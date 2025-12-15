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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.PsiTreeTaskImpl
import com.intellij.lang.ASTNode

abstract class TreeImportMixin(node: ASTNode) : PsiTreeImport, PsiTreeTaskImpl(node) {

    override fun calcImports(): Map<String, String?> {
        val result = mutableMapOf<String, String?>()

        for (attribute in attributeList) {
            val key = attribute.attributeName.text
            val value = attribute.value.vstring?.getValue() ?: continue

            result[key] = value
        }
        return result
    }

}
