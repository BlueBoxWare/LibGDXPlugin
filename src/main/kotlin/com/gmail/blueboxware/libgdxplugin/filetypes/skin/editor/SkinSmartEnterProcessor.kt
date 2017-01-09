package com.gmail.blueboxware.libgdxplugin.filetypes.skin.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
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
    addFixers(SkinObjectPropertyFixer())
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

  private class SkinObjectPropertyFixer : SmartEnterProcessorWithFixers.Fixer<SkinSmartEnterProcessor>() {

    override fun apply(editor: Editor, processor: SkinSmartEnterProcessor, element: PsiElement) {

      val parent = element.context?.parent ?: return

      var key: PsiElement? = null
      var value: PsiElement? = null

      if (parent is SkinPropertyName) {
        key = parent
        value = parent.property?.propertyValue
      } else if (parent is SkinPropertyValue) {
        key = parent.property?.propertyName
        value = parent
      } else if (parent is SkinClassSpecification) {
        key = parent.className
        value = parent.resources
      }

      if (value != null && value.text != "") {
        if (terminatedOnCurrentLine(editor, value) && !isFollowedByTerminal(value, SkinElementTypes.COMMA)) {
          editor.document.insertString(value.textRange.endOffset, ",")
          processor.shouldAddNewline = true
        }
      } else if (key != null) {
        val keyEndOffset = key.textRange.endOffset
        if (terminatedOnCurrentLine(editor, key) && !isFollowedByTerminal(key, SkinElementTypes.COLON)) {
          processor.myFirstErrorOffset = keyEndOffset + 2
          editor.document.insertString(keyEndOffset, ": ")
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


