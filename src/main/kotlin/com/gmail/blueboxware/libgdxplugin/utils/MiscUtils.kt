package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.kotlin.idea.search.allScope

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

const val PREFIX = "com.gmail.blueboxware.libgdxplugin"

private val GDX198VERSION = MavenComparableVersion("1.9.8")

internal fun Project.isLibGDXProject(): Boolean = getComponent(VersionManager::class.java)?.getUsedVersion(Libraries.LIBGDX) != null

internal fun Project.isLibGDX199(): Boolean =
        getComponent(VersionManager::class.java)?.getUsedVersion(Libraries.LIBGDX)?.compareTo(GDX198VERSION) ?: 0 > 0

internal fun <T>key(key: String) = Key<T>("$PREFIX.$key")

internal fun <T> T?.singletonOrNull(): Collection<T>? = this?.let { listOf(this) }

internal fun trimQuotes(str: String?) = str?.trim { it == '"' || it == '\'' }

internal fun Project.findClass(fqName: String, scope: GlobalSearchScope = allScope()) =
        JavaPsiFacade.getInstance(this).findClass(fqName, scope)

internal fun Project.findClasses(fqName: String, scope: GlobalSearchScope = allScope()) =
        JavaPsiFacade.getInstance(this).findClasses(fqName, scope)

internal fun PsiElement.findClass(fqName: String, scope: GlobalSearchScope = project.allScope()) =
        project.findClass(fqName, scope)

internal fun PsiElement.findClasses(fqName: String, scope: GlobalSearchScope = project.allScope()) =
        project.findClasses(fqName, scope)

internal fun <K, V> Map<K, V>.getKey(value: V): K? = keys.find { get(it) == value }