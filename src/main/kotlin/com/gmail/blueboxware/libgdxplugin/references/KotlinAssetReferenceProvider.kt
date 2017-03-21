package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
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

    if (element.project.getComponent(VersionManager::class.java)?.isLibGDXProject != true) return arrayOf()

    (element.context?.context?.context as? KtCallExpression)?.let { methodCall ->

      PsiUtils.resolveKotlinMethodCallToStrings(methodCall)?.let { resolvedMethod ->

        if (resolvedMethod.first == AssetUtils.SKIN_CLASS_NAME) {

          return createSkinReferences(element, methodCall, resolvedMethod.second)

        } else if (resolvedMethod.first == AssetUtils.TEXTURE_ATLAS_CLASS_NAME) {

          return createAtlasReferences(element, methodCall, resolvedMethod.second)

        }

      }

    }

    return arrayOf()

  }

  fun createAtlasReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName in AssetUtils.TEXTURE_ATLAS_TEXTURE_METHODS) {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    }

    return arrayOf()

  }

  fun createSkinReferences(element: PsiElement, callExpression: KtCallExpression, methodName: String): Array<out PsiReference> {

    if (methodName == "getColor") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.Color")

    } else if (methodName == "getDrawable" || methodName == "newDrawable") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.scenes.scene2d.utils.Drawable")

    } else if (methodName in AssetUtils.SKIN_TEXTURE_REGION_METHODS) {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.TextureRegion")

    } else if (methodName == "getFont") {

      return AssetReference.createReferences(element, callExpression, "com.badlogic.gdx.graphics.g2d.BitmapFont")

    } else if (methodName in listOf("get", "optional", "has", "remove")) {

      val arg2receiver = (callExpression.valueArguments.getOrNull(1)?.getArgumentExpression() as? KtDotQualifiedExpression)?.receiverExpression

      if (arg2receiver is KtClassLiteralExpression) {

        getClassFromClassLiteralExpression(arg2receiver, callExpression.analyzeFully())?.let { clazz ->
          if (clazz in AssetUtils.SKIN_TEXTURE_REGION_CLASSES) {
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

  fun getClassFromClassLiteralExpression(ktClassLiteralExpression: KtClassLiteralExpression, bindingContext: BindingContext): String? =
          ((ktClassLiteralExpression.typeReference?.typeElement as? KtUserType)?.referenceExpression as? KtSimpleNameExpression)?.getImportableTargets(bindingContext)?.firstOrNull()?.let { clazz ->
            JavaPsiFacade.getInstance(ktClassLiteralExpression.project).findClass(clazz.fqNameSafe.asString(), GlobalSearchScope.allScope(ktClassLiteralExpression.project))?.let { psiClass ->
              SkinClassSpecificationMixin.putDollarInInnerClassName(psiClass)
            }
          }

}