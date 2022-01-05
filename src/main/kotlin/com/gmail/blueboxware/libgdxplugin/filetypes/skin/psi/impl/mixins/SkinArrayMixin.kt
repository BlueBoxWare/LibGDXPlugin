package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinValueImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

/*
 * Copyright 2016 Blue Box Ware
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
abstract class SkinArrayMixin(node: ASTNode) : SkinArray, SkinValueImpl(node) {

    override fun getValueList(): List<SkinValue> = PsiTreeUtil.getChildrenOfTypeAsList(this, SkinValue::class.java)

    override fun getPresentation(): ItemPresentation = object : ItemPresentation {
        override fun getPresentableText() = "array"

        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean): Icon = AllIcons.Json.Array
    }

}
