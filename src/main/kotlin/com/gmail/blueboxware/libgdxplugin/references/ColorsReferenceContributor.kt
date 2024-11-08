package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.gmail.blueboxware.libgdxplugin.utils.isColorsGetCall
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.analysis.api.permissions.KaAllowAnalysisOnEdt
import org.jetbrains.kotlin.analysis.api.permissions.allowAnalysisOnEdt
import org.jetbrains.kotlin.psi.KtCallExpression
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
@OptIn(KaAllowAnalysisOnEdt::class)
internal class ColorsReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiLiteralExpression::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {

                    val psiLiteralExpression = element as? PsiLiteralExpression ?: return PsiReference.EMPTY_ARRAY

                    element.getParentOfType<PsiMethodCallExpression>()?.let { methodCall ->
                        if (methodCall.isColorsGetCall()) {
                            return arrayOf(ColorsReference(psiLiteralExpression))
                        }
                    }

                    return PsiReference.EMPTY_ARRAY

                }
            }
        )

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtStringTemplateExpression::class.java), object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {

                    val ktStringTemplateExpression = element as? KtStringTemplateExpression
                        ?: return PsiReference.EMPTY_ARRAY
                    element.getParentOfType<KtCallExpression>()?.let { ktCallExpression ->
                        allowAnalysisOnEdt {
                            if (ktCallExpression.isColorsGetCall()) {
                                return arrayOf(ColorsReference(ktStringTemplateExpression))
                            }
                        }
                    }

                    return PsiReference.EMPTY_ARRAY

                }
            })
    }

}
