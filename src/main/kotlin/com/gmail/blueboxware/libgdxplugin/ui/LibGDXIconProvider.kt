package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectSkinFiles
import com.intellij.ide.IconProvider
import com.intellij.openapi.file.exclude.EnforcedPlainTextFileTypeManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPlainTextFile
import icons.Icons
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
class LibGDXIconProvider : IconProvider() {

  override fun getIcon(element: PsiElement, flags: Int): Icon? {

    element.containingFile?.virtualFile?.let { virtualFile ->
      if (element is PsiPlainTextFile
              && element.project.getComponent(LibGDXProjectSkinFiles::class.java)?.contains(element.virtualFile) == true
              && EnforcedPlainTextFileTypeManager.getInstance().isMarkedAsPlainText(virtualFile)
      ) {
        return Icons.LIBGDX_ICON
      }
    }

    return null

  }
}