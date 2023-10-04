package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SuppressForObjectFix
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.DEFAULT_TAGGED_CLASSES_NAMES
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName
import com.gmail.blueboxware.libgdxplugin.utils.getKey
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDX199
import com.intellij.codeInsight.intention.FileModifier
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile


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
internal class SkinAbbrClassInspection : SkinBaseInspection() {

    override fun getStaticDescription() = message("skin.inspection.abbr.class.description")

    override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
        arrayOf(
            SuppressForObjectFix(id),
            SuppressForFileFix(id)
        )

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : SkinElementVisitor() {

            override fun visitClassName(skinClassName: SkinClassName) {

                if (!skinClassName.project.isLibGDX199()) {
                    return
                }

                skinClassName
                    .resolve()
                    ?.let(::DollarClassName)
                    ?.takeIf { it == skinClassName.value }
                    ?.let { fqName ->

                        DEFAULT_TAGGED_CLASSES_NAMES.getKey(fqName.plainName)?.let { shortName ->

                            holder.registerProblem(
                                skinClassName,
                                message("skin.inspection.abbr.class.message", shortName),
                                QuickFix(skinClassName, DollarClassName(shortName))
                            )

                        }

                    }

            }
        }

    private class QuickFix(element: SkinClassName, @FileModifier.SafeFieldForPreview val shortName: DollarClassName) :
        LocalQuickFixOnPsiElement(element) {

        override fun getFamilyName(): String = FAMILY_NAME

        override fun getText(): String = familyName

        override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
            (startElement as? SkinClassName)?.value = shortName
        }

    }

    companion object {

        const val FAMILY_NAME = "Change to short class name"

    }

}
