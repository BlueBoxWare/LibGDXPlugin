package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.psi.util.parentOfType


/*
 * Copyright 2021 Blue Box Ware
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
@Suppress("SameParameterValue")
class TestFindUsages: LibGDXCodeInsightFixtureTestCase() {

  fun testFindUsages1() {
    doTest(3)
  }

  private fun doTest(nrOfUsages: Int) {
    val usages = myFixture.testFindUsages(testname() + ".lson")
    val selectedElement = file.findElementAt(myFixture.caretOffset)?.parentOfType<GdxJsonProperty>(true)
            ?: throw AssertionError()
    assertEquals(nrOfUsages, usages.size)
    for (usage in usages) {
      assertTrue((usage.element as GdxJsonProperty).reference?.isReferenceTo(selectedElement) == true)
    }
  }

  override fun getBasePath() = "/filetypes/json/findUsages/"

}