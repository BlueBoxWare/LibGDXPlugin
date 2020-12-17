package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections.LibGDXDuplicatePropertyInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections.LibGDXJsonStringProblemsInspector
import com.gmail.blueboxware.libgdxplugin.message


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
class TestSuppressQuickfixes: LibGDXCodeInsightFixtureTestCase() {

  fun test1() = doTest(
          message("suppress.object"),
          """
            {
              <caret>a: a
              a: b
            }
          """.trimIndent(),
          """
            //noinspection DuplicateProperty
            {
              a: a
              a: b
            }
          """.trimIndent()
  )

  fun test2() = doTest(
          message("suppress.file"),
          """
            {
              <caret>a: a
              a: b
            }
          """.trimIndent(),
          """
            //noinspection DuplicateProperty
            {
              a: a
              a: b
            }
          """.trimIndent()
  )

  fun test3() = doTest(
          message("suppress.object"),
          """
            {
              a: [{a: 1}, {a: a, <caret>a: b}]
            }
          """.trimIndent(),
          """
            {
              a: [{a: 1},
                  //noinspection DuplicateProperty
                  {a: a, a: b}]
            }
          """.trimIndent()
  )

  fun test4() = doTest(
          message("suppress.file"),
          """
            {
              a: [{a: 1}, {a: a, <caret>a: b}]
            }
          """.trimIndent(),
          """
            //noinspection DuplicateProperty
            {
              a: [{a: 1}, {a: a, a: b}]
            }
          """.trimIndent()
  )

  fun test5() = doTest(
          message("suppress.file"),
          """
            // Comment
            /* Comment */


            // Comment
            {
              a: [{a: 1}, {a: a, <caret>a: b}]
            }
          """.trimIndent(),
          """
            //noinspection DuplicateProperty
            // Comment
            /* Comment */


            // Comment
            {
              a: [{a: 1}, {a: a, a: b}]
            }
          """.trimIndent()
  )

  fun test6() = doTest(
          message("suppress.object"),
          """
            // Comment
            /* Comment */


            // Comment
            {
              a: [
                {a: 1},
                 // Comment

                 /* Comment */
                {a: a, <caret>a: b}
              ]
            }
          """.trimIndent(),
          """
            // Comment
            /* Comment */


            // Comment
            {
              a: [
                {a: 1},
                 // Comment

                 /* Comment */
                //noinspection DuplicateProperty
                {a: a, a: b}
              ]
            }
          """.trimIndent()
  )

  fun test7() = doTest(
          message("suppress.object"),
          """
            {
              a: [
                {a: 1},
                  // Comment

                  /* Comment */ {a: a, <caret>a: b}
              ]
            }
          """.trimIndent(),
          """
            {
              a: [
                {a: 1},
                  // Comment

                  /* Comment */
                  //noinspection DuplicateProperty
                  {a: a, a: b}
              ]
            }
          """.trimIndent()
  )

  fun test8() = doTest(
          message("suppress.property"),
          """
            {
              a: a
              <caret>a: b
              a: c
            }
          """.trimIndent(),
          """
            {
              a: a
                //noinspection DuplicateProperty
                a: b
              a: c
            }
          """.trimIndent()
  )

  fun test9() = doTest(
          message("suppress.property"),
          """
            {
              foo: {
                a: a, /* c */ <caret>a: b, a: c
              }
            }
          """.trimIndent(),
          """
            {
              foo: {
                a: a, /* c */
                  //noinspection DuplicateProperty
                  a: b, a: c
              }
            }
          """.trimIndent()
  )

  fun test10() = doTest(
          message("suppress.property"),
          """
            {
              foo: {
                a: a, //
                <caret>a: b, a: c
              }
            }
          """.trimIndent(),
          """
            {
              foo: {
                a: a, //
                  //noinspection DuplicateProperty
                  a: b, a: c
              }
            }
          """.trimIndent()
  )

  fun test11() = doTest(
          message("suppress.string"),
          """
           {
            a: [
              <caret>\e
            ],
          }
          """.trimIndent(),
          """
           {
            a: [
              //noinspection InvalidEscape
              \e
            ],
          }
          """.trimIndent()
  )


  fun doTest(familyName: String, content: String, expectedResult: String) {
    myFixture.enableInspections(LibGDXDuplicatePropertyInspection(), LibGDXJsonStringProblemsInspector())
    configureByText("test.lson", content)
    for (intention in myFixture.availableIntentions) {
      if (intention.familyName == familyName) {
        myFixture.launchAction(intention)
      }
    }

    myFixture.checkResult(expectedResult)

  }

  override fun getBasePath() = "filetypes/json/suppressQuickfixes"

}