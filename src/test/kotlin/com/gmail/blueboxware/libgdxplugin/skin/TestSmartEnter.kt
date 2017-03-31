package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessors
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction

class TestSmartEnter : LibGDXCodeInsightFixtureTestCase() {

  fun testColonInsertedAfterClassname() {
    doTest()
  }

  fun testColonInsertedAfterClassname2() {
    doTest()
  }

  fun testColonInsertedAfterClassname3() {
    doTest()
  }

  fun testColonInsertedAfterClassname4() {
    doTest()
  }

  fun testColonInsertedBetweenClassNameAndBody() {
    doTest()
  }

  fun testColonInsertedAfterResourceName() {
    doTest()
  }

  fun testColonInsertedAfterResourceName2() {
    doTest()
  }

  fun testColonInsertedAfterResourceName3() {
    doTest()
  }

  fun testColonInsertedAfterPropertyName() {
    doTest()
  }

  fun testColonInsertedAfterPropertyName2() {
    doTest()
  }

  fun testArrayNoCommaInsertedBeforeComma() {
    doTest()
  }

  fun testArrayCommaInsertedBeforeItem() {
    doTest()
  }

  fun testArrayCommaAfterLastItem() {
    doTest()
  }

  fun testArrayCommaAndNewlineAfterItem() {
    doTest()
  }

  fun doTest() {
    myFixture.configureByFile(getTestName(true) + ".skin")

    val processors = SmartEnterProcessors.INSTANCE.forKey(LibGDXSkinLanguage.INSTANCE)

    object: WriteCommandAction<Unit>(myFixture.project) {
      override fun run(result: Result<Unit>) {
        val editor = myFixture.editor
        for (processor in processors) {
          processor.process(myFixture.project, editor, myFixture.file)
        }
      }
    }.execute()

    myFixture.checkResultByFile(getTestName(true) + "_after.skin", true)
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()

    myFixture.copyFileToProject("ColorArrayHolder.java")
  }

  override fun getBasePath() = "/filetypes/skin/smartEnter/"
}