/*
 * Copyright 2025 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed" to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.blueboxware.libgdxplugin.filetypes.tree

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElement
import com.gmail.blueboxware.libgdxplugin.utils.getCachedValue
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.intellij.codeInsight.AnnotationUtil.getBooleanAttributeValue
import com.intellij.codeInsight.AnnotationUtil.getStringAttributeValue
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassListReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceSet
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.ProjectAndLibrariesScope
import com.intellij.psi.search.PsiShortNamesCache
import com.intellij.psi.util.PsiModificationTracker

val DEFAULT_AI_IMPORTS = listOf(
    "AlwaysFail",
    "AlwaysSucceed",
    "DynamicGuardSelector",
    "Failure",
    "Include",
    "Invert",
    "Parallel",
    "Random",
    "RandomSelector",
    "RandomSequence",
    "Repeat",
    "Selector",
    "SemaphoreGuard",
    "Sequence",
    "Success",
    "UntilFail",
    "UntilSuccess",
    "Wait"
)

private const val AI_LIB_PREFIX = "com.badlogic.gdx.ai.btree"

private const val TASK_ATTRIBUTE_ANNOTATION = "$AI_LIB_PREFIX.annotation.TaskAttribute"
private const val TASK_ATTRIBUTE_REQUIRED = "required"
private const val TASK_ATTRIBUTE_NAME = "name"


fun Project.getDefaultAiTaskClasses(): Map<String, PsiClass?> = getCachedValue(
    key("gdx.ai.default.classes"), PsiModificationTracker.getInstance(this)
) {
    DEFAULT_AI_IMPORTS.associate { shortName ->
        PsiShortNamesCache.getInstance(this).getClassesByName(shortName, ProjectAndLibrariesScope(this))
            .firstOrNull { clazz ->
                clazz.qualifiedName?.startsWith(AI_LIB_PREFIX) == true
            }.let { shortName to it }
    }
} ?: mapOf()

@Suppress("unused")
fun Project.getDefaultAiTaskFqns() =
    getDefaultAiTaskClasses().map { (name, clazz) -> name to clazz?.qualifiedName }.toMap()

const val TASK_CLASS_FQN = "com.badlogic.gdx.ai.btree.Task"

fun PsiElement.findClass(fqn: String): PsiClass? =
    JavaPsiFacade.getInstance(project).findClass(fqn, GlobalSearchScope.projectScope(project))

fun TreeElement.getClassReferences(string: String, startInElement: Int) =
    JavaClassReferenceSet(
        string.removeSuffix("?"),
        this,
        startInElement,
        false,
        JavaClassListReferenceProvider().apply {
            setOption(JavaClassReferenceProvider.RESOLVE_QUALIFIED_CLASS_NAME, true)
            setOption(JavaClassReferenceProvider.ALLOW_WILDCARDS, false)
        }).references

fun PsiClass.getTaskAttributes(): Collection<PsiField> =
    fields.filter { field -> field.hasAnnotation(TASK_ATTRIBUTE_ANNOTATION) }

fun PsiField.getTreeAttribute(): PsiAnnotation? = getAnnotation(TASK_ATTRIBUTE_ANNOTATION)

fun PsiAnnotation.isRequiredAttribute(): Boolean =
    getBooleanAttributeValue(this, TASK_ATTRIBUTE_REQUIRED) ?: false

fun PsiAnnotation.getAttributeName(): String =
    getStringAttributeValue(this, TASK_ATTRIBUTE_NAME) ?: ""

@Suppress("unused")
fun PsiField.isRequiredAttribute(): Boolean = getTreeAttribute()?.isRequiredAttribute() ?: false

@Suppress("unused")
fun PsiField.getAttributeName(): String = getTreeAttribute()?.getAttributeName() ?: ""
