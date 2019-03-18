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
class TestFormatting: LibGDXCodeInsightFixtureTestCase() {

  fun testWithError1() = doTest(
          """
            {
            Color {
            c: {}
            }
            Color: {
            default: {}
            }
            }
          """.trimIndent(),
          """
            {
              Color {
                c: { }
              }
              Color: {
                default: { }
              }
            }
          """.trimIndent()
  )

  fun testWithError2() = doTest(
          """
            {
            Color:
            c: {}
            }
            Color: {
            default: {}
            }
            }
          """.trimIndent(),
          """
            {
              Color:
              c: {
              }
            }
            Color: {
              default: {
            }
            }
            }
          """.trimIndent()
  )

  fun testWithError3() = doTest(
          """
            {
            Color
            c: {}
            }
            Color: {
            default: {}
            }
            }
          """.trimIndent(),
          """
            {
              Color
              c: {
              }
            }
            Color: {
              default: {
            }
            }
            }
          """.trimIndent()
  )

  fun testWithError4() = doTest(
          """
            {
            Color: {
            c: {
            a:
            b: 1,
            r: 1,
            g: 1
            }
            }
            }
          """.trimIndent(),
          """
            {
              Color: {
                c: {
                  a:
                  b: 1,
                  r: 1,
                  g: 1
                }
              }
            }
          """.trimIndent()
  )

  fun testWithError5() = doTest(
          """
            {
            Color: {
            c: {
            a: 1
            b
            r: 1,
            g: 1
            }
            }
            }
          """.trimIndent(),
          """
            {
              Color: {
                c: {
                  a: 1
                  b
                  r: 1,
                  g: 1
                }
              }
            }
          """.trimIndent()
  )

  fun testWithError6() = doTest(
          """
            {
            Color: {
            c: {
            a: 1
            b 1
            r: 1,
            g: 1
            }
            }
            }
          """.trimIndent(),
          """
            {
              Color: {
                c: {
                  a: 1
                  b 1
                  r: 1,
                  g: 1
                }
              }
            }
          """.trimIndent()
  )

  fun testDefaultStyle() {
    @Suppress("DEPRECATION")
    // COMPAT: CodeStyle#getCustomSettings() introduced in 181
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = true
    doFileTest("test.skin", "test_after.skin")
  }

  fun testWrapColors() {
    @Suppress("DEPRECATION")
    // COMPAT: CodeStyle#getCustomSettings() introduced in 181
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = false
    doFileTest("test.skin", "test_wrap_colors_after.skin")
  }

  fun test2DefaultStyle() {
    @Suppress("DEPRECATION")
    // COMPAT: CodeStyle#getCustomSettings() introduced in 181
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = true
    doFileTest("test2.skin", "test2_after.skin")
  }


  fun test2WrapColors() {
    @Suppress("DEPRECATION")
    // COMPAT: CodeStyle#getCustomSettings() introduced in 181
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = false
    doFileTest("test2.skin", "test2_wrap_colors_after.skin")
  }

  fun testComments() {
    doFileTest("test_comments.skin", "test_comments_after.skin")
  }

  private fun doFileTest(before: String, after: String) {
    myFixture.configureByFile(before)
    WriteCommandAction.runWriteCommandAction(null) {
      CodeStyleManager.getInstance(myFixture.project).reformat(myFixture.file)
    }
    myFixture.checkResultByFile(after)
  }

  private fun doTest(before: String, after: String) {
    myFixture.configureByText("skin.skin", before)
    WriteCommandAction.runWriteCommandAction(null) {
      CodeStyleManager.getInstance(myFixture.project).reformat(myFixture.file)
    }
    myFixture.checkResult(after)
  }

  override fun getBasePath() = "/filetypes/skin/formatting/"
}