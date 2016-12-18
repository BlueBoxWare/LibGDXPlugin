package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

/*
 * Copyright 2016 Blue Box Ware
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
abstract class SkinLiteralMixin(node: ASTNode) : SkinElementImpl(node), SkinLiteral {

  private val refLock = Object()
  private var references: Array<PsiReference>? = null
  private var modCount = -1L

  override fun getReferences(): Array<out PsiReference> {
    val count = manager.modificationTracker.modificationCount

    if (count != modCount) {
      synchronized(refLock) {
        if (count != modCount) {
          references = ReferenceProvidersRegistry.getReferencesFromProviders(this)
          modCount = count
        }
      }
    }

    return references ?: arrayOf<PsiReference>()
  }
}