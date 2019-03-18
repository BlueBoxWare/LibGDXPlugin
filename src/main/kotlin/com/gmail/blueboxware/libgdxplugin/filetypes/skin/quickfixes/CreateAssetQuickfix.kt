package com.gmail.blueboxware.libgdxplugin.filetypes.skin.quickfixes

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.DRAWABLE_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettingsManager


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
class CreateAssetQuickFix(
        element: SkinElement,
        private val assetName: String,
        val className: DollarClassName,
        val filename: String? = null
): LocalQuickFixOnPsiElement(element) {

  override fun getFamilyName(): String = FAMILY_NAME

  override fun getText(): String = "Create resource '$assetName'" + if (filename != null) " in '$filename'" else ""

  override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {

    val skinFile = startElement.containingFile as? SkinFileImpl ?: return

    FileEditorManager.getInstance(project).openFile(skinFile.virtualFile, true, true)

    val (_, position) = (if (className.plainName == DRAWABLE_CLASS_NAME) {
      skinFile.addTintedDrawable(assetName, startElement as? SkinElement)
    } else if (className.plainName == COLOR_CLASS_NAME) {
      skinFile.addColor(assetName, startElement as? SkinElement)
    } else {
      skinFile.addResource(className, assetName, startElement as? SkinElement)
    }) ?: return

    FileEditorManager.getInstance(project).selectedTextEditor?.let { editor ->
      FileDocumentManager.getInstance().let { fileDocumentManager ->
        if (fileDocumentManager.getFile(editor.document) == file.virtualFile) {
          editor.caretModel.moveToOffset(position)
          @Suppress("DEPRECATION")
          // COMPAT: CodeStyle#getLanuageSettings() introduced in 181
          if (
                  className.plainName != DRAWABLE_CLASS_NAME
                  && className.plainName != COLOR_CLASS_NAME
                  && CodeStyleSettingsManager
                          .getSettings(project)
                          .getCommonSettings(LibGDXSkinLanguage.INSTANCE)
                          .SPACE_WITHIN_BRACES
          ) {
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.document)
            editor.document.insertString(editor.caretModel.offset, "  ")
            editor.caretModel.moveToOffset(editor.caretModel.offset - 2)
          }
        }
      }
    }

  }

  companion object {

    const val FAMILY_NAME = "Create resource"

  }

}
