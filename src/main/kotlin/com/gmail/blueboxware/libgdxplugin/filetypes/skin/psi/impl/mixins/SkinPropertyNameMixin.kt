package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaFieldReference
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference

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
abstract class SkinPropertyNameMixin(node: ASTNode) : SkinPropertyName, SkinElementImpl(node) {

    override fun getProperty(): SkinProperty? = firstParent()

    override fun getValue() = stringLiteral.value

    override fun getReference(): PsiReference? = SkinJavaFieldReference(this)

    override fun toString(): String = "SkinPropertyName(${stringLiteral.text})"

}
