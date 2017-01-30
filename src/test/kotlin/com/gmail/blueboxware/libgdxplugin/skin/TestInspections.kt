package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinMalformedColorStringInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinNonExistingClassInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinNonExistingFieldInspection
import com.intellij.codeInspection.LocalInspectionTool

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
class TestInspections : LibGDXCodeInsightFixtureTestCase() {

  fun testNonExistingClassInspection() {
    doTest(SkinNonExistingClassInspection())
  }

  fun testNonExistingFieldInspection() {
    doTest(SkinNonExistingFieldInspection())
  }

  fun testMalformedColorStringInspection() {
    doTest(SkinMalformedColorStringInspection())
  }

  private fun doTest(inspection: LocalInspectionTool) {
    myFixture.enableInspections(inspection)
    myFixture.testHighlighting(false, false, false, getTestName(true) + ".skin")
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
  }

  override fun getBasePath() = "/filetypes/skin/inspections/"
}