package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ContainerBasedSuppressQuickFix
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

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
open class SkinFileInspection : LocalInspectionTool() {

  init {
    if (ApplicationManager.getApplication().isUnitTestMode) {
      assert(INSPECTION_NAMES.contains(getShortID()))
    }
  }

  fun getShortID() = id.removePrefix("LibGDXSkin")

  override fun getGroupPath() = arrayOf("LibGDX", "Skin files")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun isSuppressedFor(element: PsiElement): Boolean =
          (element as? SkinElement)?.isInspectionSuppressed(getShortID()) ?: super.isSuppressedFor(element)

  override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
    if (this !is SkinInspectionNameInspection) {
      arrayOf(SuppressFix(getShortID()), SuppressForFileFix(getShortID()))
    } else {
      arrayOf()
    }

  open class SuppressFix(val id: String): ContainerBasedSuppressQuickFix {

    override fun getContainer(context: PsiElement?): PsiElement? =
            ((context?.parent?.parent as? SkinClassName)?.parent ?: context)?.let { realContext ->
              PsiTreeUtil.findFirstParent(realContext, true, { it is SkinClassSpecification || it is SkinObject })
            }

    override fun getFamilyName(): String = message("suppress.object")

    override fun isSuppressAll(): Boolean = false

    override fun isAvailable(project: Project, context: PsiElement): Boolean = context.isValid && getContainer(context) != null

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
      getContainer(descriptor.psiElement)?.let { suppress(it, id) }
    }

  }

  class SuppressForFileFix(id: String): SuppressFix(id) {

    override fun getContainer(context: PsiElement?) = context?.containingFile as? SkinFile

    override fun getFamilyName(): String = message("suppress.file")

  }

  companion object {

    val INSPECTION_NAMES = listOf(
            "DuplicateProperty",
            "DuplicateResource",
            "MalformedColorString",
            "MissingProperty",
            "NonExistingClass",
            "NonExistingField",
            "NonExistingFile",
            "NonExistingResourceInAlias",
            "NonExistingInspection",
            "TypeError"
    )

    private fun suppress(element: PsiElement, id: String) {
      SkinElementFactory.createSuppressionComment(element.project, id)?.let { comment ->
        when (element) {
          is SkinObject   -> element.addComment(comment)
          is SkinClassSpecification -> element.addComment(comment)
          is SkinFile -> element.addComment(comment)
        }
      }
    }

  }

}