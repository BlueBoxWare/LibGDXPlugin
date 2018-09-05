package com.gmail.blueboxware.libgdxplugin.completion

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInsight.completion.CompletionConfidence
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.psi.*
import com.intellij.util.ThreeState
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtCallExpression
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
class LibGDXCompletionConfidence: CompletionConfidence() {

  override fun shouldSkipAutopopup(contextElement: PsiElement, psiFile: PsiFile, offset: Int): ThreeState {

    if (!contextElement.project.isLibGDXProject()) {
      return ThreeState.UNSURE
    }

    if (psiFile.fileType == JavaFileType.INSTANCE) {

      if (contextElement.parent is PsiLiteralExpression) {

        (contextElement.parent.parent.parent as? PsiMethodCallExpression)?.let { methodCall ->
          // Colors.get() and Colors.getColors().get()
          if (methodCall.isColorsGetCall()) {
            return ThreeState.NO
          } else {
            val (clazz, _) = methodCall.resolveCallToStrings() ?: return ThreeState.UNSURE
            // Skin.*() and TextureAtlas.*()
            if (clazz == SKIN_CLASS_NAME || clazz == TEXTURE_ATLAS_CLASS_NAME) {
              return ThreeState.NO
            }
          }
        }

        // GDXAssets annotation
        if (contextElement.parent.parent.parent is PsiAnnotationParameterList) {
          contextElement.getParentOfType<PsiAnnotation>()?.let { annotation ->
            if (annotation.qualifiedName == ASSET_ANNOTATION_NAME) {
              return ThreeState.NO
            }
          }
        }
      }

    } else if (psiFile.fileType == KotlinFileType.INSTANCE) {

      if (contextElement.parent.parent is KtStringTemplateExpression) {

        (contextElement.parent.parent.parent.parent.parent as? KtCallExpression)?.let { call ->

          if (call.isColorsGetCall()) {
            // Colors.get() and Colors.getColors().get()
            return ThreeState.NO
          } else {
            val (clazz, _) = call.resolveCallToStrings() ?: return ThreeState.UNSURE

            // Skin.*() and TextureAtlas.*()
            if (clazz == SKIN_CLASS_NAME || clazz == TEXTURE_ATLAS_CLASS_NAME) {
              return ThreeState.NO
            }
          }

          // GDXAssets annotation
          if (call.getParentOfType<KtAnnotationEntry>()?.toLightAnnotation()?.qualifiedName == ASSET_ANNOTATION_NAME) {
            return ThreeState.NO
          }
        }

      }

    }

    return ThreeState.UNSURE

  }

}