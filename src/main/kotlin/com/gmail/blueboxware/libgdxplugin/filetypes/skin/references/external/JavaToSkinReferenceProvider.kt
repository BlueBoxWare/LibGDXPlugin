package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.external

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.gmail.blueboxware.libgdxplugin.utils.PsiUtils
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
class JavaToSkinReferenceProvider : PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
    (element.context?.context as? PsiMethodCallExpression)?.let { methodCall ->

      PsiUtils.resolveJavaMethodCallToStrings(methodCall)?.let { resolvedMethod->
        if (resolvedMethod.first == AssetUtils.SKIN_CLASS_NAME) {

          if (resolvedMethod.second == "getColor") {

            return SkinResourceExternalReference.createReferences(element, methodCall, "com.badlogic.gdx.graphics.Color")

          } else if (resolvedMethod.second == "getDrawable") {

            return SkinResourceExternalReference.createReferences(element, methodCall, "com.badlogic.gdx.scenes.scene2d.utils.Drawable")

          } else if (resolvedMethod.second == "get" || resolvedMethod.second == "optional") {

            (methodCall.argumentList.expressions.getOrNull(1) as? PsiClassObjectAccessExpression)?.let { classObject ->
              (classObject.operand.type as? PsiClassReferenceType)?.resolve()?.let { clazz ->
                if (clazz.qualifiedName != "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable") {
                  return SkinResourceExternalReference.createReferences(element, methodCall, SkinClassSpecificationMixin.putDollarInInnerClassName(clazz))
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