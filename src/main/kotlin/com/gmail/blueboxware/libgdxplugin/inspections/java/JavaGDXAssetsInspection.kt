package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.Assets
import com.gmail.blueboxware.libgdxplugin.utils.getProjectBaseDir
import com.gmail.blueboxware.libgdxplugin.utils.getPsiFile
import com.gmail.blueboxware.libgdxplugin.utils.supersAndThis
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

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
internal class JavaGDXAssetsInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("gdxassets.annotation.inspection.descriptor")

  override fun getID() = "LibGDXAssetsFileError"

  override fun getDisplayName() = message("gdxassets.annotation.inspection")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object: JavaElementVisitor() {

    override fun visitAnnotation(annotation: PsiAnnotation?) {

      if (annotation == null || annotation.qualifiedName != Assets.ASSET_ANNOTATION_NAME) {
        return
      }

      if ((annotation.owner as? PsiModifierList)?.context is PsiVariable) {
        if (annotation.getClassNamesOfOwningVariable().none { it in Assets.TARGETS_FOR_GDXANNOTATION }) {
          holder.registerProblem(annotation, message("gdxassets.annotation.problem.descriptor.wrong.target"), ProblemHighlightType.GENERIC_ERROR_OR_WARNING)
        }
      }

    }

    override fun visitAnnotationParameterList(list: PsiAnnotationParameterList?) {
      (list?.context as? PsiAnnotation)?.let { annotation ->

        if (annotation.qualifiedName != Assets.ASSET_ANNOTATION_NAME) {
          return
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
          ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
            checkSkinFilename(it, value, holder)
          }
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiLiteralExpression)?.let { literal ->
          (literal.value as? String)?.let { value ->
            checkSkinFilename(literal, value, holder)
          }
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
          ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
            checkFilename(it, value, holder)
          }
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiLiteralExpression)?.let { literal ->
          (literal.value as? String)?.let { value ->
            checkFilename(literal, value, holder)
          }
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_PROPERTIES_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
          ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
            checkFilename(it, value, holder)
          }
        }

        (annotation.findAttributeValue(Assets.ASSET_ANNOTATION_PROPERTIES_PARAM_NAME) as? PsiLiteralExpression)?.let { literal ->
          (literal.value as? String)?.let { value ->
            checkFilename(literal, value, holder)
          }
        }

        annotation.parameterList.attributes.forEach { psiNameValuePair ->
          if (psiNameValuePair.name == Assets.ASSET_ANNOTATION_SKIN_PARAM_NAME) {
            if (annotation.getClassNamesOfOwningVariable().none { it == Assets.SKIN_CLASS_NAME }) {
              registerUselessParameterProblem(holder, psiNameValuePair, Assets.ASSET_ANNOTATION_SKIN_PARAM_NAME, Assets.SKIN_CLASS_NAME)
            }
          } else if (psiNameValuePair.name == Assets.ASSET_ANNOTATION_ATLAS_PARAM_NAME) {
            if (annotation.getClassNamesOfOwningVariable().none { it == Assets.TEXTURE_ATLAS_CLASS_NAME || it == Assets.SKIN_CLASS_NAME }) {
              registerUselessParameterProblem(
                      holder,
                      psiNameValuePair,
                      Assets.ASSET_ANNOTATION_ATLAS_PARAM_NAME,
                      Assets.SKIN_CLASS_NAME + " or " + Assets.TEXTURE_ATLAS_CLASS_NAME
              )
            }
          } else if (psiNameValuePair.name == Assets.ASSET_ANNOTATION_PROPERTIES_PARAM_NAME) {
            if (annotation.getClassNamesOfOwningVariable().none { it == Assets.I18NBUNDLE_CLASS_NAME }) {
              registerUselessParameterProblem(holder, psiNameValuePair, Assets.ASSET_ANNOTATION_PROPERTIES_PARAM_NAME, Assets.I18NBUNDLE_CLASS_NAME)
            }
          }
        }

      }

    }

  }

  companion object {

    internal fun checkSkinFilename(element: PsiElement, fileName: String, holder: ProblemsHolder) {

      checkFilename(element, fileName, holder)?.let { psiFile ->
        if (psiFile.fileType != LibGDXSkinFileType.INSTANCE && psiFile !is SkinFile) {
          holder.registerProblem(element, message("gdxassets.annotation.problem.descriptor.not.a.skin", fileName), ProblemHighlightType.WEAK_WARNING)
        }
      }

    }

    internal fun checkFilename(element: PsiElement, fileName: String, holder: ProblemsHolder): PsiFile? {

      if (fileName == "") return null

      val psiFile = element.project.getPsiFile(fileName)

      if (psiFile == null) {
        holder.registerProblem(element,
                message(
                        "gdxassets.annotation.problem.descriptor.nofile",
                        fileName,
                        element.project.getProjectBaseDir()?.path ?: ""
                ),
                ProblemHighlightType.ERROR
        )
      }

      return psiFile

    }

    private fun PsiAnnotation.getClassNamesOfOwningVariable(): List<String> {

      ((owner as? PsiModifierList)?.context as? PsiVariable)?.type?.let { type ->
        if (type is PsiClassType) {
          return type.resolve()?.supersAndThis()?.mapNotNull { it.qualifiedName } ?: listOf()
        }
      }

      return listOf()

    }

    private fun registerUselessParameterProblem(
            holder: ProblemsHolder,
            psiNameValuePair: PsiNameValuePair,
            parameterName: String,
            className: String
    ) {

      psiNameValuePair.nameIdentifier?.let { identifier ->
        holder.registerProblem(
                identifier,
                message(
                        "gdxassets.annotation.problem.descriptor.useless.parameter",
                        parameterName,
                        className
                )
        )
      }

    }

  }

}