package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonFile
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider


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
class GdxJsonFileImpl(fileViewProvider: FileViewProvider) :
    PsiFileBase(fileViewProvider, LibGDXJsonLanuage),
    GdxJsonFile {

    val factory = GdxJsonElementFactory(project)

    override fun getFileType(): FileType = viewProvider.fileType

}
