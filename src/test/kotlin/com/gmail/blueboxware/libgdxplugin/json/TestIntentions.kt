package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions.GdxJsonAddQuotesIntention
import com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions.GdxJsonMoveArrayElementBackwardIntention
import com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions.GdxJsonMoveArrayElementForwardIntention
import com.gmail.blueboxware.libgdxplugin.testname
import java.util.*


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
class TestIntentions : LibGDXCodeInsightFixtureTestCase() {

    fun testWrapWithQuotes1() =
        doWrapWithQuotesTest(
            """{ fo<caret>o: bar }""",
            """{ "foo": bar }"""
        )

    fun testWrapWithQuotes2() =
        doWrapWithQuotesTest(
            """{ foo: b<caret>ar }""",
            """{ foo: "bar"}"""
        )

    fun testWrapWithQuotes3() =
        doWrapWithQuotesTest(
            """[foo, 1, ba<caret>r]""",
            """
                    [foo, 1,
                        "bar"
                    ]
                  """.trimIndent()
        )

    fun testWrapWithQuotes4() =
        doWrapWithQuotesTest(
            """{ with [{/ we<caret>ird ch4r$: with / weird ]} ch4r$ }""",
            """{ "with [{/ weird ch4r$": with / weird ]} ch4r$ }"""
        )

    fun testWrapWithQuotes5() =
        doWrapWithQuotesTest(
            """{ with [{/ weird ch4r$: with<caret> / weird *()' ch4r$ }""",
            """{ with [{/ weird ch4r$: "with / weird *()' ch4r$"}"""
        )

    fun testWrapWithQuotes6() =
        doWrapWithQuotesTest(
            """{ f"o"<caret>\"o: bar }""",
            """{ "f\"o\"\\\"o": bar }"""
        )

    fun testWrapWithQuotes7() =
        doWrapWithQuotesTest(
            """{ foo: b<caret>""'a\"r }""",
            """{ foo: "b\"\"'a\\\"r"}"""
        )

    fun testWrapWithQuotes8() =
        doWrapWithQuotesTest(
            """{ foo: <caret>1 }""",
            """{ foo: "1"}""".trimIndent()
        )

    fun testWrapWithQuotes9() =
        doWrapWithQuotesTest(
            """{ foo: <caret>null }""",
            """{ foo: "null"}""".trimIndent()
        )

    fun testMoveElementForward1() =
        doMoveArrayElementTest(
            """[<caret>1, 2, 3]""",
            """[2, 1, 3]"""
        )

    fun testMoveElementForward2() =
        doMoveArrayElementTest(
            """[<caret>1]""",
            null
        )

    fun testMoveElementForward3() =
        doMoveArrayElementTest(
            """[2, 3, <caret>1]""",
            null
        )

    fun testMoveElementForward4() =
        doMoveArrayElementTest(
            """[f<caret>oo, bar]""",
            """[bar, foo]"""
        )

    fun testMoveElementForward5() =
        doMoveArrayElementTest(
            """
                    {
                      foo: ["<caret>foo", { bar: [1,2] } ]
                    }
                  """.trimIndent(),
            """
                    {
                      foo: [{ bar: [1,2] }, "foo" ]
                    }
                  """.trimIndent()
        )

    fun testMoveElementForward6() =
        doMoveArrayElementTest(
            """
                    {
                      foo: ["foo", { bar: [<caret>{ 1:2 }, 1, 2] } ]
                    }
                  """.trimIndent(),
            """
                    {
                      foo: ["foo", { bar: [1, { 1:2 }, 2] } ]
                    }
                  """.trimIndent()
        )

    fun testMoveElementForward7() =
        doMoveArrayElementTest(
            """
                    [{}, <caret>[1,2], [3,4]]
                  """.trimIndent(),
            """
                    [{}, [3,4], [1,2]]
                  """.trimIndent()
        )

    fun testMoveElementBack1() =
        doMoveArrayElementTest(
            """[1, <caret>2, 3]""",
            """[2, 1, 3]"""
        )

    fun testMoveElementBack2() =
        doMoveArrayElementTest(
            """[<caret>1, 2, 3]""",
            null
        )

    fun testMoveElementBack3() =
        doMoveArrayElementTest(
            """
                    {
                      foo: ["foo", {<caret> bar: [1,2] } ]
                    }
                  """.trimIndent(),
            """
                    {
                      foo: [{ bar: [1,2] }, "foo" ]
                    }
                  """.trimIndent()
        )

    fun testMoveElementBack4() =
        doMoveArrayElementTest(
            """
                    [{}, <caret>[1,2], [3,4]]
                  """.trimIndent(),
            """
                    [[1,2], {}, [3,4]]
                  """.trimIndent()
        )

    fun testMoveElementBack5() =
        doMoveArrayElementTest(
            """
                    {
                      foo: ["foo", { bar: <caret>[{ 1:2 }, 1, 2] } ]
                    }
                  """.trimIndent(),
            """
                    {
                      foo: [{ bar: [{ 1:2 }, 1, 2] }, "foo" ]
                    }
                  """.trimIndent()
        )

    private fun doWrapWithQuotesTest(content: String, expected: String) {
        configureByText("test.lson", content)
        myFixture.launchAction(GdxJsonAddQuotesIntention())
        myFixture.checkResult(expected)
    }

    private fun doMoveArrayElementTest(content: String, expected: String?) {
        configureByText("test.lson", content)

        val intention =
            if (testname().lowercase(Locale.getDefault()).contains("forward"))
                GdxJsonMoveArrayElementForwardIntention()
            else
                GdxJsonMoveArrayElementBackwardIntention()

        if (expected == null) {
            assertTrue(myFixture.availableIntentions.none { it.text == intention.text })
        } else {
            myFixture.launchAction(intention)
            myFixture.checkResult(expected)
        }

    }

}
