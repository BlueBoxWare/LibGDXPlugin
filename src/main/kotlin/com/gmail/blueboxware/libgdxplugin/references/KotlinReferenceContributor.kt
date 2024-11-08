package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.LibGDXAtlas2FileType
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.LibGDXAtlas2Language
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.json.JsonFileType
import com.intellij.lang.Language
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.PropertiesLanguage
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.PathUtil
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.analysis.api.permissions.KaAllowAnalysisOnEdt
import org.jetbrains.kotlin.analysis.api.permissions.allowAnalysisOnEdt
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgument

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
internal class KotlinReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtStringTemplateExpression::class.java),
            AssetReferenceProvider()
        )

        createAssetAnnotationProvider(
            registrar,
            ASSET_ANNOTATION_SKIN_PARAM_NAME,
            listOf(
                LibGDXSkinFileType,
                JsonFileType.INSTANCE,
                PlainTextFileType.INSTANCE
            ),
            listOf(LibGDXSkinLanguage.INSTANCE)
        )

        createAssetAnnotationProvider(
            registrar,
            ASSET_ANNOTATION_ATLAS_PARAM_NAME,
            listOf(LibGDXAtlas2FileType, PlainTextFileType.INSTANCE),
            listOf(LibGDXAtlas2Language.INSTANCE)
        )

        createAssetAnnotationProvider(
            registrar,
            ASSET_ANNOTATION_PROPERTIES_PARAM_NAME,
            listOf(PropertiesFileType.INSTANCE),
            listOf(PropertiesLanguage.INSTANCE)
        )

    }

    private fun createAssetAnnotationProvider(
        registrar: PsiReferenceRegistrar,
        paramName: String,
        fileTypes: List<FileType>,
        preferableLangs: List<Language>
    ) {

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtStringTemplateExpression::class.java).inside(KtAnnotationEntry::class.java),
            object : PsiReferenceProvider() {

                @OptIn(KaAllowAnalysisOnEdt::class)
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference> {
                    element.getParentOfType<KtAnnotationEntry>()?.let { entry ->
                        val classId = allowAnalysisOnEdt { entry.classId() }
                        classId?.asFqNameString()?.let { fqName ->
                            if (fqName == ASSET_ANNOTATION_NAME) {
                                var valueArgument = element.getParentOfType<KtValueArgument>()
                                if (element.context !is KtCollectionLiteralExpression) {
                                    valueArgument = valueArgument?.getParentOfType()
                                }
                                valueArgument?.getArgumentName()?.asName?.identifier?.let { arg ->
                                    if (arg == paramName) {
                                        (element as? KtStringTemplateExpression)?.asPlainString()?.let { path ->
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

            })

    }
}
