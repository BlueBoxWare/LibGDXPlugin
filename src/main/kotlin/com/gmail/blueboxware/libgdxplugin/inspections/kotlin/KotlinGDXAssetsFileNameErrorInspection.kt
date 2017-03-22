package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.inspections.java.JavaGDXAssetsFileNameErrorInspection
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.idea.caches.resolve.analyzeFully
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.kotlin.resolve.BindingContext

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
class KotlinGDXAssetsFileNameErrorInspection : LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("gdxassets.annotation.filename.inspection.descriptor")

  override fun getID() = "LibGDXAssetsFileError"

  override fun getDisplayName() = message("gdxassets.annotation.filename.inspection")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitAnnotationEntry(annotationEntry: KtAnnotationEntry) {

      annotationEntry.analyzeFully().get(BindingContext.ANNOTATION, annotationEntry)?.type?.getJetTypeFqName(false)?.let { fqName ->

        if (fqName != AssetUtils.ASSET_ANNOTATION_NAME) return

        annotationEntry.valueArgumentList?.arguments?.forEach { argument ->

          val name = argument.getArgumentName()?.asName?.identifier

          (argument.getArgumentExpression() as? KtCallExpression)?.valueArgumentList?.arguments?.forEach {
            (it.getArgumentExpression() as? KtStringTemplateExpression)?.plainContent?.let { value ->

              if (name == AssetUtils.ASSET_ANNOTATION_SKIN_PARAM_NAME) {
                JavaGDXAssetsFileNameErrorInspection.checkSkinFilename(it, value, holder)
              } else if (name == AssetUtils.ASSET_ANNOTATION_ATLAS_PARAM_NAME) {
                JavaGDXAssetsFileNameErrorInspection.checkFilename(it, value, holder)
              }

            }
          }

        }

      }

    }
  }

}