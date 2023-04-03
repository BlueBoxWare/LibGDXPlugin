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
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.lang.java.JavaLanguage
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.KotlinLanguage

class ImagePreviewPsiDocumentationTargetProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? =
        if (element.language in languages) ImagePreviewDocumentationTarget(element, originalElement) else null

    companion object {
        private val languages = listOf(
            LibGDXSkinLanguage.INSTANCE,
            LibGDXAtlas2Language.INSTANCE,
            KotlinLanguage.INSTANCE,
            JavaLanguage.INSTANCE
        )
    }
}
