package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.intellij.codeInsight.completion.CompletionType


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
class TestCompletion: LibGDXCodeInsightFixtureTestCase() {

  private val EMPTY = setOf<String>()

  private val TESTS = listOf(
          """{ foo: <caret> }""" to (setOf("true", "false", "null") to EMPTY),
          """{ foo: tr<caret> }""" to (setOf("true") to setOf("false", "null")),
          """{ foo: "<caret>" }""" to (setOf("true", "false", "null") to EMPTY),
          """{ foo: "fa<caret>" }""" to (setOf("false") to setOf("true", "null")),
          """[ <caret> ]""" to (setOf("true", "false", "null") to EMPTY),
          """{ <caret>: "" }""" to (EMPTY to setOf("true", "false", "null")),

          """{ a: a, b: [{e: 12, f: g}], c: 2, d: { <caret>, c: 2, e: 1 }}"""
                  to (setOf("a", "b", "d", "f")
                  to setOf("c", "e")),
          """{ a: a, b: [{e: 12, f: g}], c: 2, d: { c: 2, e: 1, <caret> }}"""
                  to (setOf("a", "b", "d", "f")
                  to setOf("c", "e")),
          """{ a: a, b: [{e: 12, f: g}], c: 2, d: { c: 2, e: 1, "<caret>" }}"""
                  to (setOf("a", "b", "d", "f")
                  to setOf("c", "e")),
          """{ a: a, b: [{e: 12, f: g}], c: 2, d: { c: 2, e: 1, <caret>, }}"""
                  to (setOf("a", "b", "d", "f")
                  to setOf("c", "e")),

          """{ a: foo, b: [{e: 12, f: g, fe:f, f: <caret> }], c: 2, d: { c: 2, e: 1,}}"""
                  to (setOf("foo", "12", "g", "f", "2", "1") to setOf("b", "c", "e")),
          """{ a: foo, b: [{e: 12, f: g, fe:f, f: fo<caret> }], c: 2, d: { c: 2, e: 1,}}"""
                  to (setOf("foo") to setOf("b", "c", "e", "12", "g", "f", "2", "1")),
          """{ a: foo, b: [{e: 12, f: g, fe:f, f: "<caret>" }], c: 2, d: { c: 2, e: 1,}}"""
                  to (setOf("foo", "12", "g", "f", "2", "1") to setOf("b", "c", "e")),
          """{ a: foo, b: [{e: 12, f: g, fe:f, f: "fo<caret>" }], c: 2, d: { c: 2, e: 1,}}"""
                  to (setOf("foo") to setOf("b", "c", "e", "12", "g", "f", "2", "1")),
          """[foo, bar, 12, [{g: e}], <caret>]""" to (setOf("foo", "bar", "12", "e") to setOf("g"))
  )

  fun testCompletions() =
          TESTS.forEach {
            doTest(it.first, it.second.first, it.second.second)
          }

  private fun doTest(content: String, expectedResults: Set<String>, notExpectedResults: Set<String>) {
    configureByText("test.lson", content)

    assertNotNull(myFixture.complete(CompletionType.BASIC, 1))

    val result = myFixture.lookupElementStrings ?: throw AssertionError()

    result.forEach { str ->
      assertFalse("Not expected to find '$str'. Content:\n $content", str in notExpectedResults)
    }

    expectedResults.forEach { str ->
      assertTrue(
              "Expected to find '$str'. Found: " +
                      result.joinToString { "'$it'" } +
                      ", Content: '$content'",
              str in result
      )
    }

  }

}