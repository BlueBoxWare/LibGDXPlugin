package skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import getTestDataPathFromProperty

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
class TestSetColor : LightCodeInsightFixtureTestCase() {

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

  fun keepColorOnOneLine(yesOrNo: Boolean) {
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

    PsiTestUtil.addLibrary(myFixture.module, getTestDataPathFromProperty() + "/lib/gdx.jar")
  }

  override fun getTestDataPath() = getTestDataPathFromProperty() + "/filetypes/skin/setColor/"

}