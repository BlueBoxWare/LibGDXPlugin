package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.intellij.application.options.CodeStyle
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings


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
class TestFormatting: LibGDXCodeInsightFixtureTestCase() {

  fun testDefaultStyle1() = doFileTest("1.json", "1.default")

  fun testDefaultStyle2() = doFileTest("2.json", "2.default")

  fun testAlignOnColon1() {
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
    doFileTest("1.json", "1.alignOnColon")
  }

  fun testAlignOnColon2() {
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
    doFileTest("2.json", "2.alignOnColon")
  }

  fun testWrapIfLong1() {
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
    doFileTest("1.json", "1.wrapIfLong")
  }

  fun testWrapIfLong2() {
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
    CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
    doFileTest("2.json", "2.wrapIfLong")
  }

  private fun doFileTest(beforeFile: String, afterFile: String) {
    configureByFileAsGdxJson(beforeFile)
    WriteCommandAction.runWriteCommandAction(null) {
      CodeStyleManager.getInstance(project).reformat(file)
    }
    myFixture.checkResultByFile(afterFile)
  }

  override fun getBasePath() = "/filetypes/json/formatting/"

}