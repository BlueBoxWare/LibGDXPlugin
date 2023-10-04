/*
 * Copyright 2022 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.codeInsight.daemon.ImplicitUsageProvider
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import org.jetbrains.kotlin.idea.base.util.allScope
import org.jetbrains.kotlin.psi.KtClass

internal class ClassImplicitUsageProvider : ImplicitUsageProvider {

    override fun isImplicitUsage(element: PsiElement): Boolean {
        if (!element.project.isLibGDXProject()) {
            return false
        }

        return CachedValuesManager.getCachedValue(element) {

            val fqName =
                (element as? PsiClass)?.qualifiedName ?: (element as? KtClass)?.fqName?.asString()
                ?: return@getCachedValue CachedValueProvider.Result.create(false, element)

            var found = false
            FileTypeIndex.processFiles(LibGDXSkinFileType, { virtualFile ->
                (PsiManager.getInstance(element.project).findFile(virtualFile) as? SkinFile)?.let { skinFile ->

                    skinFile.getClassSpecifications().forEach { classSpec ->
                        classSpec.className.let { className ->
                            if (className.value.plainName == fqName) {
                                found = true
                                return@processFiles true
                            }
                        }
                    }

                }

                true

            }, element.project.allScope())

            return@getCachedValue CachedValueProvider.Result.create(found, element)
        }

    }

    override fun isImplicitRead(element: PsiElement): Boolean = false

    override fun isImplicitWrite(element: PsiElement): Boolean = false
}
