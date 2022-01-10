package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.utils.componentType
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.gmail.blueboxware.libgdxplugin.utils.getCachedValue
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiType
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.PsiTypesUtil

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
abstract class SkinValueMixin(node: ASTNode) : SkinValue, SkinElementImpl(node) {

    override fun getProperty() = firstParent<SkinProperty>()

    override fun isBoolean(): Boolean = false

    override fun resolveToClass(): PsiClass? =
        getCachedValue(RESOLVED_CLASS_KEY, this) {
            (parent as? SkinResource)?.let { resource ->
                resource.classSpecification?.resolveClass()
            } ?: (resolveToType() as? PsiClassType)?.resolve()

        }

    override fun resolveToTypeString(): String? =
        getCachedValue(RESOLVED_STRING_KEY, null) { resolveToType()?.canonicalText }

    override fun resolveToType(): PsiType? = getCachedValue(RESOLVED_TYPE_KEY, null) {

        (parent as? SkinResource)?.let { resource ->
            resource.classSpecification?.resolveClass()?.let { clazz ->
                return@getCachedValue PsiTypesUtil.getClassType(clazz)
            }
            return@getCachedValue null
        }

        property?.resolveToType()?.let { type ->
            var elementType: PsiType? = type
            var arrayDepth = arrayDepth()

            while (arrayDepth > 0 && elementType != null) {
                elementType = elementType.componentType(project)
                arrayDepth--
            }

            return@getCachedValue if (arrayDepth == 0) {
                elementType
            } else {
                null
            }
        }

        return@getCachedValue null

    }

    private fun arrayDepth(): Int {
        var depth = 0
        var element: PsiElement = this

        while (element.parent is SkinArray) {
            element = element.parent
            depth++
        }

        return depth
    }

    companion object {
        val RESOLVED_CLASS_KEY = key<CachedValue<PsiClass?>>("resolvedClass")
        val RESOLVED_TYPE_KEY = key<CachedValue<PsiType?>>("resolvedType")
        val RESOLVED_STRING_KEY = key<CachedValue<String?>>("resolvedString")
    }

}
