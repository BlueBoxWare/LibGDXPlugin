package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.UIUtil
import org.jetbrains.kotlin.psi.KtCallExpression

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
class AssetReference(element: PsiElement, val resourceName: String, val className: String?, val assetFiles: Pair<List<SkinFile>, List<AtlasFile>>) : PsiPolyVariantReferenceBase<PsiElement>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

    val result = mutableListOf<PsiElementResolveResult>()

    val isDrawable = className == "com.badlogic.gdx.scenes.scene2d.utils.Drawable"

    if (className != "com.badlogic.gdx.graphics.g2d.TextureRegion") {
      assetFiles.first.forEach {
        if (it.getUserData(AssetUtils.FAKE_FILE_KEY) != true) {
          it.getResources(if (isDrawable) "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable" else className, resourceName).forEach { resource ->
            result.add(PsiElementResolveResult(resource))
          }
        }
      }
    }

    if (isDrawable || className == null || className == "com.badlogic.gdx.graphics.g2d.TextureRegion") {
      assetFiles.second.forEach { atlasFile ->
        if (atlasFile.getUserData(AssetUtils.FAKE_FILE_KEY) != true) {
          atlasFile.getPages().forEach { page ->
            page.regionList.forEach { region ->
              if (region.name == resourceName) {
                result.add(PsiElementResolveResult(region))
              }
            }
          }
        }
      }
    }

    return result.toTypedArray()

  }

  override fun getVariants(): Array<out Any> {

    val result = mutableListOf<LookupElement>()

    val isDrawable = className == "com.badlogic.gdx.scenes.scene2d.utils.Drawable"

    if (className != "com.badlogic.gdx.graphics.g2d.TextureRegion") {
      assetFiles.first.forEach { skinFile ->

        skinFile.getResources(if (isDrawable) "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable" else className, null).forEach { resource ->
          val lookupElement = LookupElementBuilder
                  .create(resource)
                  .withIcon(resource.asColor(false)?.let { ColorIcon(if (UIUtil.isRetina()) 24 else 12, it, true) })
                  .withTypeText(
                          if (className == null)
                            StringUtil.getShortName(resource.classSpecification?.classNameAsString ?: "")
                          else
                            if (isDrawable)
                              getOriginalFileName(skinFile)
                            else
                              null
                          , true
                  )

          result.add(lookupElement)
        }

      }
    }

    if (isDrawable || className == null || className == "com.badlogic.gdx.graphics.g2d.TextureRegion") {
      assetFiles.second.forEach { atlasFile ->
        atlasFile.getPages().forEach { page ->
          page.regionList.forEach { region ->

            if (result.find { (it.psiElement as? AtlasRegion)?.let { reg -> reg.name == region.name} ?: false } == null) {
              val lookupElement = LookupElementBuilder
                      .create(region)
                      .withTypeText(getOriginalFileName(atlasFile), true)

              result.add(lookupElement)
            }
          }
        }
      }
    }

    return result.toTypedArray()

  }

  override fun getRangeInElement(): TextRange? = ElementManipulators.getValueTextRange(element)

  companion object {

    fun createReferences(element: PsiElement, callExpression: PsiElement, wantedClass: String? = null, resourceName: String? = null): Array<out PsiReference> {

      val assetFiles = if (callExpression is PsiMethodCallExpression) {
        AssetUtils.getAssetFiles(callExpression)
      } else if (callExpression is KtCallExpression) {
        AssetUtils.getAssetFiles(callExpression)
      } else {
        listOf<SkinFile>() to listOf<AtlasFile>()
      }

      return arrayOf(AssetReference(element, resourceName ?: StringUtil.stripQuotesAroundValue(element.text), wantedClass, assetFiles))

    }

    fun getOriginalFileName(psiFile: PsiFile): String =
            if (psiFile.getUserData(AssetUtils.FAKE_FILE_KEY) == true) {
              psiFile.originalFile.name
            } else {
              psiFile.name
            }

  }

}