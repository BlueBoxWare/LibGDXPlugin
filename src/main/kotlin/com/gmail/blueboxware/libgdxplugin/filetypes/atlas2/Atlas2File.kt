package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Field
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2FieldOwner
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Page
import com.gmail.blueboxware.libgdxplugin.utils.FilePresentation
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import icons.Icons


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
class Atlas2File(fileViewProvider: FileViewProvider) : PsiFileBase(fileViewProvider, LibGDXAtlas2Language.INSTANCE),
    Atlas2FieldOwner {

    fun getPages(): List<Atlas2Page> = children.mapNotNull { it as? Atlas2Page }

    override fun getFileType() = viewProvider.fileType

    override fun getFieldList(): List<Atlas2Field> = children.filterIsInstance<Atlas2Field>()

    override fun getField(name: String): Atlas2Field? = getFieldList().firstOrNull { it.key == name }

    override fun getFieldValues(name: String): List<String>? = getField(name)?.values

    override fun getFieldValue(name: String): String? = getFieldValues(name)?.firstOrNull()

    override fun getFieldValueI(name: String): Int? = getFieldValue(name)?.toIntOrNull()

    override fun getFieldValueI(name: String, default: Int): Int = getFieldValueI(name) ?: default

    override fun getFieldValuesI(name: String): List<Int?>? = getFieldValues(name)?.map { it.toIntOrNull() }

    override fun getFieldValuesI(name: String, default: Int): List<Int>? =
        getFieldValues(name)?.map { it.toIntOrNull() ?: default }

    override fun toString() = "Atlas2File: " + (virtualFile?.name ?: "<unknown>")

    override fun getPresentation() = FilePresentation(project, virtualFile, name, Icons.ATLAS_FILETYPE)

}
