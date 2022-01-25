package com.gmail.blueboxware.libgdxplugin.atlas

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.testname
import com.gmail.blueboxware.libgdxplugin.utils.firstParent


/*
 * Copyright 2022 Blue Box Ware
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
class TestFindUsages : LibGDXCodeInsightFixtureTestCase() {

    fun test1() {
        myFixture.copyFileToProject("Test.java")
        doTest(7)
    }

    fun doTest(nrOfUsages: Int) {
        myFixture.copyFileToProject(testname() + ".skin")
        val usagesInfos = myFixture.testFindUsages(testname() + ".atlas")
        assertEquals(nrOfUsages, usagesInfos.size)
        val origin = file.findElementAt(myFixture.caretOffset)?.firstParent<Atlas2Region>()
        usagesInfos.forEach { usagesInfo ->
            assertNotNull(usagesInfo.element)
            assertEquals(origin, usagesInfo.element?.references?.firstOrNull()?.resolve())
        }
    }

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addDummyLibGDX199()
        addAnnotations()

    }

    override fun getBasePath() = "/filetypes/atlas/findUsages/"

}
