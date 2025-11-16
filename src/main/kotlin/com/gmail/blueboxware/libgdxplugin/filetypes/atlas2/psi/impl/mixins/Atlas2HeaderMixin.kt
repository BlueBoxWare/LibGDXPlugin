package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Header
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Page
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.Atlas2ElementImpl
import com.gmail.blueboxware.libgdxplugin.references.FileReference
import com.gmail.blueboxware.libgdxplugin.utils.getProjectBaseDir
import com.intellij.lang.ASTNode
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiReference


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
abstract class Atlas2HeaderMixin(node: ASTNode) : Atlas2Header, Atlas2ElementImpl(node) {

    override fun getValue(): String = if (parent is Atlas2Page) text else text.trim()

    override fun getReference(): PsiReference? {
        (parent as? Atlas2Page)?.getImageFile()?.let { imageFile ->
            project.getProjectBaseDir()?.let { baseDir ->

                VfsUtilCore.getRelativePath(imageFile, baseDir)?.let { relativePath ->

                    return FileReference(this, relativePath, listOf())

                }

            }
        }
        return null
    }

}
