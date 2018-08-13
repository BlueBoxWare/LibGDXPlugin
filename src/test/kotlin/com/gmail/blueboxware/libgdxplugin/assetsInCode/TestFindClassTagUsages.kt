package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages.ClassTagUsageTargetProvider
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter
import junit.framework.TestCase


/*
 * Copyright 2018 Blue Box Ware
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
class TestFindClassTagUsages: LibGDXCodeInsightFixtureTestCase() {

  fun testJava() {
    doTest("TaggedJavaClass.java", 3, "java2")
  }

  fun testKotlin() {
    doTest("TaggedKotlinClass.kt", 3, "kotlin2")
  }

  fun testDuplicateTags() {
    doTest("DuplicateTags.kt", 3, "kotlin2")
  }

  fun doTest(
          filename: String,
          numberOfUsagesToFind: Int,
          tagName: String
  ) {

    myFixture.configureByFile(filename)
    ClassTagUsageTargetProvider().getTargets(myFixture.editor, myFixture.file)?.firstOrNull()?.let { usageTarget ->
      (usageTarget as? PsiElement2UsageTargetAdapter)?.element?.let { targetElement ->
        val usages = myFixture.findUsages(targetElement)
        TestCase.assertEquals(numberOfUsagesToFind, usages.size)
        usages.forEach { usage ->
          TestCase.assertEquals(tagName, (usage.element as? SkinClassName)?.value?.plainName)
        }
        return
      }
    }

    throw AssertionError()

  }

  override fun getBasePath() = "assetsInCode/findClassTagUsages"

  override fun setUp() {
    super.setUp()

    addDummyLibGDX199()
    addAnnotations()

    listOf("skin1.skin", "skin2.skin").forEach {
      myFixture.copyFileToProject(it)
    }

  }

}