package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.COLON
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.COMMA
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonFile
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.utils.isFollowedByTerminal
import com.gmail.blueboxware.libgdxplugin.utils.terminatedOnCurrentLine
import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/191.4738/json/src/com/intellij/json/editor/smartEnter/JsonSmartEnterProcessor.java
 *
 */
internal class GdxJsonSmartEnterProcessor : SmartEnterProcessorWithFixers() {

    private var shouldAddNewline = false

    init {
        addFixers(GdxJsonObjectPropertyFixer(), GdxJsonArrayElementFixer())
        addEnterProcessors(GdxJsonEnterProcessor())
    }

    override fun collectAdditionalElements(element: PsiElement, result: MutableList<PsiElement>) {

        var parent = element.parent

        while (parent != null && parent !is GdxJsonFile) {
            result.add(parent)
            parent = parent.parent
        }

    }

    private inner class GdxJsonEnterProcessor : FixEnterProcessor() {

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

    private class GdxJsonArrayElementFixer : Fixer<GdxJsonSmartEnterProcessor>() {

        override fun apply(editor: Editor, processor: GdxJsonSmartEnterProcessor, element: PsiElement) {

            if (element is GdxJsonValue && element.parent is GdxJsonArray) {

                if (terminatedOnCurrentLine(editor, element) && !isFollowedByTerminal(element, COMMA)) {
                    editor.document.insertString(element.textRange.endOffset, ",")
                    processor.shouldAddNewline = true
                }

            }

        }
    }

    private class GdxJsonObjectPropertyFixer : Fixer<GdxJsonSmartEnterProcessor>() {

        override fun apply(editor: Editor, processor: GdxJsonSmartEnterProcessor, element: PsiElement) {

            (element as? GdxJsonProperty)?.let { property ->

                property.value.let { propertyValue ->

                    if (propertyValue != null) {

                        if (terminatedOnCurrentLine(editor, propertyValue) && !isFollowedByTerminal(
                                propertyValue,
                                COMMA
                            )
                        ) {
                            editor.document.insertString(propertyValue.textRange.endOffset, ",")
                            processor.shouldAddNewline = true
                        }

                    } else {

                        val keyEndOffset = property.propertyName.textRange.endOffset

                        if (terminatedOnCurrentLine(editor, property.propertyName)
                            && !isFollowedByTerminal(property.propertyName, COLON)
                        ) {
                            processor.myFirstErrorOffset = keyEndOffset + 2
                            editor.document.insertString(keyEndOffset, ": ")
                        }

                    }


                }

            }

        }

    }

}
