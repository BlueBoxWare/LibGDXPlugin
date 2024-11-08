@file:Suppress("SameParameterValue")

package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.codeInsight.generation.CommentByBlockCommentHandler
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.spellchecker.inspections.SpellCheckingInspection


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
class TestEditorFeatures : LibGDXCodeInsightFixtureTestCase() {

    fun testFolding1() {
        myFixture.testFolding(testDataPath + "folding1.lson")
    }

    fun testFolding2() {
        myFixture.testFolding(testDataPath + "folding2.lson")
    }

    fun testBreadcrumbs1() = doTestBreadcrumbs("1", "items", "3", "value", "2", "foo", "p")

    fun testSpellChecking() {
        myFixture.enableInspections(SpellCheckingInspection::class.java)
        configureByFile("spellChecking.lson")
        myFixture.checkHighlighting(true, false, true)
    }

    fun testCommenting() {
        configureByFileAsGdxJson("comments/comments.json")
        val commentByLineAction = CommentByLineCommentAction()
        commentByLineAction.actionPerformedImpl(project, editor)
        myFixture.checkResultByFile("comments/lineComment.txt")
        editor.caretModel.moveCaretRelatively(0, -1, false, false, false)
        commentByLineAction.actionPerformedImpl(project, editor)
        myFixture.checkResultByFile("comments/noComment.txt")
        editor.caretModel.moveCaretRelatively(0, -1, false, false, false)
        editor.caretModel.moveToOffset(editor.caretModel.visualLineStart)
        editor.caretModel.moveCaretRelatively(editor.caretModel.visualLineEnd - 1, 0, true, false, false)
        val blockCommentAction = CommentByBlockCommentHandler()
        WriteCommandAction.runWriteCommandAction(project) {
            blockCommentAction.invoke(project, editor, editor.caretModel.currentCaret, file)
            myFixture.checkResultByFile("comments/blockComment.txt")
            blockCommentAction.invoke(project, editor, editor.caretModel.currentCaret, file)
            myFixture.checkResultByFile("comments/noComment.txt")
        }
    }

    private fun doTestBreadcrumbs(vararg expectedComponents: String) {
        configureByFile(testname() + ".lson")
        assertOrderedEquals(myFixture.breadcrumbsAtCaret.map { it.text }, *expectedComponents)
    }

    override fun getBasePath() = "/filetypes/json/editor/"

}
