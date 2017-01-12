package skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessors
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import getTestDataPathFromProperty

class TestSmartEnter : LightCodeInsightFixtureTestCase() {

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

  override fun getTestDataPath() = getTestDataPathFromProperty() + "/filetypes/skin/smartEnter/"
}