package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.NO_ANNOTATIONS
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SkinAnnotation
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SkinAnnotations
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getSkinAnnotations
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment

/*
 * Copyright 2016 Blue Box Ware
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
abstract class SkinElementImpl(node: ASTNode): ASTWrapperPsiElement(node), SkinElement {

  override fun toString() = StringUtil.trimEnd(javaClass.simpleName, "Impl")

  override fun getActiveAnnotations(annotation: SkinAnnotations?): List<SkinAnnotation> {

    val inheritedAnnotations = (parent as? SkinElement)?.getActiveAnnotations(annotation) ?: NO_ANNOTATIONS

    if (this !is SkinObject && this !is SkinClassSpecification) {
      return inheritedAnnotations
    }

    val result = mutableListOf<SkinAnnotation>()
    result.addAll(inheritedAnnotations)

    findChildrenByType<PsiComment>(SkinParserDefinition.SKIN_COMMENTARIES)?.forEach { comment ->
      result.addAll(comment.getSkinAnnotations().filter { if (annotation != null) it.first == annotation else true })
    }

    return result
  }

  override fun isInspectionSuppressed(inspectionId: String): Boolean =
          getActiveAnnotations(SkinAnnotations.SUPPRESS).any { it.second == inspectionId }

}