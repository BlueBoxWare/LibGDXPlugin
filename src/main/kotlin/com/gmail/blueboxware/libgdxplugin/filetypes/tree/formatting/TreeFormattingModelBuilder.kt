/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet

internal class TreeFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            TreeBlock(
                formattingContext.node, createSpaceBuilder(codeStyleSettings)
            ), codeStyleSettings
        )
    }

}

val SPACE_SEPERATED = TokenSet.create(
    TreeElementTypes.TASKNAME, TreeElementTypes.TIMPORT, TreeElementTypes.TROOT,
    TreeElementTypes.TSUBTREE, TreeElementTypes.ATTRIBUTE, TreeElementTypes.COMMENT
)

private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
    val commonSettings = settings.getCommonSettings(TreeLanguage)

    return SpacingBuilder(settings, TreeLanguage)
        .before(TreeElementTypes.COLON).spaceIf(commonSettings.SPACE_BEFORE_COLON)
        .after(TreeElementTypes.COLON).spaceIf(commonSettings.SPACE_AFTER_COLON)

        .after(TreeElementTypes.GUARD).spaceIf(commonSettings.SPACE_AFTER_TYPE_CAST)

        .withinPair(TreeElementTypes.LPAREN, TreeElementTypes.RPAREN).spaceIf(commonSettings.SPACE_WITHIN_PARENTHESES)

        .between(SPACE_SEPERATED, SPACE_SEPERATED).spaces(1)
}

