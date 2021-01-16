package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserDefinition
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.ParsingTestCase


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
class TestParsing: ParsingTestCase("", "json", GdxJsonParserDefinition()) {

  fun test1() {
    doTest()
  }

  fun test2() {
    doTest()
  }

  fun testErrors() {
    doTest()
  }

  fun testEmptyFile() =
          doCodeTest("")

  fun testEmptyFileWithNewlines() =
          doCodeTest("\n\n\n")

  fun testEmptyObject() =
          doCodeTest("{}")

  fun testEmptyObjectWithNewlines() =
          doCodeTest("{\n\n\n}")

  fun testEmptyObjectWithComma() =
          doCodeTest("{\n,\n}")

  fun testSingleString() =
          doCodeTest("foo")

  fun testObjectWithPrefixedComma() =
          doCodeTest("{,foo:bar,bar:foo}")

  fun testObjectWithPrefixedCommaAndNewlines() =
          doCodeTest("\n{\n,\nfoo\n:\nbar\n,\nbar\n:\nfoo\n}\n")

  fun testObjectWithTrailingComma() =
          doCodeTest("{foo:bar,bar:foo  ,}")

  fun testObjectWithTrailingCommaAndNewline() =
          doCodeTest("{foo:bar,bar:foo\n,}")

  fun testObjectWithTrailingCommaAndNewlines() =
          doCodeTest("\n{\nfoo\n:\nbar\n,\nbar\n:\nfoo   ,\n}\n")

  fun testObjectWithConsecutiveCommas1_error() =
          doCodeTest("{\n,\n,\nfoo:bar\n}")

  fun testObjectWithConsecutiveCommas2_error() =
          doCodeTest("{\nfoo:bar\n,\n,\nfoor:bar}")

  fun testObjectWithConsecutiveCommas3_error() =
          doCodeTest("{\nfoo:bar\n,\n,\nfoo:bar\n}")

  fun testObjectWithNewlinesAndWhitespace() =
          doCodeTest("\n{\n\n  foo   \n\n   :    \n\n   bar   \n  \n   foo  \n\n   :  \n\n    bar\n   \n}\n\n")

  fun testObjectWithNewlinesCommasAndWhitespace() =
          doCodeTest("\n{\n,\n  foo   \n\n   :    \n\n   bar   \n , \n   foo  \n\n   :  \n\n    bar   ,  \n\n}\n\n")

  fun testStringWithQuotes() =
          doCodeTest("""["foo bar"]""")

  fun testStringWithoutQuotes() =
          doCodeTest("[foo]")

  fun testStringWithEmbeddedQuotes() =
          doCodeTest("""[foo"bar"foo]""")

  fun testQuotedStringWithEmbeddedQuotes_error() =
          doCodeTest("""["foo"bar"]""")

  fun testQuotedStringWithEscapedQuotes() =
          doCodeTest("""["foo\"bar"]""")

  fun testQuotedStringWithFunnyChars() =
          doCodeTest("""["';:[]{}=+-_)(*&^%$#@!`~<>?/.,"]""")

  fun testBareKeyWithFunnyChars() =
          doCodeTest("""{ <>,".;"'{][]{"{{}}[[]]]["+_=-)(*&^%$#@!~ : "foo" }""")

  fun testBareStringWithFunnyChars() =
          doCodeTest("""[~`!@#$%^&*-_=+|.'":{[{[:\"]""")

  fun testEscapedDoubleQuotes() =
          doCodeTest("""[ "\"", "\\\"", "\\\\\"", a"b, a\"b, a\\b]""")

  fun testMultiLineString() =
          doCodeTest("[\"\n\nfoo\n\n\"\n]")

  fun testBlockCommentsAroundUnquotedStrings1() =
          doCodeTest("{ /*dsf*/ foo /**/:/**/ foo /**/ }")

  fun testBlockCommentsAroundUnquotedStrings2() =
          doCodeTest("{ /*dsf*/foo/**/:/**/foo/**/ }")

  fun testBlockCommentInKey_error() =
          doCodeTest("{foo/**/bar : \"\"}")

  fun testBlockCommentInUnquotedValue_error() =
          doCodeTest("{foobar : foo/**/bar}")

  fun testEmptyArray() =
          doCodeTest("[\n\n]\n")

  fun testEmptyArrayWithComma() =
          doCodeTest("[\n,\n]")

  fun testEmptyArrayWith2Commas() =
          doCodeTest("[\n,\n,\n]")

  fun testArrayWithStartingAndTrailingComma() =
          doCodeTest("[\n,\nfoo ,\n]")

  fun testArrayWithConsecutiveCommas1_error() =
          doCodeTest("[foo , , bar]")

  fun testArrayWithConsecutiveCommas2_error() =
          doCodeTest("[foo \n, \n, \nbar]")

  fun testNoSpaces() =
          doCodeTest("""{colors:[red,yellow,b,abcdef],}""")

  fun doTest() {
    doTest(true)
  }

  override fun getTestDataPath() =
          FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/filetypes/json/parsing")

  override fun includeRanges() = true

}
