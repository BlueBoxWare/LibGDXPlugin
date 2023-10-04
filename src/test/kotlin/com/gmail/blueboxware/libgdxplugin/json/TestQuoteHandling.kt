package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonFileType
import com.intellij.testFramework.LightPlatformCodeInsightTestCase


/*
 * Copyright 2021 Blue Box Ware
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
class TestQuoteHandling : LibGDXCodeInsightFixtureTestCase() {

    fun testDoubleQuote1() = doTest(
        "<caret>",
        "\"\""
    )

    fun testDoubleQuote2() = doTest(
        "\"<caret>\"",
        "\"\""
    )

    fun testDoubleQuoteAtEndOfUnquotedString() = doTest(
        "[foo<caret>]",
        "[foo\"]"
    )

    fun testSingleQuote() = doTest(
        "<caret>",
        "'",
        '\''
    )

    fun testBackSpaceInQuotes() = doTest(
        "\"<caret>\"",
        "",
        '\b'

    )

    fun testBackSpaceInQuotesInUnquotedString() = doTest(
        "foo\"<caret>\"bar",
        "foo\"bar",
        '\b'
    )

    fun testDoubleQuoteInUnquotedString() = doTest(
        "foo<caret>bar",
        "foo\"bar"
    )

    fun doTest(source: String, expected: String, char: Char = '\"') {
        myFixture.configureByText(LibGDXJsonFileType, source)
        if (char == '\b') {
            LightPlatformCodeInsightTestCase.backspace(editor, project)
        } else {
            myFixture.type(char)
        }
        myFixture.checkResult(expected)
    }

}
