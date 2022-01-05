package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.utils.TAG_ANNOTATION_NAME
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.psi.KtAnnotationEntry
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
class ClassTagFindUsagesHandlerFactory : FindUsagesHandlerFactory() {

    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler? =
        if (!forHighlightUsages) {
            (element as? PsiLiteralExpression)?.let(::ClassTagFindUsagesHandler)
                ?: (element as? KtStringTemplateExpression)?.let(::ClassTagFindUsagesHandler)
        } else {
            null
        }

    override fun canFindUsages(element: PsiElement): Boolean =
        (element as? PsiLiteralExpression)?.getParentOfType<PsiAnnotation>()?.qualifiedName == TAG_ANNOTATION_NAME
                || (element as? KtStringTemplateExpression)?.getParentOfType<KtAnnotationEntry>()
            ?.toLightAnnotation()?.qualifiedName == TAG_ANNOTATION_NAME
}
