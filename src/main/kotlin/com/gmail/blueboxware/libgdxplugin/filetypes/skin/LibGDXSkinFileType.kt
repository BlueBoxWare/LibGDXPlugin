package com.gmail.blueboxware.libgdxplugin.filetypes.skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.highlighting.SkinSyntaxHighlighterFactory
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.fileTypes.EditorHighlighterProvider
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import icons.Icons

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
class LibGDXSkinFileType private constructor(): LanguageFileType(LibGDXSkinLanguage.INSTANCE){

  companion object {
    val INSTANCE = LibGDXSkinFileType()
  }

  init {
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this, object: EditorHighlighterProvider {
      override fun getEditorHighlighter(project: Project?, fileType: FileType, virtualFile: VirtualFile?, colors: EditorColorsScheme): EditorHighlighter {
        return LayeredLexerEditorHighlighter(SkinSyntaxHighlighterFactory.SkinHighlighter(), colors)
      }
    })
  }

  override fun getIcon() = Icons.LIBGDX_ICON

  override fun getName() = "LibGDX Skin"

  override fun getDescription() = "LibGDX Skin file"

  override fun getDefaultExtension() = "skin"



}