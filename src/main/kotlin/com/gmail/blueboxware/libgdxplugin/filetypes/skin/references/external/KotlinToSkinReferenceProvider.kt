package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.external

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.gmail.blueboxware.libgdxplugin.utils.PsiUtils
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.idea.caches.resolve.analyzeFully
import org.jetbrains.kotlin.idea.imports.getImportableTargets
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

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
class KotlinToSkinReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {

    (element.context?.context?.context as? KtCallExpression)?.let { methodCall ->

      PsiUtils.resolveKotlinMethodCallToStrings(methodCall)?.let { resolvedMethod ->

        if (resolvedMethod.first == AssetUtils.SKIN_CLASS_NAME) {

          if (resolvedMethod.second == "getColor") {

            return SkinResourceExternalReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.Color")

          } else if (resolvedMethod.second == "getDrawable") {

            return SkinResourceExternalReference.createReferences(element, methodCall, "com.badlogic.gdx.scenes.scene2d.utils.Drawable")

          } else if (resolvedMethod.second == "get" || resolvedMethod.second == "optional") {

            ((methodCall.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression as? KtClassLiteralExpression)?.let { classLiteralExpression ->
              ((classLiteralExpression.typeReference?.typeElement as? KtUserType)?.referenceExpression as? KtSimpleNameExpression)?.getImportableTargets(methodCall.analyzeFully())?.firstOrNull()?.let { clazz ->
                if (clazz.fqNameSafe.asString() != "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable") {
                  JavaPsiFacade.getInstance(element.project).findClass(clazz.fqNameSafe.asString(), GlobalSearchScope.allScope(element.project))?.let { psiClass ->
                    return SkinResourceExternalReference.createReferences(element, methodCall, SkinClassSpecificationMixin.putDollarInInnerClassName(psiClass))
                  }
                }
              }
            }

            return SkinResourceExternalReference.createReferences(element, methodCall)

          }

        }

      }

    }

    return arrayOf()

  }

}