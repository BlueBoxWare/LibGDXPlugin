package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.utils.TAG_ANNOTATION_NAME
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.gmail.blueboxware.libgdxplugin.utils.isLeaf
import com.intellij.find.findUsages.DefaultUsageTargetProvider
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.usages.UsageTarget
import org.jetbrains.kotlin.asJava.toLightAnnotation
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtAnnotationEntry
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
class ClassTagUsageTargetProvider: DefaultUsageTargetProvider() {

  override fun getTargets(editor: Editor, file: PsiFile): Array<UsageTarget>? {

    file.findElementAt(editor.caretModel.offset)?.let { sourceElement ->

      if (sourceElement.isLeaf(JavaTokenType.STRING_LITERAL)) {

        sourceElement.getParentOfType<PsiAnnotation>().let { psiAnnotation ->
          if (psiAnnotation == null || psiAnnotation.qualifiedName != TAG_ANNOTATION_NAME) {
            return UsageTarget.EMPTY_ARRAY
          }
        }

        sourceElement.getParentOfType<PsiLiteralExpression>()?.let {
          return arrayOf(PsiElement2UsageTargetAdapter(it))
        }

      } else if (sourceElement.isLeaf(KtTokens.REGULAR_STRING_PART)) {

        sourceElement.getParentOfType<KtAnnotationEntry>().let { ktAnnotationEntry ->
          if (ktAnnotationEntry == null || ktAnnotationEntry.toLightAnnotation()?.qualifiedName != TAG_ANNOTATION_NAME) {
            return UsageTarget.EMPTY_ARRAY
          }
        }

        sourceElement.getParentOfType<KtStringTemplateExpression>()?.let {
          return arrayOf(PsiElement2UsageTargetAdapter(it))
        }

      }

      Unit

    }

    return UsageTarget.EMPTY_ARRAY
  }

  override fun getTargets(psiElement: PsiElement): Array<UsageTarget>? {
    return null
  }

}