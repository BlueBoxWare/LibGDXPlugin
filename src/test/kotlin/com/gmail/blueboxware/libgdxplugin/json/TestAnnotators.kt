package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonFileType


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
class TestAnnotators: LibGDXCodeInsightFixtureTestCase() {

  fun testColorAnnotator() {
    configureByFile("colorAnnotator.lson")
    myFixture.checkHighlighting(false, false, true)
  }

  fun testTrailingCommaAfterNewline1() = doTest(
          """
            {
              ,
            }
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline2() = doTest(
          """
            {
              foo: bar,
            }
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline3() = doTest(
          """
            {
              foo: bar
              <error>,</error>
            }
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline4() = doTest(
          """
            {
              foo: bar
              /**/<error>,</error>
            }
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline5() = doTest(
          """
            {foo: bar
              <error>,</error>
            }
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline6() = doTest(
          """
            [
              foo  /**/  ,
            ]
          """.trimIndent()
  )

  fun testTrailingCommaAfterNewline7() = doTest(
          """
            [
              foo
              <error>,</error>
            ]
          """.trimIndent()
  )

  fun testMissingClosingBracket1() = doTest(
          """
            <error>{</error>
          """.trimIndent()
  )

  fun testConsecutiveCommas1() = doTest(
          """
            {,<error>,</error>}
          """.trimIndent()
  )

  fun testConsecutiveCommas2() = doTest(
          """
            {,foo:bar,}
          """.trimIndent()
  )

  fun testConsecutiveCommas3() = doTest(
          """
            { ,  /**/  <error>,</error>}
          """.trimIndent()
  )

  fun testConsecutiveCommas4() = doTest(
          """
            {   ,
            <error>,</error>
            }
          """.trimIndent()
  )

  fun testConsecutiveCommas5() = doTest(
          """
            [,foo,] 
          """.trimIndent()
  )

  fun testConsecutiveCommas6() = doTest(
          """
           [,/**/,<error>f</error>oo] 
          """.trimIndent()
  )

  fun testConsecutiveCommas7() = doTest(
          """
            [
             ,
             <error>,</error>
            ]
          """.trimIndent()
  )

  fun testMissingClosingBracket2() {
    myFixture.configureByFile("missingClosingBracket.lson")
    myFixture.checkHighlighting(false, false, false)
  }

  private fun doTest(source: String) {
    myFixture.configureByText(LibGDXJsonFileType.INSTANCE, source)
    myFixture.checkHighlighting(false, false, false)
  }

  override fun getBasePath() = "filetypes/json/annotators/"

}