package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getArrayIndexOfItem
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.isArrayElement
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider


/*
 * Copyright 2019 Blue Box Ware
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
class SkinBreadcrumbsProvider : BreadcrumbsProvider {

    override fun getLanguages() = LANGUAGES

    override fun acceptElement(element: PsiElement): Boolean =
        element is SkinProperty
                || element is SkinClassSpecification
                || element is SkinResource
                || element is SkinArray

    override fun getElementInfo(element: PsiElement): String {

        if (element is SkinProperty) {
            return element.name
        } else if (element is SkinClassSpecification) {
            return element.classNameAsString.plainName
        } else if (element is SkinResource) {
            return element.name
        } else if (element is SkinValue && element.isArrayElement()) {
            element.getArrayIndexOfItem()?.let {
                return it.toString()
            }
        }

        return ""

    }

    companion object {
        val LANGUAGES = arrayOf(LibGDXSkinLanguage.INSTANCE)
    }

}
