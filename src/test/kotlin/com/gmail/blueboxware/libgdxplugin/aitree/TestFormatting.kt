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

@file:Suppress("SameParameterValue")

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting.TreeCodeStyleSettings
import com.intellij.application.options.CodeStyle
import com.intellij.formatting.FormatterTestUtils
import com.intellij.openapi.actionSystem.IdeActions

class TestFormatting : LibGDXCodeInsightFixtureTestCase() {

    fun testDefaultStyle1() = doTestFormatting("1.tree", "1.default", "tree")

    fun testTooManySpaces() = doTestFormatting("tooManySpaces.tree", "tooManySpaces.after", "tree")

    fun testTabs() {
        (CodeStyle.getSettings(project).getCommonSettings(TreeLanguage).indentOptions ?: throw AssertionError()).apply {
            USE_TAB_CHARACTER = true
            INDENT_SIZE = 3
        }
        doTestFormatting("1.tree", "1.tabs", "tree")
    }

    fun testSpaces() {
        CodeStyle.getSettings(project).getCommonSettings(TreeLanguage).apply {
            SPACE_BEFORE_COLON = true
            SPACE_AFTER_COLON = true
            SPACE_WITHIN_PARENTHESES = true
            SPACE_AFTER_TYPE_CAST = false
        }
        doTestFormatting("1.tree", "1.spaces", "tree")
    }

    fun testDoNotReformatIndentation() {
        CodeStyle.getSettings(project).getCommonSettings(TreeLanguage).apply {
            SPACE_WITHIN_PARENTHESES = true
        }
        CodeStyle.getSettings(project).getCustomSettings(TreeCodeStyleSettings::class.java).apply {
            KEEP_INDENT = true
        }
        doTestFormatting("1.tree", "1.keepIndents", "tree")
    }

    fun testBlanklinesRemoval() {
        (0..3).forEach { count ->
            CodeStyle.getSettings(project).getCommonSettings(TreeLanguage).apply {
                KEEP_BLANK_LINES_IN_CODE = count
            }
            doTestFormatting("blankLines.tree", "blankLines.$count", "tree")
        }
    }

    fun testKeepCommentsAndIndentOnEmptyLines() {
        CodeStyle.getSettings(project).getCustomSettings(TreeCodeStyleSettings::class.java).apply {
            KEEP_INDENTS_ON_EMPTY_LINES = true
            KEEP_COMMENTS = true
        }
        doFormatTest(
            """
                       
                  # Comment
            root
                            
               # Comment
                task1
                task2
        """.trimIndent(), """
                       
                  # Comment
            root
                            
               # Comment
              task1
              task2
        """.trimIndent()
        )
    }

    fun testBackSpace1() = doBackSpaceTest(4)

    fun testBackSpace2() = doBackSpaceTest(5)

    fun testEnter1() = doEnterTest(2)

    fun testEnter2() = doEnterTest(2)

    private fun doFormatTest(before: String, after: String) =
        FormatterTestUtils.testFormatting(
            project,
            "tree",
            before,
            after,
            FormatterTestUtils.Action.REFORMAT
        )

    private fun doBackSpaceTest(repeat: Int) {
        val testName = getTestName(true)
        configureByFile("$testName.tree")
        repeat(repeat) {
            executeAction(IdeActions.ACTION_EDITOR_BACKSPACE)
            val suffix = if (repeat == 1) "" else "-${it + 1}"
            myFixture.checkResultByFile("$testName-after$suffix.tree")
        }
    }

    private fun doEnterTest(repeat: Int) {
        val testName = getTestName(true)
        configureByFile("$testName.tree")
        repeat(repeat) {
            executeAction(IdeActions.ACTION_EDITOR_ENTER)
            val suffix = if (repeat == 1) "" else "-${it + 1}"
            myFixture.checkResultByFile("$testName-after$suffix.tree", true)
        }
    }

    override fun getBasePath() = "/filetypes/aitree/formatting/"

}
