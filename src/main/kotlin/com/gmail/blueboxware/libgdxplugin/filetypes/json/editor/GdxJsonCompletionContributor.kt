package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.childrenOfType
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.gmail.blueboxware.libgdxplugin.utils.getCachedValue
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValue
import com.intellij.util.ProcessingContext


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonCompletionContributor : CompletionContributor() {

    init {

        extend(
            CompletionType.BASIC, psiElement().withSuperParent(1, GdxJsonPropertyName::class.java),
            object : CompletionProvider<CompletionParameters>() {

                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {

                    val allPropertyNames = parameters.originalFile.collectPropertyKeys() ?: return
                    val usedPropertyNames = parameters.position.firstParent<GdxJsonJobject>()?.collectUsedPropertyKeys()
                        ?: setOf()
                    val prefix = parameters.position.parent.text
                        .trim('"')
                        .split(',')
                        .firstOrNull()
                        ?.removeSuffix(CompletionUtil.DUMMY_IDENTIFIER_TRIMMED)
                        ?: return
                    val prefixMatcher = PlainPrefixMatcher(prefix)

                    allPropertyNames.forEach { propertyName ->
                        if (propertyName !in usedPropertyNames) {
                            result.withPrefixMatcher(prefixMatcher)
                                .addElement(LookupElementBuilder.create(propertyName))
                        }
                    }
                }

            })

        extend(
            CompletionType.BASIC, psiElement().withSuperParent(2, GdxJsonValue::class.java),
            object : CompletionProvider<CompletionParameters>() {

                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {

                    val prefix = parameters.position.parent.text.trim('"')
                        .removeSuffix(CompletionUtil.DUMMY_IDENTIFIER_TRIMMED)
                    val prefixMatcher = PlainPrefixMatcher(prefix)
                    parameters.originalFile.collectValues()?.forEach {
                        result.withPrefixMatcher(prefixMatcher).addElement(LookupElementBuilder.create(it))
                    }

                    KEYWORDS.forEach {
                        result.withPrefixMatcher(prefixMatcher).addElement(LookupElementBuilder.create(it).bold())
                    }

                }

            })

    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = CompletionUtil.DUMMY_IDENTIFIER_TRIMMED
    }

    companion object {

        private val KEYWORDS = setOf("true", "false", "null")

        private fun PsiFile.collectPropertyKeys() = getCachedValue(PROPERTY_NAMES_KEY) {
            (this as? GdxJsonFile)?.childrenOfType<GdxJsonProperty>()?.mapNotNull { it.name }?.toSet() ?: setOf()
        }

        private fun PsiFile.collectValues() = getCachedValue(VALUES_KEY) {
            (this as? GdxJsonFile)?.childrenOfType<GdxJsonValue>()?.mapNotNull {
                (it.value as? GdxJsonLiteral)?.getValue()
            }?.toSet() ?: setOf()
        }

        private fun GdxJsonJobject.collectUsedPropertyKeys() = getCachedValue(PROPERTY_NAMES_KEY) {
            propertyList.mapNotNull { it.name }.flatMap { it.split(",") }.map { it.trim() }.toSet()
        }

        private val PROPERTY_NAMES_KEY = key<CachedValue<Set<String>>>("property_names")
        private val VALUES_KEY = key<CachedValue<Set<String>>>("values")

    }

}
