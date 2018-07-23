package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.idea.imports.getImportableTargets
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.resolve.BindingContext
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
class KotlinAssetReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {

    if (!element.project.isLibGDXProject()) {
      return arrayOf()
    }

    (element.context?.context?.context as? KtCallExpression)?.let { methodCall ->

      methodCall.resolveCallToStrings()?.let { (className, methodName) ->

        if (className == SKIN_CLASS_NAME) {

          return createSkinReferences(element, methodCall, methodName)

        } else if (className == TEXTURE_ATLAS_CLASS_NAME) {

          return createAtlasReferences(element, methodCall, methodName)

        }

      }

    }

    return arrayOf()

  }

  private fun createAtlasReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName in TEXTURE_ATLAS_TEXTURE_METHODS) {

      return AssetReference.createReferences(element, callExpression, TEXTURE_REGION_CLASS_NAME)

    }

    return arrayOf()

  }

  private fun createSkinReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, callExpression, COLOR_CLASS_NAME)

    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, callExpression, DRAWABLE_CLASS_NAME)

    } else if (methodName in SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, callExpression, TEXTURE_REGION_CLASS_NAME)

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, callExpression, BITMAPFONT_CLASS_NAME)

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

      val arg2receiver = (callExpression.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression

      if (arg2receiver is KtClassLiteralExpression) {

        getClassFromClassLiteralExpression(arg2receiver, callExpression.analyzePartial())?.let { clazz ->
          if (clazz.qualifiedName in SKIN_TEXTURE_REGION_CLASSES) {
            return AssetReference.createReferences(element, callExpression, wantedClass = TEXTURE_REGION_CLASS_NAME)
          } else if (clazz.qualifiedName != TINTED_DRAWABLE_CLASS_NAME) {
            return AssetReference.createReferences(element, callExpression, wantedClass = DollarClassName(clazz))
          }
        }

      }

      return AssetReference.createReferences(element, callExpression)

    }

    return arrayOf()
  }

  private fun getClassFromClassLiteralExpression(ktClassLiteralExpression: KtClassLiteralExpression, bindingContext: BindingContext): PsiClass? =
    (ktClassLiteralExpression.receiverExpression as? KtReferenceExpression ?: (ktClassLiteralExpression.receiverExpression as? KtDotQualifiedExpression)?.selectorExpression as? KtReferenceExpression)?.getImportableTargets(bindingContext)?.firstOrNull()?.let { clazz ->
      ktClassLiteralExpression.findClass(clazz.fqNameSafe.asString())
    }

}