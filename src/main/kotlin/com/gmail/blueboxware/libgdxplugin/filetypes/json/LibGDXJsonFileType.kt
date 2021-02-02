package com.gmail.blueboxware.libgdxplugin.filetypes.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.editor.GdxJsonSyntaxHighlighterFactory
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders
import com.intellij.openapi.fileTypes.LanguageFileType
import icons.Icons


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
class LibGDXJsonFileType private constructor(): LanguageFileType(LibGDXJsonLanuage.INSTANCE) {

  companion object {
    val INSTANCE = LibGDXJsonFileType()
  }

  init {
    FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this) { _, _, _, colors ->
      LayeredLexerEditorHighlighter(GdxJsonSyntaxHighlighterFactory.GdxJsonHighlighter(), colors)
    }
  }

  override fun getIcon() = Icons.LIBGDX_JSON_FILETYPE

  override fun getName() = "libGDX JSON"

  override fun getDefaultExtension() = "lson"

  @Suppress("DialogTitleCapitalization")
  override fun getDescription() = "libGDX JSON file"

}