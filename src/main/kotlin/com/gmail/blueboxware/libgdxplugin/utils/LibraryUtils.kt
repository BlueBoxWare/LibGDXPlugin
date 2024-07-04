package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.VersionService
import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXLibrary
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.components.service
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral


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
internal fun checkVersionAndReport(
    holder: ProblemsHolder,
    element: PsiElement,
    lib: Libraries,
    version: MavenComparableVersion?
) {

    val versionManager = holder.project.service<VersionService>()

    val latestVersion = versionManager.getLatestVersion(lib) ?: return
    val usedVersion = version ?: run {
        if (lib.library is LibGDXLibrary) {
            versionManager.getUsedVersion(Libraries.LIBGDX)
        } else {
            versionManager.getUsedVersion(lib)
        }
    } ?: return

    if (usedVersion < latestVersion) {

        holder.registerProblem(
            element,
            message("outdated.version.inspection.msg", lib.library.name, latestVersion)
        )

    }
}


internal fun getLibraryInfoFromIdeaLibrary(library: Library): Pair<Libraries, MavenComparableVersion>? {

    if ((library as? LibraryEx)?.isDisposed == true) {
        return null
    }

    for (libGDXLib in Libraries.values()) {
        libGDXLib.getVersionFromIdeaLibrary(library)?.let { version ->
            return Pair(libGDXLib, version)
        }
    }

    return null

}

internal fun getLibraryFromExtKey(extKey: String): Libraries? =
    Libraries.values().find { it.library.extKeys?.contains(extKey) == true }

internal fun getLibraryFromGroovyLiteral(grLiteral: GrLiteral): Libraries? =
    grLiteral.asString()?.let(::getLibraryFromMavenCoordString)

internal fun getLibraryInfoFromGroovyLiteral(grLiteral: GrLiteral): Pair<Libraries, MavenComparableVersion?>? =
    getLibraryFromGroovyLiteral(grLiteral)?.let { lib ->
        grLiteral.asString()?.let { string ->
            lib.getVersionFromMavenCoordString(string).let { version ->
                lib to version
            }
        }
    }

internal fun getLibraryFromGroovyArgumentList(groovyCommandArgumentList: GrCommandArgumentList): Libraries? {

    val groupArgument =
        (groovyCommandArgumentList.getNamedArgument("group") as? GrLiteral)?.asString()?.let(::trimQuotes)
            ?: return null
    val nameArgument =
        (groovyCommandArgumentList.getNamedArgument("name") as? GrLiteral)?.asString()?.let(::trimQuotes)
            ?: return null

    return Libraries.values().find {
        it.library.groupId == groupArgument && it.library.artifactId == nameArgument
    }

}

internal fun getLibraryFromKotlinArgumentList(ktValueArgumentList: KtValueArgumentList): Libraries? {

    val groupArgument = ktValueArgumentList.getNamedArgumentPlainContent("group") ?: return null
    val nameArgument = ktValueArgumentList.getNamedArgumentPlainContent("name") ?: return null

    return Libraries.values().find {
        it.library.groupId == groupArgument && it.library.artifactId == nameArgument
    }

}

internal fun getLibraryInfoFromGroovyArgumentList(groovyCommandArgumentList: GrCommandArgumentList): Pair<Libraries, MavenComparableVersion?>? =
    getLibraryFromGroovyArgumentList(groovyCommandArgumentList)?.let {
        it to getVersionFromGroovyArgumentList(groovyCommandArgumentList)
    }

internal fun getLibraryInfoFromKotlinArgumentList(ktValueArgumentList: KtValueArgumentList): Pair<Libraries, MavenComparableVersion?>? =
    getLibraryFromKotlinArgumentList(ktValueArgumentList)?.let {
        it to getVersionFromKotlinArgumentList(ktValueArgumentList)
    }

internal fun getLibraryInfoFromGroovyAssignment(grAssignmentExpression: GrAssignmentExpression): Pair<Libraries, MavenComparableVersion?>? =
    grAssignmentExpression.lValue.text?.let { extKey ->
        getLibraryFromExtKey(extKey)?.let { lib ->
            getVersionFromGroovyAssignment(grAssignmentExpression).let { version ->
                lib to version
            }
        }
    }

internal fun getLibraryFromKotlinString(ktStringTemplateExpression: KtStringTemplateExpression): Libraries? =
    getLibraryFromMavenCoordString(ktStringTemplateExpression.plainContent)


internal fun getLibraryInfoFromKotlinString(ktStringTemplateExpression: KtStringTemplateExpression): Pair<Libraries, MavenComparableVersion?>? =
    ktStringTemplateExpression.plainContent.let { str ->
        getLibraryFromKotlinString(ktStringTemplateExpression)?.let { lib ->
            lib.getVersionFromMavenCoordString(str).let { version ->
                lib to version
            }
        }
    }

internal fun Libraries.getVersionFromMavenCoordString(str: String): MavenComparableVersion? =
    Regex("""${library.groupId}:${library.artifactId}:([^$:@"']+\.[^$:@"']+).*""").let { regex ->
        regex.matchEntire(str)?.let { matchResult ->
            matchResult.groupValues.getOrNull(1)?.toVersion()
        }
    }

internal fun Libraries.getVersionFromIdeaLibrary(ideaLibrary: Library): MavenComparableVersion? {

    val regex = Regex("""[/\\]${library.groupId}[/\\]${library.artifactId}[/\\]([^/]+\.[^/]+)/""")

    for (url in ideaLibrary.getUrls(OrderRootType.CLASSES)) {
        regex.find(url)?.let { matchResult ->
            matchResult.groupValues.getOrNull(1)?.let { versionString ->
                return versionString.toVersion()
            }
        }
    }

    return null

}

internal fun getVersionFromGroovyAssignment(grAssignmentExpression: GrAssignmentExpression): MavenComparableVersion? =
    (grAssignmentExpression.rValue as? GrLiteral)?.asString()?.let(::trimQuotes)?.let { MavenComparableVersion(it) }

private fun getLibraryFromMavenCoordString(str: String): Libraries? =
    Libraries.values().find { lib ->
        Regex("""${lib.library.groupId}:${lib.library.artifactId}($|:.*)""").matches(str)
    }

private fun getVersionFromGroovyArgumentList(groovyCommandArgumentList: GrCommandArgumentList): MavenComparableVersion? =
    (groovyCommandArgumentList.getNamedArgument("version") as? GrLiteral)?.asString()?.toVersion()

private fun getVersionFromKotlinArgumentList(ktValueArgumentList: KtValueArgumentList): MavenComparableVersion? =
    ktValueArgumentList.getNamedArgumentPlainContent("version")?.let(::MavenComparableVersion)

private fun KtValueArgumentList.getNamedArgumentPlainContent(name: String): String? =
    (getNamedArgument(name) as? KtStringTemplateExpression)?.plainContent

private fun String.toVersion() =
    takeIf { contains('.') && !any { char -> char in listOf('$', '"', '\'') } }?.let(::MavenComparableVersion)
