package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.external.KotlinToSkinReferenceProvider
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.json.JsonFileType
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.PathUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.idea.caches.resolve.analyzeFully
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
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
class KotlinAssetReferenceContributor : PsiReferenceContributor() {

  override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {

    registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtStringTemplateExpression::class.java),
            KotlinToSkinReferenceProvider()
    )

    createAssetAnnotationProvider(
            registrar,
            AssetUtils.ASSET_ANNOTATION_SKIN_PARAM_NAME,
            listOf(LibGDXSkinFileType.INSTANCE, JsonFileType.INSTANCE),
            listOf(LibGDXSkinLanguage.INSTANCE)
    )

    createAssetAnnotationProvider(
            registrar,
            AssetUtils.ASSET_ANNOTATION_ATLAS_PARAM_NAME,
            listOf(LibGDXAtlasFileType.INSTANCE, PlainTextFileType.INSTANCE),
            listOf(LibGDXAtlasLanguage.INSTANCE)
    )

  }

  fun createAssetAnnotationProvider(registrar: PsiReferenceRegistrar, paramName: String, fileTypes: List<FileType>, preferableLangs: List<Language>) {

    registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtStringTemplateExpression::class.java).inside(KtAnnotationEntry::class.java),
            object : PsiReferenceProvider() {

              override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<out PsiReference> {
                element.getParentOfType<KtAnnotationEntry>(true)?.let { entry ->
                  entry.analyzeFully().get(BindingContext.ANNOTATION, entry)?.type?.getJetTypeFqName(false)?.let { fqName ->
                    if (fqName == AssetUtils.ASSET_ANNOTATION_NAME) {
                      element.getParentOfType<KtValueArgument>(true)?.getParentOfType<KtValueArgument>(true)?.getArgumentName()?.asName?.identifier?.let { arg ->
                        if (arg == paramName) {
                          (element as? KtStringTemplateExpression)?.plainContent?.let { path ->
                            return arrayOf(
                                    FileReference(
                                            element,
                                            PathUtil.toSystemIndependentName(path),
                                            fileTypes = fileTypes,
                                            preferableLanguages = preferableLangs
                                    )
                            )
                          }
                        }
                      }
                    }
                  }
                }

                return arrayOf()

              }

            }
    )

  }
}