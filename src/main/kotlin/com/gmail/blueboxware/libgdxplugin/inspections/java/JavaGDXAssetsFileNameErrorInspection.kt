package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.gmail.blueboxware.libgdxplugin.utils.FileUtils
import com.gmail.blueboxware.libgdxplugin.utils.PsiUtils
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.json.JsonFileType
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
class JavaGDXAssetsFileNameErrorInspection : LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("gdxassets.annotation.filename.inspection.descriptor")

  override fun getID() = "LibGDXAssetsFileError"

  override fun getDisplayName() = message("gdxassets.annotation.filename.inspection")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = object: JavaElementVisitor() {

    override fun visitAnnotationParameterList(list: PsiAnnotationParameterList?) {
      (list?.context as? PsiAnnotation)?.let { annotation ->

        if (annotation.qualifiedName != AssetUtils.ASSET_ANNOTATION_NAME) {
          return
        }

        (annotation.findAttributeValue(AssetUtils.ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
          ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
            checkSkinFilename(it, value, holder)
          }
        }

        (annotation.findAttributeValue(AssetUtils.ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiLiteralExpression)?.let { literal ->
          (literal.value as? String)?.let { value ->
            checkSkinFilename(literal, value, holder)
          }
        }

        (annotation.findAttributeValue(AssetUtils.ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
          ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
            checkFilename(it, value, holder)
          }
        }

        (annotation.findAttributeValue(AssetUtils.ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiLiteralExpression)?.let { literal ->
          (literal.value as? String)?.let { value ->
            checkFilename(literal, value, holder)
          }
        }

      }

    }

  }

  companion object {

    fun checkSkinFilename(element: PsiElement, fileName: String, holder: ProblemsHolder) {

      checkFilename(element, fileName, holder)?.fileType?.let { type ->
        if (type != LibGDXSkinFileType.INSTANCE && type != JsonFileType.INSTANCE) {
          holder.registerProblem(element, message("gdxassets.annotation.filename.problem.descriptor.not.a.skin", fileName), ProblemHighlightType.WEAK_WARNING)
        }
      }

    }

    fun checkFilename(element: PsiElement, fileName: String, holder: ProblemsHolder): PsiFile? {

      val psiFile = PsiUtils.getPsiFile(element.project, fileName)

      if (psiFile == null) {
        holder.registerProblem(element,
                message(
                        "gdxassets.annotation.filename.problem.descriptor.nofile",
                        fileName,
                        FileUtils.projectBaseDir(element.project)?.path ?: ""
                ),
                ProblemHighlightType.ERROR
        )
      }

      return psiFile

    }

  }

}