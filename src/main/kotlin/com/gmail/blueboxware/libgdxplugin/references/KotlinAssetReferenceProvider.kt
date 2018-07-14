package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.utils.*
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

        if (className == Assets.SKIN_CLASS_NAME) {

          return createSkinReferences(element, methodCall, methodName)

        } else if (className == Assets.TEXTURE_ATLAS_CLASS_NAME) {

          return createAtlasReferences(element, methodCall, methodName)

        }

      }

    }

    return arrayOf()

  }

  private fun createAtlasReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName in Assets.TEXTURE_ATLAS_TEXTURE_METHODS) {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    }

    return arrayOf()

  }

  private fun createSkinReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.Color")

    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.scenes.scene2d.utils.Drawable")

    } else if (methodName in Assets.SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.BitmapFont")

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

      val arg2receiver = (callExpression.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression

      if (arg2receiver is KtClassLiteralExpression) {

        getClassFromClassLiteralExpression(arg2receiver, callExpression.analyzePartial())?.let { clazz ->
          if (clazz in Assets.SKIN_TEXTURE_REGION_CLASSES) {
            return AssetReference.createReferences(element, callExpression, wantedClass = "com.badlogic.gdx.graphics.g2d.TextureRegion")
          } else if (clazz != "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable") {
            return AssetReference.createReferences(element, callExpression, wantedClass = clazz)
          }
        }

      }

      return AssetReference.createReferences(element, callExpression)

    }

    return arrayOf()
  }

  private fun getClassFromClassLiteralExpression(ktClassLiteralExpression: KtClassLiteralExpression, bindingContext: BindingContext): String? =
    (ktClassLiteralExpression.receiverExpression as? KtReferenceExpression ?: (ktClassLiteralExpression.receiverExpression as? KtDotQualifiedExpression)?.selectorExpression as? KtReferenceExpression)?.getImportableTargets(bindingContext)?.firstOrNull()?.let { clazz ->
      ktClassLiteralExpression.findClass(clazz.fqNameSafe.asString())?.qualifiedName
    }

}