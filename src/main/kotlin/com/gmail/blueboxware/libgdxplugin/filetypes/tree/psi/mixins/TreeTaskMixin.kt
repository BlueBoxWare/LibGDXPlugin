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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeTask
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElementImpl
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.NlsSafe

abstract class TreeTaskMixin(node: ASTNode) : PsiTreeTask, TreeElementImpl(node) {

    override fun getName(): @NlsSafe String? = taskname?.name

}
