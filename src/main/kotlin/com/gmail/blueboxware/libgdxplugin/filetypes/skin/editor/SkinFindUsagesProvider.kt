package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinLexer
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

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
class SkinFindUsagesProvider : FindUsagesProvider {

  override fun getType(element: PsiElement) = when(element) {
    is SkinResource       ->  "skin resource"
    else                  ->  ""
  }

  override fun getNodeText(element: PsiElement, useFullName: Boolean) = when(element) {
    is SkinResource     -> "skin resource"
    else                -> ""
  }

  override fun getDescriptiveName(element: PsiElement) = when(element) {
    is SkinResource     -> element.name + " (" + (element.classSpecification?.classNameAsString ?: "<unknown>") + ")"
    else                -> ""
  }

  override fun getHelpId(psiElement: PsiElement) = null

  override fun canFindUsagesFor(psiElement: PsiElement) = psiElement is SkinResource

  override fun getWordsScanner() = DefaultWordsScanner(
          SkinLexer(),
          TokenSet.create(SkinElementTypes.UNQUOTED_STRING),
          SkinParserDefinition.SKIN_COMMENTARIES,
          TokenSet.create(SkinElementTypes.UNQUOTED_STRING)
  ).apply { setMayHaveFileRefsInLiterals(true) }
}