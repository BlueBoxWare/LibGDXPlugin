package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.utils.allScope
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDX199
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager


/*
 * Copyright 2018 Blue Box Ware
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
internal class TaggedClassImplicitUsageProvider : ImplicitUsageProvider {

    override fun isImplicitUsage(element: PsiElement): Boolean {

        if (element !is PsiClass || !element.project.isLibGDX199()) {
            return false
        }

        return CachedValuesManager.getCachedValue(element) {
            val parameters =
                ReferencesSearch.SearchParameters(element, element.allScope(), true)

            var found = false

            TaggedClassUsagesSearcher().execute(parameters) {
                found = true
                return@execute false
            }

            return@getCachedValue CachedValueProvider.Result.create(found, element)
        }

    }

    override fun isImplicitRead(element: PsiElement): Boolean = false

    override fun isImplicitWrite(element: PsiElement): Boolean = false

}
