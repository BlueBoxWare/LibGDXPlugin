package com.gmail.blueboxware.libgdxplugin.bitmapFont

import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontFile
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontParserDefinition
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
class TestParsing : ParsingTestCase("", "fnt", BitmapFontParserDefinition()) {

  fun test1() {
    doTest()
  }

  fun test2() {
    doTest()
  }

  fun test3() {
    doTest()
  }

  fun doTest() {
    doTest(true)

    val bitmapFontFile = myFile as? BitmapFontFile ?: throw AssertionError()

    val expectedCharacterIds = getCharacterIds(myFile.text)

    assertEquals(expectedCharacterIds.size, bitmapFontFile.getCharacters().size)
    assertTrue(bitmapFontFile.getCharacters().map { it.getValue("id") }.containsAll(expectedCharacterIds))

    val expectedPages = getPages(myFile.text)

    assertEquals(expectedPages.size, bitmapFontFile.getPages().size)
    assertTrue(bitmapFontFile.getPages().map { it.getValue("id") }.containsAll(expectedPages.map { it.first }))
    assertTrue(bitmapFontFile.getPages().map { it.getValue("file") }.containsAll(expectedPages.map { it.second }))
  }

  fun getCharacterIds(str: String): List<String> = Regex("""char\s+id=(\d+)""").findAll(str).map { it.groups[1]?.value ?: "" }.toList()

  fun getPages(str: String): List<Pair<String, String>> = Regex("""page\s+id=(\d+)\sfile="([^"]*)"""").findAll(str).map { Pair(it.groups[1]?.value ?: "", it.groups[2]?.value ?: "") }.toList()

  override fun getTestDataPath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/filetypes/bitmapFont/psi")

  override fun skipSpaces() = true

  override fun includeRanges() = true

}