/*
 * Copyright 2023 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.LibGDXAtlas2Language
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinResourceReference
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.gmail.blueboxware.libgdxplugin.utils.PROPERTY_NAME_TINTED_DRAWABLE_NAME
import com.gmail.blueboxware.libgdxplugin.utils.TINTED_DRAWABLE_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.lang.java.JavaLanguage
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

class ImagePreviewPsiDocumentationTargetProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? {
        val language = originalElement?.language ?: return null

        if (language !in languages) {
            return null
        }

        var el: PsiElement = originalElement

        if (language == LibGDXAtlas2Language.INSTANCE) {

            el.getParentOfType<Atlas2Region>()?.let {
                return ImagePreviewDocumentationTarget(it)
            }

        } else {

            if (language == LibGDXSkinLanguage.INSTANCE && el.parent is SkinStringLiteral && el.parent.parent is SkinPropertyValue) {
                el.getParentOfType<SkinResource>()?.let { resource ->
                    if (resource.classSpecification?.resolveClass()?.qualifiedName == TINTED_DRAWABLE_CLASS_NAME) {
                        el =
                            (resource.`object`?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_NAME)?.value as? SkinStringLiteral)
                                ?: el
                    }
                }
            }

            when (language) {
                LibGDXSkinLanguage.INSTANCE -> el.getParentOfType<SkinStringLiteral>(false)
                JavaLanguage.INSTANCE -> el.getParentOfType<PsiLiteralExpression>(false)
                KotlinLanguage.INSTANCE -> el.getParentOfType<KtStringTemplateExpression>(false)
                else -> return null
            }?.let { targetElement ->
                if (targetElement.references.any { it is AssetReference || it is SkinResourceReference }) {
                    return ImagePreviewDocumentationTarget(targetElement)
                }
            }

        }

        return null
    }

    companion object {
        private val languages = listOf(
            LibGDXSkinLanguage.INSTANCE, LibGDXAtlas2Language.INSTANCE, KotlinLanguage.INSTANCE, JavaLanguage.INSTANCE
        )
    }
}
