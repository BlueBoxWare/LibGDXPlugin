/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.completion

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TASK_CLASS_FQN
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeString
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.ProjectAndLibrariesScope
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.util.ProcessingContext

internal class TreeCompletionContributor : CompletionContributor() {

    init {

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(TreeString::class.java).withSuperParent(4, TreeImport::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    taskSubclassCompletion(parameters, result)
                }

            }
        )
    }

    private fun taskSubclassCompletion(parameters: CompletionParameters, result: CompletionResultSet) {

        if (parameters.position.text.contains('.')) return

        JavaPsiFacade.getInstance(parameters.position.project).findClasses(
            TASK_CLASS_FQN,
            ProjectAndLibrariesScope(parameters.position.project)
        ).forEach { superClass ->
            ClassInheritorsSearch.search(superClass).forEach { psiClass ->
                val name = psiClass.name ?: return@forEach
                val fqn = psiClass.qualifiedName ?: return@forEach
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(fqn).withIcon(AllIcons.Nodes.Type).withLookupString(name)
                            .withPresentableText(name).withBoldness(true),
                        75.0
                    )
                )
            }
        }

    }

}
