package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.java.library.getMavenCoordinates
import com.intellij.openapi.progress.EmptyProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.asSafely
import org.jetbrains.kotlin.config.MavenComparableVersion
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

const val PREFIX = "com.gmail.blueboxware.libgdxplugin"

const val gdxGroupId = "com.badlogicgames.gdx"
const val gdxArtifactId = "gdx"

private val GDX198VERSION = MavenComparableVersion("1.9.8")
private val GDX199VERSION = MavenComparableVersion("1.9.9")

private fun Project.getLibGDXVersion(): MavenComparableVersion? =
    CachedValuesManager.getManager(this).getCachedValue(this) {
        var versionString: String? = null
        var isLibGDX = false

        LibraryTablesRegistrar.getInstance().getLibraryTable(this).libraryIterator.forEach { library ->
            if (library.getMavenCoordinates()?.groupId == gdxGroupId && library.getMavenCoordinates()?.artifactId == gdxArtifactId) {
                versionString = library.getMavenCoordinates()?.version
                isLibGDX = true
            }
        }

        if (versionString == null) {
            findClasses("com.badlogic.gdx.Version").forEach { clazz ->
                isLibGDX = true
                (clazz.findFieldByName(
                    "VERSION", false
                )?.initializer as? PsiLiteralExpression)?.value?.asSafely<String>()?.let {
                    versionString = it
                }
            }
        }

        val version = try {
            MavenComparableVersion(versionString)
        } catch (_: Exception) {
            if (isLibGDX) GDX199VERSION
            else null
        }

        return@getCachedValue CachedValueProvider.Result.create(
            version, ProjectRootModificationTracker.getInstance(this)
        )
    }

internal fun Project.isLibGDXProject(): Boolean = getLibGDXVersion() != null

internal fun Project.isLibGDX199(): Boolean = (getLibGDXVersion() ?: GDX198VERSION) > GDX198VERSION

internal fun <T> key(key: String) = Key<T>("$PREFIX.$key")

internal fun <T> T?.singletonOrNull(): Collection<T>? = this?.let { listOf(this) }

internal fun trimQuotes(str: String?) = str?.trim { it == '"' || it == '\'' }

internal fun Project.findClass(fqName: String, scope: GlobalSearchScope = this.allScope()) =
    psiFacade().findClass(fqName, scope)

internal fun Project.findClasses(fqName: String, scope: GlobalSearchScope = allScope()) =
    psiFacade().findClasses(fqName, scope)

internal fun PsiElement.findClass(fqName: String, scope: GlobalSearchScope = project.allScope()) =
    project.findClass(fqName, scope)

internal fun PsiElement.findClasses(fqName: String, scope: GlobalSearchScope = project.allScope()) =
    project.findClasses(fqName, scope)

internal fun <K, V> Map<K, V>.getKey(value: V): K? = keys.find { get(it) == value }

@Suppress("unused")
internal fun runUnderProgressIfNecessary(action: () -> Unit) {

    if (ProgressManager.getGlobalProgressIndicator() == null) {
        ProgressManager.getInstance().runProcess(action, EmptyProgressIndicator())
        return
    }

    action()

}

internal fun <T> computeUnderProgressIfNecessary(f: () -> T): T =
    if (ProgressManager.getGlobalProgressIndicator() == null) {
        ProgressManager.getInstance().runProcess(Computable { f() }, EmptyProgressIndicator())
    } else {
        f()
    }

internal fun PsiElement.allScope(): GlobalSearchScope = project.allScope()

internal inline fun <R> PsiElement.getCachedValue(key: Key<CachedValue<R>>, crossinline f: () -> R): R? =
    CachedValuesManager.getCachedValue(this, key) {
        CachedValueProvider.Result.create(f(), PsiModificationTracker.MODIFICATION_COUNT)
    }

internal inline fun <R> PsiElement.getCachedValue(
    key: Key<CachedValue<R>>, dependency: Any?, crossinline f: () -> R
): R? = CachedValuesManager.getManager(project).getCachedValue(this, key, {
    CachedValueProvider.Result.create(f(), dependency ?: PsiModificationTracker.MODIFICATION_COUNT)
}, false)

internal inline fun <R> Project.getCachedValue(
    key: Key<CachedValue<R>>,
    vararg dependencies: Any?,
    crossinline f: () -> R
): R? =
    CachedValuesManager.getManager(this).getCachedValue(this, key, {
        CachedValueProvider.Result.create(f(), dependencies)
    }, false)

internal fun <E> List<E>.indexOfOrNull(element: E): Int? = indexOf(element).takeIf { it >= 0 }
