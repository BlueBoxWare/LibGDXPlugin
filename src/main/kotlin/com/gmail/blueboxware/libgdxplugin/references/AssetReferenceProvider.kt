package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.idea.imports.getImportableTargets
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClassLiteralExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtReferenceExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe


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
class AssetReferenceProvider: PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

    if (!element.project.isLibGDXProject()) {
      return PsiReference.EMPTY_ARRAY
    }

    val methodCall =
            (element.context?.context as? PsiMethodCallExpression)
                    ?: (element.context?.context?.context as? KtCallExpression)
                    ?: return PsiReference.EMPTY_ARRAY

    val (className, methodName) =
            (methodCall as? PsiMethodCallExpression)?.resolveCallToStrings()
                    ?: (methodCall as? KtCallExpression)?.resolveCallToStrings()
                    ?: return PsiReference.EMPTY_ARRAY

    if (className == SKIN_CLASS_NAME) {

      return createSkinReferences(element, methodCall, methodName)

    } else if (className == TEXTURE_ATLAS_CLASS_NAME) {

      return createAtlasReferences(element, methodCall, methodName)

    } else {

      return PsiReference.EMPTY_ARRAY

    }

  }

  private fun createAtlasReferences(element: PsiElement, methodCall: PsiElement, methodName: String): Array<PsiReference> {

    if (methodName in TEXTURE_ATLAS_TEXTURE_METHODS) {
      return AssetReference.createReferences(element, methodCall, TEXTURE_REGION_CLASS_NAME)
    }

    return PsiReference.EMPTY_ARRAY
  }

  private fun createSkinReferences(element: PsiElement, methodCall: PsiElement, methodName: String): Array<PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, methodCall, COLOR_CLASS_NAME)


    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, methodCall, DRAWABLE_CLASS_NAME)

    } else if (methodName in SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, methodCall, TEXTURE_REGION_CLASS_NAME)

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, methodCall, BITMAPFONT_CLASS_NAME)

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

      if (methodCall is PsiMethodCallExpression) {

        val arg2 = methodCall.argumentList.expressions.getOrNull(1)

        if (arg2 is PsiClassObjectAccessExpression) {

          getClassFromClassObjectExpression(arg2)?.let { clazz ->
            if (clazz.qualifiedName in SKIN_TEXTURE_REGION_CLASSES) {
              return AssetReference.createReferences(element, methodCall, wantedClass = TEXTURE_REGION_CLASS_NAME)
            } else if (clazz.qualifiedName != TINTED_DRAWABLE_CLASS_NAME) {
              return AssetReference.createReferences(element, methodCall, wantedClass = DollarClassName(clazz))
            }
          }

        }

        return AssetReference.createReferences(element, methodCall)

      } else if (methodCall is KtCallExpression) {

        val arg2receiver = (methodCall.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression

        if (arg2receiver is KtClassLiteralExpression) {

          getClassFromClassLiteralExpression(arg2receiver, methodCall.analyzePartial())?.let { clazz ->
            if (clazz.qualifiedName in SKIN_TEXTURE_REGION_CLASSES) {
              return AssetReference.createReferences(element, methodCall, wantedClass = TEXTURE_REGION_CLASS_NAME)
            } else if (clazz.qualifiedName != TINTED_DRAWABLE_CLASS_NAME) {
              return AssetReference.createReferences(element, methodCall, wantedClass = DollarClassName(clazz))
            }
          }

        }

        return AssetReference.createReferences(element, methodCall)

      }

    }

    return PsiReference.EMPTY_ARRAY
  }

  private fun getClassFromClassObjectExpression(psiClassObjectAccessExpression: PsiClassObjectAccessExpression): PsiClass? =
          (psiClassObjectAccessExpression.operand.type as? PsiClassReferenceType)?.resolve()

  private fun getClassFromClassLiteralExpression(ktClassLiteralExpression: KtClassLiteralExpression, bindingContext: BindingContext): PsiClass? =
          (ktClassLiteralExpression.receiverExpression as? KtReferenceExpression
                  ?: (ktClassLiteralExpression.receiverExpression as? KtDotQualifiedExpression)?.selectorExpression as? KtReferenceExpression)?.getImportableTargets(bindingContext)?.firstOrNull()?.let { clazz ->
            ktClassLiteralExpression.findClass(clazz.fqNameSafe.asString())
          }

}