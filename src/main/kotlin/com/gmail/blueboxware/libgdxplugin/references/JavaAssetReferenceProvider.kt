package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.Assets
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.gmail.blueboxware.libgdxplugin.utils.putDollarInInnerClassName
import com.gmail.blueboxware.libgdxplugin.utils.resolveCallToStrings
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

    if (element.project.isLibGDXProject()) {
      return arrayOf()
    }

    (element.context?.context as? PsiMethodCallExpression)?.let { methodCall ->

      methodCall.resolveCallToStrings()?.let { resolvedMethod->

        if (resolvedMethod.first == Assets.SKIN_CLASS_NAME) {

          return createSkinReferences(element, methodCall, resolvedMethod.second)

        } else if (resolvedMethod.first == Assets.TEXTURE_ATLAS_CLASS_NAME) {

          return createAtlasReferences(element, methodCall, resolvedMethod.second)

        }

      }

    }

    return arrayOf()
  }

  fun createAtlasReferences(element: PsiElement, methodCall: PsiMethodCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName in Assets.TEXTURE_ATLAS_TEXTURE_METHODS) {

      return AssetReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    }

    return arrayOf()

  }

  fun createSkinReferences(element: PsiElement, methodCall: PsiMethodCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.Color")

    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, methodCall, "com.badlogic.gdx.scenes.scene2d.utils.Drawable")

    } else if (methodName in Assets.SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.g2d.BitmapFont")

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

      val arg2 = methodCall.argumentList.expressions.getOrNull(1)

      if (arg2 is PsiClassObjectAccessExpression) {

        getClassFromClassObjectExpression(arg2)?.let { clazz ->
          if (clazz in Assets.SKIN_TEXTURE_REGION_CLASSES) {
            return AssetReference.createReferences(element, methodCall, wantedClass = "com.badlogic.gdx.graphics.g2d.TextureRegion")
          } else if (clazz != "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable") {
            return AssetReference.createReferences(element, methodCall, wantedClass = clazz)
          }
        }

      }

      return AssetReference.createReferences(element, methodCall)

    }

    return arrayOf()

  }

  fun getClassFromClassObjectExpression(psiClassObjectAccessExpression: PsiClassObjectAccessExpression): String? =
          (psiClassObjectAccessExpression.operand.type as? PsiClassReferenceType)?.resolve()?.let(PsiClass::putDollarInInnerClassName)

}