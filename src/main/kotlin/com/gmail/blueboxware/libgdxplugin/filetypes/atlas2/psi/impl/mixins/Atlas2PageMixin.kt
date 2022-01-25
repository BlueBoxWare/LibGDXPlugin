package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Page
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.Atlas2FieldOwnerImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException
import com.intellij.util.PathUtil


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
abstract class Atlas2PageMixin(node: ASTNode) : Atlas2Page, Atlas2FieldOwnerImpl(node) {

    override fun getIndex(): Int? = (containingFile as? Atlas2File)?.getPages()?.indexOf(this)


    override fun getName(): String? = header.value

    override fun setName(name: String): PsiElement = throw IncorrectOperationException()

    override fun getImageFile(): VirtualFile? =
        containingFile.virtualFile.parent?.findFileByRelativePath(PathUtil.toSystemIndependentName(name))

    override fun getPresentation() = object : ItemPresentation {

        override fun getLocationString(): String? = null

        override fun getIcon(unused: Boolean) = AllIcons.FileTypes.Any_type

        override fun getPresentableText(): String {
            val index = (index ?: 0) + 1
            return "Page $index ($name)"
        }

    }

}
