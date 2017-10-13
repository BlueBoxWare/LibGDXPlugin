package com.gmail.blueboxware.libgdxplugin.filetypes.properties

import com.intellij.codeInspection.unused.ImplicitPropertyUsageProvider
import com.intellij.lang.properties.psi.Property
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.searches.ReferencesSearch


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
class GDXImplicitPropertyUsageProvider: ImplicitPropertyUsageProvider() {

  override fun isUsed(property: Property?): Boolean {

    if (property == null) {
      return false
    }

    val project = property.project
    val name = property.name ?: return false
    val scope = GlobalSearchScope.allScope(project)
    val psiSearchHelper = PsiSearchHelper.SERVICE.getInstance(property.project)
    val cheapEnough = psiSearchHelper.isCheapEnoughToSearch(name, scope, null, null)

    if (cheapEnough == PsiSearchHelper.SearchCostResult.ZERO_OCCURRENCES) {
      return false
    } else if (cheapEnough == PsiSearchHelper.SearchCostResult.TOO_MANY_OCCURRENCES) {
      return true
    } else {
      ReferencesSearch.search(property, scope, false).forEach { reference ->
        if (reference is GDXPropertyReference) {
          return true
        }
      }
    }

    return false

  }
}