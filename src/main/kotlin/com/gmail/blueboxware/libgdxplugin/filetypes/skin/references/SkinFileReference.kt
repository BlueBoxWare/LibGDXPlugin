package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*

/*
 * Copyright 2017 Blue Box Ware
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
class SkinFileReference(element: SkinPropertyValue, val baseFile: VirtualFile) : SkinReference<SkinPropertyValue>(element) {

  constructor(element: SkinPropertyValue, baseFile: PsiFile): this(element, baseFile.virtualFile)

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {
    (element.value as? SkinStringLiteral)?.value?.let { fileName ->
      VfsUtil.findRelativeFile(baseFile.parent, fileName)?.let { virtualFile ->
        PsiManager.getInstance(element.project).findFile(virtualFile)?.let { psiFile ->
          return arrayOf(PsiElementResolveResult(psiFile))
        }
      }
    }

    return ResolveResult.EMPTY_ARRAY
  }

}