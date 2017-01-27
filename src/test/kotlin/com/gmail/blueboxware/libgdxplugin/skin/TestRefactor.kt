package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase

/*
 * Copyright 2017 Blue Box Ware
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
class TestRefactor : LibGDXCodeInsightFixtureTestCase() {

  fun testRenameResource1() {
    doRenameTest("green")
  }

  fun doRenameTest(newName: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    myFixture.renameElementAtCaret(newName)
    myFixture.checkResultByFile(getTestName(true) + ".after")
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
  }

  override fun getBasePath() = "/filetypes/skin/refactor/"

}