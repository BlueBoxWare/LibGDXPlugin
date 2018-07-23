package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.util.ProcessingContext

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
class JavaAssetReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {

    if (!element.project.isLibGDXProject()) {
      return arrayOf()
    }

    (element.context?.context as? PsiMethodCallExpression)?.let { methodCall ->

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

  private fun createAtlasReferences(element: PsiElement, methodCall: PsiMethodCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName in TEXTURE_ATLAS_TEXTURE_METHODS) {

      return AssetReference.createReferences(element, methodCall, TEXTURE_REGION_CLASS_NAME)

    }

    return arrayOf()

  }

  private fun createSkinReferences(element: PsiElement, methodCall: PsiMethodCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, methodCall, COLOR_CLASS_NAME)

    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, methodCall, DRAWABLE_CLASS_NAME)

    } else if (methodName in SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, methodCall, TEXTURE_REGION_CLASS_NAME)

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, methodCall, BITMAPFONT_CLASS_NAME)

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

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

    }

    return arrayOf()

  }

  private fun getClassFromClassObjectExpression(psiClassObjectAccessExpression: PsiClassObjectAccessExpression): PsiClass? =
          (psiClassObjectAccessExpression.operand.type as? PsiClassReferenceType)?.resolve()

}