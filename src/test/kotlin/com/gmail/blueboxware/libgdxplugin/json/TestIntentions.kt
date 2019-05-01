package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions.GdxJsonAddQuotesIntention


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
class TestIntentions: LibGDXCodeInsightFixtureTestCase() {

  fun testWrapWithQuotes1() =
          doTest(
                  """{ fo<caret>o: bar }""",
                  """{ "foo": bar }"""
          )

  fun testWrapWithQuotes2() =
          doTest(
                  """{ foo: b<caret>ar }""",
                  """{ foo: "bar"}"""
          )

  fun testWrapWithQuotes3() =
          doTest(
                  """[foo, 1, ba<caret>r]""",
                  """
                    [foo, 1,
                        "bar"
                    ]
                  """.trimIndent()
          )

  fun testWrapWithQuotes4() =
          doTest(
                  """{ with [{/ we<caret>ird ch4r$: with / weird ]} ch4r$ }""",
                  """{ "with [{/ weird ch4r$": with / weird ]} ch4r$ }"""
          )

  fun testWrapWithQuotes5() =
          doTest(
                  """{ with [{/ weird ch4r$: with<caret> / weird *()' ch4r$ }""",
                  """{ with [{/ weird ch4r$: "with / weird *()' ch4r$"}"""
          )

  fun testWrapWithQuotes6() =
          doTest(
                  """{ f"o"<caret>\"o: bar }""",
                  """{ "f\"o\"\\\"o": bar }"""
          )

  fun testWrapWithQuotes7() =
          doTest(
                  """{ foo: b<caret>""'a\"r }""",
                  """{ foo: "b\"\"'a\\\"r"}"""
          )

  fun testWrapWithQuotes8() =
          doTest(
                  """{ foo: <caret>1 }""",
                  """{ foo: "1"}""".trimIndent()
          )

  fun testWrapWithQuotes9() =
          doTest(
                  """{ foo: <caret>null }""",
                  """{ foo: "null"}""".trimIndent()
          )

  fun doTest(content: String, expected: String) {
    configureByText("test.lson", content)
    myFixture.launchAction(GdxJsonAddQuotesIntention())
    myFixture.checkResult(expected)
  }

}