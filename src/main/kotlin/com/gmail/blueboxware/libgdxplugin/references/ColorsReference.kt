package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.*
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
class ColorsReference(element: PsiElement): PsiPolyVariantReferenceBase<PsiElement>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
          ((element as? PsiLiteralExpression)?.asString()
                  ?: (element as? KtStringTemplateExpression)?.asPlainString())?.let { colorName ->
            element.project.getColorsMap()[colorName]?.nameElements()?.map(::PsiElementResolveResult)?.toTypedArray<ResolveResult>()
          } ?: PsiElementResolveResult.EMPTY_ARRAY


  override fun getVariants(): Array<Any> = element.project.getColorsMap().entries.map { (colorName, colorDef) ->

    val icon = colorDef?.valueElement?.let {
      it.getColor(ignoreContext = true)?.let(::createColorIcon)
    } ?: AllIcons.FileTypes.Properties

    LookupElementBuilder
            .create(colorName)
            .withIcon(icon)

  }.toTypedArray()

}