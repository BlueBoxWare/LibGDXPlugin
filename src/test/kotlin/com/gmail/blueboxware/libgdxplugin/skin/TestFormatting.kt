package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.CodeStyleSettingsManager

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
class TestFormatting : LibGDXCodeInsightFixtureTestCase() {

  fun testDefaultStyle() {
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = true
    doTest("test.skin", "test_after.skin")
  }

  fun testWrapColors() {
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = false
    doTest("test.skin", "test_wrap_colors_after.skin")
  }

  fun test2DefaultStyle() {
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = true
    doTest("test2.skin", "test2_after.skin")
  }


  fun test2WrapColors() {
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = false
    doTest("test2.skin", "test2_wrap_colors_after.skin")
  }

  fun testComments() {
    doTest("test_comments.skin", "test_comments_after.skin")
  }

  fun doTest(before: String, after: String) {
    myFixture.configureByFile(before)
    WriteCommandAction.runWriteCommandAction(null, {
      CodeStyleManager.getInstance(myFixture.project).reformat(myFixture.file)
    })
    myFixture.checkResultByFile(after)
  }

  override fun getBasePath() = "/filetypes/skin/formatting/"
}