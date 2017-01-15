package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult

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
class SkinResourceReference(element: SkinPropertyValue) : SkinReference<SkinPropertyValue>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

    val result = mutableListOf<PsiElementResolveResult>()

    (element.containingFile as? SkinFile)?.getClassSpecifications()?.let { classSpecifications ->

      element.property?.resolveToTypeString()?.let { valueType ->
        (element.value as? SkinStringLiteral)?.let { stringLiteral ->

          for (classSpec in classSpecifications) {
            if (classSpec.classNameAsString == valueType) {
              for (resource in classSpec.resourcesAsList) {
                if (resource.name == stringLiteral.value) {
                  result.add(PsiElementResolveResult(resource))
                }
              }
            }
          }

        }
      }
    }

    return result.toTypedArray()
  }

  override fun handleElementRename(newElementName: String?): PsiElement {
    element.setValueAsString(newElementName, (element.value as? SkinStringLiteral)?.quotationChar)
    return element
  }
}
