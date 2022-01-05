package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElement
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.*
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
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
class GdxJsonSpellcheckerStrategy : SuppressibleSpellcheckingStrategy() {

    override fun getTokenizer(element: PsiElement?): Tokenizer<out PsiElement> =
        if (element is GdxJsonString) {
            TOKENIZER
        } else {
            super.getTokenizer(element)
        }

    override fun getSuppressActions(element: PsiElement, name: String): Array<SuppressQuickFix> =
        arrayOf(
            SuppressForFileFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME),
            SuppressForObjectFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME),
            SuppressForPropertyFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME),
            SuppressForStringFix(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME)
        )

    override fun isSuppressedFor(element: PsiElement, name: String): Boolean =
        (element as? GdxJsonElement)?.isSuppressed(SpellCheckingInspection.SPELL_CHECKING_INSPECTION_TOOL_NAME)
            ?: false

    companion object {

        val TOKENIZER = TokenizerBase<GdxJsonString>(PlainTextSplitter.getInstance())

    }

}
