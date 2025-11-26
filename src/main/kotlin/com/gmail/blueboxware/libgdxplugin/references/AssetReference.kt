package com.gmail.blueboxware.libgdxplugin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.ResolveCache
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
class AssetReference(
    element: PsiElement,
    val resourceName: String,
    val className: DollarClassName?,
    private val assetFiles: Pair<Set<SkinFile>, Set<Atlas2File>>
) : PsiPolyVariantReferenceBase<PsiElement>(element) {

    val skinFiles: Set<SkinFile>
        get() = assetFiles.first

    val atlasFiles: Set<Atlas2File>
        get() = assetFiles.second

    override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> =
        ResolveCache.getInstance(element.project).resolveWithCaching(this, RESOLVER, false, incompleteCode)

    override fun getVariants(): Array<out Any> {

        val result = mutableListOf<LookupElement>()

        val isDrawable = className?.plainName == DRAWABLE_CLASS_NAME

        if (className?.plainName != TEXTURE_REGION_CLASS_NAME) {
            assetFiles.first.forEach { skinFile ->

                skinFile.getResources(
                    if (isDrawable) listOf(TINTED_DRAWABLE_CLASS_NAME) else className?.plainName.singletonOrNull()
                ).forEach { resource ->
                    val lookupElement = LookupElementBuilder
                        .create(resource)
                        .withIcon(resource.asColor(false)?.let { createColorIcon(it) })
                        .withTypeText(
                            if (className == null)
                                StringUtil.getShortName(
                                    resource
                                        .getClassSpecification()
                                        ?.getRealClassNamesAsString()
                                        ?.firstOrNull()
                                        ?: ""
                                )
                            else
                                if (isDrawable)
                                    getOriginalFileName(skinFile)
                                else
                                    null, true
                        )

                    result.add(lookupElement)
                }

            }
        }

        if (isDrawable || className == null || className.plainName == TEXTURE_REGION_CLASS_NAME) {
            assetFiles.second.forEach { atlasFile ->
                atlasFile.getPages().forEach { page ->
                    page.regionList.forEach { region ->

                        if (result.find { (it.psiElement as? Atlas2Region)?.let { reg -> reg.name == region.name } == true } == null) {
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

    fun filesPresentableText(withPrefix: Boolean): String {
        val str = StringBuilder()

        (assetFiles.first + assetFiles.second).let { files ->

            if (withPrefix) {
                if (files.size == 1) {
                    str.append("file ")
                } else if (files.size > 1) {
                    str.append("files ")
                }
            }

            files.withIndex().forEach { (index, file) ->
                str.append("\"${file.name}\"")
                if (index == files.size - 2) {
                    str.append(" or ")
                } else if (index < files.size - 1 && files.size > 1) {
                    str.append(", ")
                }
            }
        }

        return str.toString()

    }

    class Resolver : ResolveCache.PolyVariantResolver<AssetReference> {

        override fun resolve(assetReference: AssetReference, incompleteCode: Boolean): Array<ResolveResult> {

            val result = mutableListOf<PsiElementResolveResult>()

            val isDrawable = assetReference.className?.plainName == DRAWABLE_CLASS_NAME

            if (assetReference.className?.plainName != TEXTURE_REGION_CLASS_NAME) {
                assetReference.assetFiles.first.forEach {
                    if (it.getUserData(FAKE_FILE_KEY) != true) {
                        it.getResources(
                            if (isDrawable)
                                listOf(TINTED_DRAWABLE_CLASS_NAME)
                            else
                                assetReference.className?.plainName.singletonOrNull(),
                            assetReference.resourceName
                        ).forEach { resource ->
                            result.add(PsiElementResolveResult(resource))
                        }
                    }
                }
            }

            if (isDrawable || assetReference.className == null || assetReference.className.plainName == TEXTURE_REGION_CLASS_NAME) {
                assetReference.assetFiles.second.forEach { atlasFile ->
                    if (atlasFile.getUserData(FAKE_FILE_KEY) != true) {
                        atlasFile.getPages().forEach { page ->
                            page.regionList.forEach { region ->
                                if (region.name == assetReference.resourceName) {
                                    result.add(PsiElementResolveResult(region))
                                }
                            }
                        }
                    }
                }
            }

            return result.toTypedArray()


        }

    }

    companion object {

        private val RESOLVER = Resolver()

        fun createReferences(
            element: PsiElement,
            callExpression: PsiElement,
            wantedClass: DollarClassName? = null,
            resourceName: String? = null
        ): Array<PsiReference> {

            val assetFiles = when (callExpression) {
                is PsiMethodCallExpression -> {
                    callExpression.getAssetFiles()
                }

                is KtCallExpression -> {
                    callExpression.getAssetFiles()
                }

                else -> {
                    listOf<SkinFile>() to listOf()
                }
            }

            if (assetFiles.first.isEmpty() && assetFiles.second.isEmpty()) {
                return arrayOf()
            }

            return arrayOf(
                AssetReference(
                    element,
                    resourceName ?: StringUtil.stripQuotesAroundValue(element.text),
                    wantedClass,
                    assetFiles.first.toSet() to assetFiles.second.toSet()
                )
            )

        }

        fun createReferences(
            element: PsiElement,
            callExpression: PsiElement,
            wantedClass: String,
            resourceName: String? = null
        ): Array<PsiReference> = createReferences(element, callExpression, DollarClassName(wantedClass), resourceName)

        fun getOriginalFileName(psiFile: PsiFile): String =
            if (psiFile.getUserData(FAKE_FILE_KEY) == true) {
                psiFile.originalFile.name
            } else {
                psiFile.name
            }

    }

}
