package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Field
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Value
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.Atlas2ElementImpl
import com.gmail.blueboxware.libgdxplugin.utils.childrenOfType
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException


/*
 * Copyright 2022 Blue Box Ware
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
abstract class Atlas2FieldMixin(node: ASTNode) : Atlas2Field, Atlas2ElementImpl(node) {

    override fun getKey(): String = keyElement.getValue()

    override fun getValueElements(): List<Atlas2Value> =
        childrenOfType<Atlas2Value>().toList()

    override fun getValues(): List<String> =
        getValueElements().map { it.getValue() }

    override fun getName(): String? = getKey()

    override fun setName(name: String): PsiElement = throw IncorrectOperationException()

    override fun getPresentation() = object : ItemPresentation {

        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean) = AllIcons.FileTypes.UiForm

        override fun getPresentableText() = getKey() + ": " + getValues().joinToString()

    }

}
