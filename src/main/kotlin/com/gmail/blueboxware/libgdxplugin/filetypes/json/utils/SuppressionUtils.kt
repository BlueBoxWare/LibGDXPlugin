package com.gmail.blueboxware.libgdxplugin.filetypes.json.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.json.COMMENTS
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.gmail.blueboxware.libgdxplugin.utils.isPrecededByNewline
import com.intellij.codeInspection.ContainerBasedSuppressQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressionUtil
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
fun GdxJsonElement.isSuppressed(id: String): Boolean {

    if ((parent as? GdxJsonElement)?.isSuppressed(id) == true) {
        return true
    }

    var prev: PsiElement? = prevSibling
    while (prev is PsiWhiteSpace || prev?.node?.elementType in COMMENTS) {
        (prev as? PsiComment)?.let { comment ->
            if (SuppressionUtil.isSuppressionComment(comment)) {
                return SuppressionUtil.isInspectionToolIdMentioned(comment.text, id)
            }
        }
        prev = prev?.prevSibling
    }

    return false

}

abstract class SuppressFix(val id: String) : ContainerBasedSuppressQuickFix {

    override fun getName(): String = familyName

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        getContainer(descriptor.psiElement)?.let { container ->
            if (!container.isPrecededByNewline()) {
                GdxJsonElementFactory(project).createNewline()?.let { newline ->
                    container.parent.addBefore(newline, container)
                }
            }
            SuppressionUtil.createSuppression(project, container, id, LibGDXJsonLanuage)
            GdxJsonElementFactory(project).createNewline()?.let { newLine ->
                container.parent.addBefore(newLine, container)
            }
        }
    }

    override fun isAvailable(project: Project, context: PsiElement): Boolean =
        context.isValid && getContainer(context) != null

    override fun isSuppressAll(): Boolean = false

}

class SuppressForFileFix(id: String) : SuppressFix(id) {

    override fun getContainer(context: PsiElement?) = context?.containingFile as? GdxJsonFile

    override fun getFamilyName(): String = message("suppress.file")

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        (descriptor.psiElement.containingFile as? GdxJsonFile)?.firstChild?.let { firstChild ->
            GdxJsonElementFactory(project).createNewline()?.let { newline ->
                SuppressionUtil.createSuppression(project, firstChild, id, LibGDXJsonLanuage)
                firstChild.parent.addBefore(newline, firstChild)
            }
        }
    }

}

class SuppressForObjectFix(id: String) : SuppressFix(id) {

    override fun getFamilyName(): String = message("suppress.object")

    override fun getContainer(context: PsiElement?): PsiElement? = context?.firstParent<GdxJsonJobject>()

}

class SuppressForStringFix(id: String) : SuppressFix(id) {

    override fun getFamilyName(): String = message("suppress.string")

    override fun getContainer(context: PsiElement?): PsiElement? = context?.firstParent<GdxJsonString>()

}

class SuppressForPropertyFix(id: String) : SuppressFix(id) {

    override fun getFamilyName(): String = message("suppress.property")

    override fun getContainer(context: PsiElement?): PsiElement? = context?.firstParent<GdxJsonProperty>()

}


