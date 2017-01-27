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

  fun testColonInsertedBetweenClassNameAndBody() {
    doTest()
  }

  fun testColonInsertedAfterResourceName() {
    doTest()
  }

  fun testColonInsertedAfterPropertyName() {
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

  override fun getBasePath() = "/filetypes/skin/smartEnter/"
}