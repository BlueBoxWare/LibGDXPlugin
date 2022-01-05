package com.gmail.blueboxware.libgdxplugin.inspections

import com.gmail.blueboxware.libgdxplugin.versions.VersionService
import com.gmail.blueboxware.libgdxplugin.filetypes.properties.GDXPropertyReference
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages.ClassTagFindUsagesHandler
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.quickfixes.CreateAssetQuickFix
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiLiteralExpression
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

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
internal fun checkForNonExistingAssetReference(element: PsiElement, elementName: String, holder: ProblemsHolder) {

    element
        .references
        .filterIsInstance<AssetReference>()
        .takeIf { it.isNotEmpty() }
        ?.firstOrNull { it.multiResolve(true).isEmpty() }
        ?.let { reference ->

            val type = reference.className ?: "<unknown>"
            val files = reference.filesPresentableText(true).takeIf { it != "" }?.let { "in $it" } ?: ""
            val fixes =
                if (elementName.isNotBlank()) {
                    reference.className?.takeIf { it.plainName != TEXTURE_REGION_CLASS_NAME }?.let { className ->
                        reference.skinFiles.map { skinFile ->
                            CreateAssetQuickFix(skinFile, elementName, className, skinFile.name)
                        }.toTypedArray()
                    }
                } else {
                    null
                }

            holder.registerProblem(
                element,
                message("nonexisting.asset.problem.descriptor", elementName, type, files),
                *fixes ?: arrayOf()
            )

        }

}

internal fun checkForUnusedClassTag(element: PsiElement, holder: ProblemsHolder) {

    val tagName =
        (element as? PsiLiteralExpression)?.asString()
            ?: (element as? KtStringTemplateExpression)?.asPlainString()
            ?: return

    var found = false

    val findUsagesHandler =
        (element as? PsiLiteralExpression)?.let(::ClassTagFindUsagesHandler)
            ?: (element as? KtStringTemplateExpression)?.let(::ClassTagFindUsagesHandler)
            ?: return

    findUsagesHandler.processElementUsages(
        element,
        {
            found = true
            false
        },
        FindUsagesOptions(element.allScope())
    )

    if (!found) {
        holder.registerProblem(element, message("unused.class.tag.problem.descriptor", tagName))
    }

}

internal fun isValidProperty(element: PsiElement): Boolean {

    element.references.filterIsInstance<GDXPropertyReference>().let { references ->
        if (references.isEmpty()) {
            return true
        }
        references.forEach { reference ->
            if ((reference as? GDXPropertyReference)?.multiResolve(false)?.isEmpty() != true) {
                return true
            }
        }
    }

    return false

}

internal fun isProblematicGDXVersionFor64Bit(project: Project): Boolean {

    project.service<VersionService>().let { versionManager ->
        val gdxVersion = versionManager.getUsedVersion(Libraries.LIBGDX) ?: return false

        if (gdxVersion >= MavenComparableVersion("1.9.0") && gdxVersion < MavenComparableVersion("1.9.2")) {
            return true
        }
    }

    return false
}

internal fun checkSkinFilename(element: PsiElement, fileName: String, holder: ProblemsHolder) {

    checkFilename(element, fileName, holder)?.let { psiFile ->
        if (psiFile.fileType != LibGDXSkinFileType.INSTANCE && psiFile !is SkinFile) {
            holder.registerProblem(
                element,
                message("gdxassets.annotation.problem.descriptor.not.a.skin", fileName),
                ProblemHighlightType.WEAK_WARNING
            )
        }
    }

}

internal fun checkFilename(element: PsiElement, fileName: String, holder: ProblemsHolder): PsiFile? {

    if (fileName == "") return null

    val psiFile = element.project.getPsiFile(fileName)

    if (psiFile == null) {
        holder.registerProblem(
            element,
            message(
                "gdxassets.annotation.problem.descriptor.nofile",
                fileName,
                element.project.getProjectBaseDir()?.path ?: ""
            ),
            ProblemHighlightType.ERROR
        )
    }

    return psiFile

}

private val keyMethods = key<Pair<Set<PsiElement>, Set<PsiElement>>>("flushingmethods")
private val keyPreviousProject = key<Project>("previousproject")

internal fun getFlushingMethods(project: Project, session: LocalInspectionToolSession): Set<PsiElement>? {

    if (session.getUserData(keyMethods) == null || session.getUserData(keyPreviousProject) != project) {
        val methods = FlushingMethodsUtils.getAllFlushingMethods(project)
        session.putUserData(keyMethods, methods)
        session.putUserData(keyPreviousProject, project)
    }

    return session.getUserData(keyMethods)?.second
}
