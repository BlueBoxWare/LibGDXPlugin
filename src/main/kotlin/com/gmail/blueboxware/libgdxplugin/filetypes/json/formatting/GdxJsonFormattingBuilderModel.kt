package com.gmail.blueboxware.libgdxplugin.filetypes.json.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.intellij.formatting.*
import com.intellij.json.JsonLanguage
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonFormattingBuilderModel.java
 *
 */
internal class GdxJsonFormattingBuilderModel : FormattingModelBuilder {

    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings
        val customSettings = settings.getCustomSettings(JsonCodeStyleSettings::class.java)
        val spacingBuilder = createSpacingBuilder(settings)
        val block = GdxJsonBlock(
            null, element.node, customSettings, null, Indent.getSmartIndent(Indent.Type.CONTINUATION), null,
            spacingBuilder
        )
        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
        val jsonSettings = settings.getCustomSettings(JsonCodeStyleSettings::class.java)
        val commonSettings = settings.getCommonSettings(JsonLanguage.INSTANCE)

        val spacesBeforeComma = if (commonSettings.SPACE_BEFORE_COMMA) 1 else 0
        val spacesBeforeColon = if (jsonSettings.SPACE_BEFORE_COLON) 1 else 0
        val spacesAfterColon = if (jsonSettings.SPACE_AFTER_COLON) 1 else 0

        return SpacingBuilder(settings, JsonLanguage.INSTANCE)
            .beforeInside(COLON, STRUCTURAL_ELEMENTS).spacing(spacesBeforeColon, spacesBeforeColon, 0, false, 0)
            .afterInside(COLON, STRUCTURAL_ELEMENTS).spacing(spacesAfterColon, spacesAfterColon, 0, false, 0)
            .apply {
                for (parent in STRUCTURAL_ELEMENTS.types) {
                    withinPairInside(L_BRACKET, R_BRACKET, parent).spaceIf(
                        commonSettings.SPACE_WITHIN_BRACKETS,
                        true
                    )
                    withinPairInside(L_CURLY, R_CURLY, parent).spaceIf(commonSettings.SPACE_WITHIN_BRACES, true)
                }
            }
            .beforeInside(COMMA, STRUCTURAL_ELEMENTS).spacing(spacesBeforeComma, spacesBeforeComma, 0, false, 0)
            .afterInside(COMMA, STRUCTURAL_ELEMENTS).spaceIf(commonSettings.SPACE_AFTER_COMMA)
    }

    private val STRUCTURAL_ELEMENTS = TokenSet.create(ARRAY, JOBJECT, PROPERTY)
}
