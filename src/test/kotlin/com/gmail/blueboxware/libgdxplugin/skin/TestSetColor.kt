package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import java.awt.Color

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
class TestSetColor : LibGDXCodeInsightFixtureTestCase() {

  fun test1() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test2() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test3() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test4() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test5() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test6() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test7() {
    keepColorOnOneLine(false)
    doTest()
  }

  fun test8() {
    keepColorOnOneLine(false)
    doTest()
  }

  fun test9() {
    keepColorOnOneLine(true)
    doTest()
  }

  fun test10() {
    myFixture.configureByFile("10.skin")
    val colorElement = myFixture.elementAtCaret.firstParent<SkinObject>()!!
    colorElement.setColor(Color(128, 128, 128))?.let { newObject ->
      WriteCommandAction.runWriteCommandAction(myFixture.project) {
        colorElement.replace(newObject)
      }
    }
    myFixture.checkResultByFile("10.after")
  }

  fun testWithTags1() {
    keepColorOnOneLine(true)
    addDummyLibGDX199()
    doTest()
  }

  fun testWithTags2() {
    keepColorOnOneLine(true)
    addDummyLibGDX199()
    doTest()
  }

  private fun keepColorOnOneLine(yesOrNo: Boolean) {
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = yesOrNo
  }

  fun doTest() {
    myFixture.configureByFile(getTestName(true) + ".skin")
    (myFixture.file as? SkinFile).let { skinFile ->
      assertNotNull(skinFile)
      val newColor = skinFile!!.getClassSpecifications("newColor").firstOrNull()?.getResource("color")?.`object`?.asColor(true)
      assertNotNull(newColor)
      val colorObject = skinFile.getClassSpecifications("com.badlogic.gdx.graphics.Color").firstOrNull()?.getResource("color")?.`object`
      assertNotNull(colorObject)
      colorObject!!.setColor(newColor)?.let { newObject ->
        WriteCommandAction.runWriteCommandAction(myFixture.project) {
          colorObject.replace(newObject)
        }
      }
    }
    myFixture.checkResultByFile(getTestName(true) + ".after")
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()

    myFixture.copyFileToProject("ColorArrayHolder.java")
  }

  override fun getBasePath() = "/filetypes/skin/setColor/"

}