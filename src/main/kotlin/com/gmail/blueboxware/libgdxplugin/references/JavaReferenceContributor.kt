package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.utils.ASSET_ANNOTATION_ATLAS_PARAM_NAME
import com.gmail.blueboxware.libgdxplugin.utils.ASSET_ANNOTATION_NAME
import com.gmail.blueboxware.libgdxplugin.utils.ASSET_ANNOTATION_PROPERTIES_PARAM_NAME
import com.gmail.blueboxware.libgdxplugin.utils.ASSET_ANNOTATION_SKIN_PARAM_NAME
import com.intellij.json.JsonFileType
import com.intellij.lang.Language
import com.intellij.lang.properties.PropertiesFileType
import com.intellij.lang.properties.PropertiesLanguage
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.*
import com.intellij.util.PathUtil
import com.intellij.util.ProcessingContext

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
class JavaReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {

        registrar.registerReferenceProvider(
            PsiJavaPatterns.literalExpression(StandardPatterns.string()),
            AssetReferenceProvider()
        )

        createAssetAnnotationProvider(
            registrar,
            ASSET_ANNOTATION_SKIN_PARAM_NAME,
            listOf(
                LibGDXSkinFileType.INSTANCE,
                JsonFileType.INSTANCE,
                PlainTextFileType.INSTANCE
            ),
            listOf(LibGDXSkinLanguage.INSTANCE)
        )

        createAssetAnnotationProvider(
            registrar,
            ASSET_ANNOTATION_ATLAS_PARAM_NAME,
            listOf(LibGDXAtlasFileType.INSTANCE, PlainTextFileType.INSTANCE),
            listOf(LibGDXAtlasLanguage.INSTANCE)
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
            PsiJavaPatterns.literalExpression(StandardPatterns.string())
                .insideAnnotationParam(StandardPatterns.string().equalTo(ASSET_ANNOTATION_NAME), paramName),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference> {
                    ((element as? PsiLiteralExpression)?.value as? String)?.let { path ->
                        return arrayOf(
                            FileReference(
                                element,
                                PathUtil.toSystemIndependentName(path),
                                fileTypes = fileTypes,
                                preferableLanguages = preferableLangs
                            )
                        )
                    }

                    return arrayOf()
                }
            })

    }

}
