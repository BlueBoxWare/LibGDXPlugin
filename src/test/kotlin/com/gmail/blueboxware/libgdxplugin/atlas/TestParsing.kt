package com.gmail.blueboxware.libgdxplugin.atlas

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ParserDefinition
import com.gmail.blueboxware.libgdxplugin.loadAtlas
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.ParsingTestCase
import java.io.File

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
class TestParsing : ParsingTestCase("", "atlas", Atlas2ParserDefinition()) {

    fun test1() {
        doParseTest("1.atlas")
    }

    fun test2() {
        doParseTest("2.atlas")
    }

    fun test3() {
        doParseTest("3.atlas")
    }

    private fun doParseTest(file: String) {
        doTest(true)

        val expected = loadAtlas(File(testDataPath, file))
        val actual = loadAtlas(myFile as Atlas2File)
        assertEquals(expected, actual)
    }

    override fun getTestDataPath() =
        FileUtil
            .toSystemDependentName(
                System.getProperty("user.dir") + "/src/test/testdata/filetypes/atlas/psi"
            )

    override fun skipSpaces() = true

    override fun includeRanges() = true

}
