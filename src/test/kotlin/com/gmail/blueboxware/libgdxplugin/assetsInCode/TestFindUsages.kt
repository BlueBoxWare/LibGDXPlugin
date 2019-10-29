package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import com.intellij.usageView.UsageInfo

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
class TestFindUsages: AssetsInCodeCodeInsightFixtureTestCase() {

  fun testFindUsages1() {
    doTest(2)
  }

  fun testFindUsages2() {
    doTest(4)
  }

  fun testFindUsages3() {
    doTest(6, ext = "atlas")
  }

  fun testFindUsagesWithTags1() {
    doTest(2)
  }

  fun testFindUsagesWithTags2() {
    doTest(4)
  }

  fun testFindUsagesWithTags3() {
    doTest(4)
  }

  fun testFindUsages4() {
    copyFileToProject("findUsages/findUsages4.skin")
    val vf = copyFileToProject("findUsages/findUsages4.atlas")
    val atlasFile = PsiManager.getInstance(project).findFile(vf) as? AtlasFile ?: throw AssertionError()
    val target = atlasFile.getPages().flatMap { it.regionList }.find { it.name == "wallpaper" }!!
    val usagesInfos = myFixture.findUsages(target)
    assertEquals(4, usagesInfos.size)
    checkUsages(usagesInfos, target)
  }

  fun doTest(nrOfUsages: Int, ext: String? = "skin") {
    val usagesInfos = myFixture.testFindUsages("findUsages/" + testname() + "." + ext)
    assertEquals(nrOfUsages, usagesInfos.size)
    checkUsages(usagesInfos, myFixture.elementAtCaret as PsiNamedElement)
  }

  private fun checkUsages(usagesInfos: Collection<UsageInfo>, target: PsiNamedElement) {
    for (usageInfo in usagesInfos) {
      usageInfo.element?.let { element ->
        assertEquals(target.name, StringUtil.stripQuotesAroundValue(element.text))

        val references = element.references.filterIsInstance<AssetReference>()
        assertEquals(1, references.size)
        val resolved = references.firstOrNull()?.resolve() as? PsiNamedElement
        assertNotNull(resolved)
        assertEquals(StringUtil.stripQuotesAroundValue(element.text), resolved!!.name)
      }

    }
  }

  override fun setUp() {
    super.setUp()

    if (testname().contains("tags", ignoreCase = true)) {
      addDummyLibGDX199()
    }

    copyFileToProject("findUsages/" + getTestName(false) + ".java")
    copyFileToProject("findUsages/" + getTestName(false) + ".kt")
  }
}