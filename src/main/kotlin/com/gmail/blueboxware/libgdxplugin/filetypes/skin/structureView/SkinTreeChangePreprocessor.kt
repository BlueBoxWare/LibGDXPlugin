package com.gmail.blueboxware.libgdxplugin.filetypes.skin.structureView

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.intellij.psi.impl.PsiTreeChangePreprocessorBase

/*
 * Copyright 2017 Blue Box Ware
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
class SkinTreeChangePreprocessor(psiManager: PsiManager): PsiTreeChangePreprocessorBase(psiManager) {

  override fun isOutOfCodeBlock(element: PsiElement): Boolean  = element.language is LibGDXSkinLanguage

  override fun acceptsEvent(event: PsiTreeChangeEventImpl): Boolean  = event.file is SkinFile
}