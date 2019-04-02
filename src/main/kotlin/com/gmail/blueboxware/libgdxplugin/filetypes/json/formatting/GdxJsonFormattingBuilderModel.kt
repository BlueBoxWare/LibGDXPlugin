package com.gmail.blueboxware.libgdxplugin.filetypes.json.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.intellij.formatting.*
import com.intellij.json.JsonLanguage
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonFormattingBuilderModel.java
 *
 */
class GdxJsonFormattingBuilderModel: FormattingModelBuilder {

  override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? = null

  override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
    val customSettings = settings.getCustomSettings(JsonCodeStyleSettings::class.java)
    val spacingBuilder = createSpacingBuilder(settings)
    val block = GdxJsonBlock(null, element.node, customSettings, null, Indent.getNoneIndent(), null, spacingBuilder)
    return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
  }

  companion object {
    fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
      val jsonSettings = settings.getCustomSettings(JsonCodeStyleSettings::class.java)
      val commonSettings = settings.getCommonSettings(JsonLanguage.INSTANCE)

      val spacesBeforeComma = if (commonSettings.SPACE_BEFORE_COMMA) 1 else 0
      val spacesBeforeColon = if (jsonSettings.SPACE_BEFORE_COLON) 1 else 0
      val spacesAfterColon = if (jsonSettings.SPACE_AFTER_COLON) 1 else 0

      return SpacingBuilder(settings, JsonLanguage.INSTANCE)
              .before(COLON).spacing(spacesBeforeColon, spacesBeforeColon, 0, true, 0)
              .after(COLON).spacing(spacesAfterColon, spacesAfterColon, 0, true, 0)
              .withinPair(L_BRACKET, R_BRACKET).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, true)
              .withinPair(L_CURLY, R_CURLY).spaceIf(commonSettings.SPACE_WITHIN_BRACES, true)
              .before(COMMA).spacing(spacesBeforeComma, spacesBeforeComma, 0, false, 0)
              .after(COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA)
    }
  }
}
