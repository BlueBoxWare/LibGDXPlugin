package skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResourceName
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
class TestFindUsages : LightCodeInsightFixtureTestCase() {

  fun testFindUsages1() {
    doTest(1)
  }

  fun testFindUsages2() {
    doTest(74)
  }

  fun testFindUsages3() {
    doTest(2)
  }

  fun doTest(nrOfUsages: Int) {
    val usagesInfos = myFixture.testFindUsages(getTestName(true) + ".skin")
    assertEquals(nrOfUsages, usagesInfos.size)
    val classType = (myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent as? SkinResourceName)?.resource?.classSpecification?.classNameAsString
    assertNotNull(classType)
    for (usageInfo in usagesInfos) {
      assertTrue(usageInfo.element is SkinPropertyValue)
      (usageInfo.element as? SkinPropertyValue)?.let { propertyValue ->
        val type = propertyValue.property?.resolveToTypeString()
        assertNotNull(type)
        assertEquals(classType, type)
      }
    }
  }

  override fun setUp() {
    super.setUp()

    PsiTestUtil.addLibrary(myFixture.module, getTestDataPathFromProperty() + "/lib/gdx.jar")
  }

  override fun getTestDataPath() = getTestDataPathFromProperty() + "/filetypes/skin/findUsages/"

}