package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult

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
class SkinJavaFieldReference(element: SkinPropertyName) : SkinReference<SkinPropertyName>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {
    element.property?.resolveToField()?.let { field ->
      return arrayOf(PsiElementResolveResult(field))
    }

    return ResolveResult.EMPTY_ARRAY
  }
}