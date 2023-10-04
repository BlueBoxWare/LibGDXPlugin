package com.gmail.blueboxware.libgdxplugin.filetypes.properties

import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.lang.properties.codeInspection.unused.ImplicitPropertyUsageProvider
import com.intellij.lang.properties.psi.Property
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.searches.ReferencesSearch
import org.jetbrains.kotlin.idea.base.util.allScope


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
internal class GDXImplicitPropertyUsageProvider : ImplicitPropertyUsageProvider {

    override fun isUsed(property: Property): Boolean {

        val project = property.project

        if (!project.isLibGDXProject()) {
            return false
        }

        val name = property.name ?: return false
        val scope = project.allScope()

        val psiSearchHelper = PsiSearchHelper.getInstance(property.project)

        when (psiSearchHelper.isCheapEnoughToSearch(name, scope, null, null)) {
            PsiSearchHelper.SearchCostResult.ZERO_OCCURRENCES -> return false
            PsiSearchHelper.SearchCostResult.TOO_MANY_OCCURRENCES -> return true
            else -> ReferencesSearch.search(property, scope, false).forEach { reference ->
                if (reference is GDXPropertyReference) {
                    return true
                }
            }
        }

        return false

    }
}
