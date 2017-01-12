package com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonFormattingBuilderModel.java
 *
 */
class SkinFormattingBuilderModel : FormattingModelBuilder {

  override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?) = null

  override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
    val block = SkinBlock(null, element.node, settings, null, Indent.getNoneIndent(), null)
    return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
  }

  companion object {
    fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
      val skinSettings = settings.getCustomSettings(SkinCodeStyleSettings::class.java)
      val commonSettings = settings.getCommonSettings(LibGDXSkinLanguage.INSTANCE)

      val spacesBeforeComma = if (commonSettings.SPACE_BEFORE_COMMA) 1 else 0
      val spacesBeforeColon = if (skinSettings.SPACE_BEFORE_COLON) 1 else 0
      val spacesAfterColon = if (skinSettings.SPACE_AFTER_COLON) 1 else 0

      return SpacingBuilder(settings, LibGDXSkinLanguage.INSTANCE)
              .before(COLON).spacing(spacesBeforeColon, spacesBeforeColon, 0, false, 0)
              .after(COLON).spacing(spacesAfterColon, spacesAfterColon, 0, false, 0)
              .withinPair(L_BRACKET, R_BRACKET).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, true)
              .withinPair(L_CURLY, R_CURLY).spaceIf(commonSettings.SPACE_WITHIN_BRACES, true)
              .before(COMMA).spacing(spacesBeforeComma, spacesBeforeComma, 0, false, 0)
              .after(COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA)
    }
  }
}
