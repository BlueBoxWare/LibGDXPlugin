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

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.DEFAULT_AI_IMPORTS
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TASK_CLASS_FQN
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeStatement
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeFile
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeImport
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeString
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.jvm.JvmModifier
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.ProjectAndLibrariesScope
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.util.ProcessingContext

internal class TreeCompletionContributor : CompletionContributor() {

    init {
        // TODO: Include etc
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withParent(TreeString::class.java).withSuperParent(4, TreeImport::class.java),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    addTaskSubclasses(parameters, result)
                }

            })

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(TreeElementTypes.TASK_NAME),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet
                ) {
                    addTaskSubclasses(parameters, result)
                    addAliases(parameters, result)
                    if (parameters.position.parent?.parent?.parent is PsiTreeStatement) {
                        addStatements(result)
                    }
                }

            })
    }

    private fun addStatements(result: CompletionResultSet) {

        listOf("import", "subtree", "root").forEach { str ->
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(str).withLookupString(str).withBoldness(true), 100.0
                )
            )
        }

    }

    private fun addAliases(parameters: CompletionParameters, result: CompletionResultSet) {

        if (parameters.position.text.contains('.')) return

        for (n in DEFAULT_AI_IMPORTS) {
            val name = n.replaceFirstChar { it.lowercase() }
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(name).withIcon(
                        AllIcons.Actions.ShowImportStatements
                    ).withItemTextItalic(true), 75.0
                )
            )
        }

        val imports = (parameters.originalFile as? TreeFile)?.getImports(parameters.position) ?: return
        imports.forEach { (name, value) ->
            val type = value.second.value?.vstring?.getValue()
            result.addElement(
                PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create(name).withTypeText(type).withIcon(
                        AllIcons.Actions.ShowImportStatements
                    ), 70.0
                )
            )
        }
    }

    private fun addTaskSubclasses(parameters: CompletionParameters, result: CompletionResultSet) {

        if (parameters.position.text.contains('.')) return

        JavaPsiFacade.getInstance(parameters.position.project).findClasses(
            TASK_CLASS_FQN, ProjectAndLibrariesScope(parameters.position.project)
        ).forEach { superClass ->
            ClassInheritorsSearch.search(superClass).forEach { psiClass ->
                val name = psiClass.name ?: return@forEach
                val fqn = psiClass.qualifiedName ?: return@forEach
                @Suppress("UnstableApiUsage") if (name in DEFAULT_AI_IMPORTS || psiClass.hasModifier(JvmModifier.ABSTRACT)) return@forEach
                result.addElement(
                    PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create(fqn).withIcon(AllIcons.Nodes.Type).withLookupString(name)
                            .withPresentableText(name).withTypeText(fqn).withBoldness(true), 75.0
                    )
                )
            }
        }

    }

}
