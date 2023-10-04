package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SKIN_COMMENTARIES
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.gmail.blueboxware.libgdxplugin.utils.isLeaf
import com.gmail.blueboxware.libgdxplugin.utils.isPrecededByNewline
import com.intellij.codeInspection.ContainerBasedSuppressQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressionUtil
import com.intellij.codeInspection.SuppressionUtil.isSuppressionComment
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace


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
val INSPECTION_NAMES = listOf(
    "SkinDuplicateProperty",
    "SkinDuplicateResourceName",
    "SkinMalformedColorString",
    "SkinMissingProperty",
    "SkinNonExistingClass",
    "SkinNonExistingField",
    "SkinNonExistingFontFile",
    "SkinNonExistingResourceAlias",
    "SkinNonExistingInspection",
    "SkinType",
    "SkinAbbrClass",
    "SkinDeprecated",
    "SkinSpellCheckingInspection"
)

fun SkinElement.isSuppressed(id: String): Boolean {

    if (this is SkinFile) {
        children.forEach { child ->
            if ((child as? PsiComment)?.isSuppressionComment(id) == true) {
                return true
            } else if (child.isLeaf(SkinElementTypes.L_CURLY)) {
                return false
            }
        }
    }

    if ((parent as? SkinElement)?.isSuppressed(id) == true) {
        return true
    }

    if (this is SkinResources) {
        return false
    }

    var prev: PsiElement? = prevSibling

    if (prev == null && this is SkinResource) {
        prev = parent?.prevSibling
    }

    while (prev is PsiWhiteSpace || prev?.node?.elementType in SKIN_COMMENTARIES) {
        (prev as? PsiComment)?.let { comment ->
            if (comment.isSuppressionComment(id)) {
                return true
            }
        }
        prev = prev?.prevSibling
    }

    return false

}

private fun PsiComment.isSuppressionComment(id: String) =
    isSuppressionComment(this) && SuppressionUtil.isInspectionToolIdMentioned(text, id)

abstract class SuppressFix(val id: String) : ContainerBasedSuppressQuickFix {

    override fun getName(): String = familyName

    override fun isSuppressAll(): Boolean = false

    override fun isAvailable(project: Project, context: PsiElement): Boolean =
        context.isValid && getContainer(context) != null

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        getContainer(descriptor.psiElement)?.let { container ->
            if (!container.isPrecededByNewline()) {
                SkinElementFactory(project).createNewLine()?.let { newline ->
                    container.parent.addBefore(newline, container)
                }
            }
            SuppressionUtil.createSuppression(project, container, id, LibGDXSkinLanguage.INSTANCE)
            SkinElementFactory(project).createNewLine()?.let { newline ->
                container.parent.addBefore(newline, container)
            }
        }
    }

}

class SuppressForObjectFix(id: String) : SuppressFix(id) {

    override fun getFamilyName(): String = message("suppress.object")

    override fun getContainer(context: PsiElement?): PsiElement? =
        context?.firstParent(true) { it is SkinClassSpecification || it is SkinObject }

}

class SuppressForPropertyFix(id: String) : SuppressFix(id) {

    override fun getFamilyName(): String = message("suppress.property")

    override fun getContainer(context: PsiElement?): PsiElement? = context?.firstParent<SkinProperty>()

}

class SuppressForFileFix(id: String) : SuppressFix(id) {

    override fun getContainer(context: PsiElement?) = context?.containingFile as? SkinFile

    override fun getFamilyName(): String = message("suppress.file")

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        (descriptor.psiElement.containingFile as? SkinFile)?.firstChild?.let { firstChild ->
            SkinElementFactory(project).createNewLine()?.let { newline ->
                SuppressionUtil.createSuppression(project, firstChild, id, LibGDXSkinLanguage.INSTANCE)
                firstChild.parent.addBefore(newline, firstChild)
            }
        }
    }

}
