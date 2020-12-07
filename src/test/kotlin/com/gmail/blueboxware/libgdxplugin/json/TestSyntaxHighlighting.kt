package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase


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
class TestSyntaxHighlighting: LibGDXCodeInsightFixtureTestCase() {

  fun testSyntaxHighlighting1() {
    doTest("1.json")
  }

  fun testSyntaxHighlighting2() {
    doTest("2.json")
  }

  fun testSyntaxHighlighting3() {
    doTest("3.json")
  }

  fun doTest(fileName: String) {
    configureByFileAsGdxJson(fileName)
    myFixture.checkHighlighting(false, true, false)
  }

  override fun getBasePath() = "filetypes/json/syntaxHighlighting"

}