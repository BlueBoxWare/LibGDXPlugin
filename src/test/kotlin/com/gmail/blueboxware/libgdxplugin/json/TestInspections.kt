package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections.LibGDXDuplicatePropertyInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections.LibGDXJsonInvalidEscapeInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections.LibGDXTopLevelValueInspection
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.codeInspection.LocalInspectionTool


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
class TestInspections: LibGDXCodeInsightFixtureTestCase() {

  fun testInvalidEscapesInspection() {
    doFileTest(LibGDXJsonInvalidEscapeInspection())
  }

  fun testDuplicatePropertyInspection() {
    doFileTest(LibGDXDuplicatePropertyInspection())
  }

  fun testToplevelInspection1() = doCodeTest(
          LibGDXTopLevelValueInspection(),
          "<weak_warning></weak_warning>"
  )

  fun testToplevelInspection2() = doCodeTest(
          LibGDXTopLevelValueInspection(),
          "\n// foo \n\n/* */ {} /* */\n//\n"
  )

  fun testToplevelInspection3() = doCodeTest(
          LibGDXTopLevelValueInspection(),
          "// \n /* */ <weak_warning>[]</weak_warning>"
  )

  fun testToplevelInspection4() = doCodeTest(
          LibGDXTopLevelValueInspection(),
          "\n<weak_warning>foo</weak_warning>\n"
  )

  fun testToplevelInspection5() = doCodeTest(
          LibGDXTopLevelValueInspection(),
          "//noinspection GDXToplevel \n[]"
  )

  private fun doFileTest(inspection: LocalInspectionTool) {
    myFixture.enableInspections(inspection::class.java)
    myFixture.testHighlighting(true, false, true, testname() + ".lson")
  }

  private fun doCodeTest(inspection: LocalInspectionTool, text: String) {
    myFixture.enableInspections(inspection::class.java)
    myFixture.configureByText(LibGDXJsonFileType.INSTANCE, text)
    myFixture.checkHighlighting(true, false, true)
  }

  override fun getBasePath() = "/filetypes/json/inspections/"

}