package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserDefinition
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.ParsingTestCase

/*
 * Copyright 2016 Blue Box Ware
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
class TestSkinParsing : ParsingTestCase("", "json", SkinParserDefinition()) {

  fun doTest() {
    doTest(true)
  }

  fun test1() {
    doTest()
  }

  fun test2() {
    doTest()
  }

  fun test3() {
    doTest()
  }

  fun test4() {
    doTest()
  }

  fun test5() {
    doTest()
  }

  fun test6() {
    doTest()
  }

  fun test7() {
    doTest()
  }

  override fun getTestDataPath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/filetypes/skin/psi")

  override fun skipSpaces() = true

  override fun includeRanges() = true

}
