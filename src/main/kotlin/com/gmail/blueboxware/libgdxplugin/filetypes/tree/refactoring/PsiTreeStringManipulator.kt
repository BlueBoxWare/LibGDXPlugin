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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.refactoring

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.PsiTreeVstringImpl
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

internal class PsiTreeStringManipulator : AbstractElementManipulator<PsiTreeVstringImpl>() {

    override fun handleContentChange(
        string: PsiTreeVstringImpl, range: TextRange, newName: String?
    ): PsiTreeVstringImpl? = newName?.let { string.setName(it, range) }

}
