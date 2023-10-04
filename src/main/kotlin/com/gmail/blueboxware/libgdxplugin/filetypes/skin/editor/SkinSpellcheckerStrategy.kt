package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SuppressForFileFix
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SuppressForObjectFix
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.SuppressForPropertyFix
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.isSuppressed
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.inspections.SpellCheckingInspection
import com.intellij.spellchecker.tokenizer.SuppressibleSpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.Tokenizer
import com.intellij.spellchecker.tokenizer.TokenizerBase


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

private val TOKENIZER = TokenizerBase<SkinElement>(PlainTextSplitter.getInstance())

internal class SkinSpellcheckerStrategy : SuppressibleSpellcheckingStrategy() {

    override fun getTokenizer(element: PsiElement?): Tokenizer<out PsiElement> =
        if (element is PsiWhiteSpace) {
            EMPTY_TOKENIZER
        } else if (element is SkinStringLiteral) {
            if (element.parent is SkinClassName || element.parent is SkinPropertyName) {
                EMPTY_TOKENIZER
            } else {
                TOKENIZER
            }
        } else if (element is PsiComment) {
            super.getTokenizer(element)
        } else {
            EMPTY_TOKENIZER
        }

    override fun getSuppressActions(element: PsiElement, name: String): Array<SuppressQuickFix> =
        arrayOf(
            SuppressForPropertyFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME),
            SuppressForObjectFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME),
            SuppressForFileFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME)
        )

    override fun isSuppressedFor(element: PsiElement, name: String): Boolean =
        (element as? SkinElement)?.isSuppressed(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME)
            ?: false
}
