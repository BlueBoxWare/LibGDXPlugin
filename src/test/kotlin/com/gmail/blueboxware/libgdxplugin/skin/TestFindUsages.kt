package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResourceName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.intellij.psi.util.PsiTreeUtil

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
class TestFindUsages : LibGDXCodeInsightFixtureTestCase() {

  fun testFindUsages1() {
    doTest(6)
  }

  fun testFindUsages2() {
    doTest(77)
  }

  fun testFindUsages3() {
    doTest(4)
  }

  fun testFindUsages4() {
    doTest(4)
  }

  fun testFindUsages5() {
    doTest(9)
  }

  fun testFindUsages6() {
    doTest(5)
  }

  fun testFindUsages7() {
    doTest(6)
  }

  fun testFindDrawableUsages() {
    myFixture.copyFileToProject("drawableUsages.skin")
    val usagesInfos = myFixture.testFindUsages("drawableUsages.atlas")
    val origin = PsiTreeUtil.findFirstParent(myFixture.file.findElementAt(myFixture.caretOffset), { it is AtlasRegion })
    assertEquals(6, usagesInfos.size)
    usagesInfos.forEach { usagesInfo ->
      assertNotNull(usagesInfo.element)
      assertTrue(usagesInfo.element is SkinStringLiteral)
      assertEquals(origin, usagesInfo.element?.reference?.resolve())
    }

  }

  fun doTest(nrOfUsages: Int) {
    val usagesInfos = myFixture.testFindUsages(getTestName(true) + ".skin")
    assertEquals(nrOfUsages, usagesInfos.size)
    val classType = (myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent as? SkinResourceName)?.resource?.classSpecification?.classNameAsString
    assertNotNull(classType)
    for (usageInfo in usagesInfos) {
      assertTrue(usageInfo.element is SkinStringLiteral)
      (usageInfo.element as? SkinPropertyValue)?.let { propertyValue ->
        val type = propertyValue.property?.resolveToTypeString()
        assertNotNull(type)
        assertTrue(classType == type || (classType == "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable" && type == "com.badlogic.gdx.scenes.scene2d.utils.Drawable"))
      }
      (usageInfo.element as? SkinStringLiteral)?.let { stringLiteral ->
        assertNotNull((usageInfo.element as? SkinStringLiteral)?.value)
        assertEquals((stringLiteral.reference?.resolve() as? SkinResource)?.name, (usageInfo.element as? SkinStringLiteral)?.value)
      }
    }
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addKotlin()

    myFixture.copyFileToProject("KotlinClass.kt", "/KotlinClass.kt")
  }

  override fun getBasePath() = "/filetypes/skin/findUsages/"

}