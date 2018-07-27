package com.gmail.blueboxware.libgdxplugin.filetypes.skin.quickfixes

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.DRAWABLE_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile


/*
 * Copyright 2018 Blue Box Ware
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
abstract class CreateAssetQuickFix(
        element: SkinElement,
        private val assetName: String,
        val className: DollarClassName,
        val filename: String? = null
): LocalQuickFixOnPsiElement(element) {

  abstract fun updateCaret(file: SkinFile, position: Int): Unit?

  override fun getFamilyName(): String = "Create resource"

  override fun getText(): String = "Create resource '$assetName'" + if (filename != null) " in '$filename'" else ""

  override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {

    val skinFile = startElement.containingFile as? SkinFileImpl ?: return

    (if (className.plainName == DRAWABLE_CLASS_NAME) {
      skinFile.addTintedDrawable(assetName, startElement as? SkinElement)
    } else if (className.plainName == COLOR_CLASS_NAME ) {
      skinFile.addColor(assetName, startElement as? SkinElement)
    } else {
      skinFile.addResource(className, assetName, startElement as? SkinElement)
    })?.let { (resource, position) ->
      updateCaret(skinFile, position)
    }


  }

}
