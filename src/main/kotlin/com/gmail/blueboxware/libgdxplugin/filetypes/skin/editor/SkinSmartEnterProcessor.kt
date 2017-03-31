package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/editor/smartEnter/JsonSmartEnterProcessor.java
 *
 */
class SkinSmartEnterProcessor : SmartEnterProcessorWithFixers() {

  private var shouldAddNewline = false

  init {
    addFixers(SkinFixer())
    addEnterProcessors(SkinEnterProcessor())
  }

  private inner class SkinEnterProcessor : SmartEnterProcessorWithFixers.FixEnterProcessor() {

    override fun doEnter(atCaret: PsiElement?, file: PsiFile?, editor: Editor, modified: Boolean): Boolean {
      if (shouldAddNewline) {
        try {
          plainEnter(editor)
        } finally {
          shouldAddNewline = false
        }
      }

      return true
    }
  }



  private class SkinFixer : SmartEnterProcessorWithFixers.Fixer<SkinSmartEnterProcessor>() {

    override fun apply(editor: Editor, processor: SkinSmartEnterProcessor, element: PsiElement) {

      val parent = element.context?.parent ?: return

      if (parent is SkinPropertyName
              || parent is SkinClassName
              || parent is SkinResourceName
              || parent is SkinPropertyValue
              ) {

        val key = if (parent is SkinPropertyValue) {
          parent.property?.propertyName
        } else {
          parent
        }

        if (key != null) {
          val keyEndOffset = key.textRange.endOffset
          if (!isFollowedByTerminal(key, SkinElementTypes.COLON)) {
            var colonText = ":"
            CodeStyleSettingsManager.getSettings(key.project).getCustomSettings(SkinCodeStyleSettings::class.java)?.let { settings ->
              if (settings.SPACE_BEFORE_COLON) {
                colonText = " :"
              }
              if (settings.SPACE_AFTER_COLON) {
                colonText += " "
              }
            }

            processor.myFirstErrorOffset = keyEndOffset + colonText.length
            editor.document.insertString(keyEndOffset, colonText)
          }
        }


      } else if (parent is SkinArray) {

        element.context?.let { value ->
          val valueEndOffset = value.textRange.endOffset
          if (!isFollowedByTerminal(value, SkinElementTypes.COMMA)) {

            var commaText = ","
            CodeStyleSettingsManager.getSettings(parent.project).getCommonSettings(LibGDXSkinLanguage.INSTANCE)?.let { settings ->
              if (settings.SPACE_BEFORE_COMMA) {
                commaText = " ,"
              }
              if (settings.SPACE_AFTER_COMMA) {
                commaText += " "
              }
            }

            editor.document.insertString(valueEndOffset, commaText)
            if (terminatedOnCurrentLine(editor, value)) {
              processor.shouldAddNewline = true
            } else {
              processor.myFirstErrorOffset = valueEndOffset + commaText.length
            }

          }
        }

      }

    }

    private fun terminatedOnCurrentLine(editor: Editor, element: PsiElement): Boolean {
      val document = editor.document
      val caretOffset = editor.caretModel.currentCaret.offset
      val elementEndOffset = element.textRange.endOffset
      if (document.getLineNumber(elementEndOffset) != document.getLineNumber(caretOffset)) {
        return false
      }
      val nextLeaf = PsiTreeUtil.nextLeaf(element, true)
      return nextLeaf == null || (nextLeaf is PsiWhiteSpace && nextLeaf.text.contains("\n"))
    }

    private fun isFollowedByTerminal(element: PsiElement, type: IElementType): Boolean {
      val nextLeaf = PsiTreeUtil.nextVisibleLeaf(element)
      return nextLeaf != null && nextLeaf.node.elementType == type
    }
  }

}


