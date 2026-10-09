package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.TAG_ANNOTATION_NAME
import com.gmail.blueboxware.libgdxplugin.utils.getSkinTag2ClassMap
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDX199
import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Processor
import com.intellij.util.concurrency.annotations.RequiresBlockingContext
import org.jetbrains.kotlin.idea.base.util.allScope
import org.jetbrains.kotlin.psi.KtClass


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
internal class TaggedClassUsagesSearcher : QueryExecutorBase<PsiReference, ReferencesSearch.SearchParameters>() {

    @Suppress("UnstableApiUsage")
    @RequiresBlockingContext
    override fun processQuery(
        queryParameters: ReferencesSearch.SearchParameters, consumer: Processor<in PsiReference>
    ) {

        val element = queryParameters.elementToSearch

        if (element !is PsiClass && element !is KtClass) {
            return
        }

        if (queryParameters.effectiveSearchScope is LocalSearchScope) {
            return
        }

        if (!element.project.isLibGDX199()) {
            return
        }

        val qualifiedName = when (element) {
            is PsiClass -> ReadAction.nonBlocking<String?> { element.qualifiedName }.executeSynchronously()
            is KtClass -> ReadAction.nonBlocking<String?> { element.fqName?.asString() }.executeSynchronously()
            else -> null
        }

        if (qualifiedName == null || qualifiedName == TAG_ANNOTATION_NAME) {
            return
        }


        val tagsToFind = ReadAction.nonBlocking<Set<String>?> {
            queryParameters.project.getSkinTag2ClassMap()?.getTags(qualifiedName)
        }.executeSynchronously() ?: return

        if (tagsToFind.isEmpty()) {
            return
        }

        val psiManager = PsiManager.getInstance(queryParameters.project)

        ReadAction.runBlocking<Throwable> {
            FileTypeIndex.getFiles(LibGDXSkinFileType, queryParameters.project.allScope()).forEach { virtualFile ->
                ProgressManager.checkCanceled()
                (psiManager.findFile(virtualFile) as? SkinFile)?.let { skinFile ->
                    skinFile.getClassSpecifications(qualifiedName).forEach { classSpec ->
                        ProgressManager.checkCanceled()
                        if (classSpec.className.stringLiteral.getValue() in tagsToFind) {
                            classSpec.className.references.forEach { reference ->
                                consumer.process(reference)
                            }
                        }
                    }
                }
            }
        }

    }
}
