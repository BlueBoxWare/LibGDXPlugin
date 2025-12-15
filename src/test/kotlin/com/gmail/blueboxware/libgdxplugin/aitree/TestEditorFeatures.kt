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

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.intellij.application.options.CodeStyle
import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction
import java.io.File

class TestEditorFeatures : LibGDXCodeInsightFixtureTestCase() {

    fun testComment() = doTestCommenter()

    fun testComment2() {
        CodeStyle.getSettings(project).getCommonSettings(TreeLanguage).apply {
            LINE_COMMENT_AT_FIRST_COLUMN = false
            LINE_COMMENT_ADD_SPACE = false
        }
        doTestCommenter()
    }

    private fun doTestCommenter() {
        val name = getTestName(true)
        myFixture.configureByFile("$name.tree")
        val action = CommentByLineCommentAction()
        action.actionPerformedImpl(project, myFixture.editor)
        val expected = File("$testDataPath$name.after.tree").readText().replace("<caret>", "")
        myFixture.checkResult(expected)

        myFixture.configureByFile("$name.after.tree")
        action.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResultByFile("$name.after2.tree")


    }

    override fun getBasePath() = "/filetypes/aitree/editor/"

}
