package com.gmail.blueboxware.libgdxplugin.atlas

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasParserDefinition
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.ParsingTestCase

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
class TestParsing: ParsingTestCase("", "atlas", AtlasParserDefinition()) {

  fun test1() {
    doTest(listOf(3, 9, 13))
  }

  fun test2() {
    doTest(listOf(4, 3, 3))
  }

  fun doTest(regionsPerPage: List<Int>) {
    doTest(true)

    val pages = (myFile as? AtlasFile)?.getPages()
    assertNotNull(pages)
    pages?.let { foundPages ->
      assertEquals(regionsPerPage.size, foundPages.size)
      for ((index, size) in regionsPerPage.withIndex()) {
        assertEquals(size, foundPages[index].regionList.size)
      }
    }
  }

  override fun getTestDataPath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/filetypes/atlas/psi")

  override fun skipSpaces() = true

  override fun includeRanges() = true

}
