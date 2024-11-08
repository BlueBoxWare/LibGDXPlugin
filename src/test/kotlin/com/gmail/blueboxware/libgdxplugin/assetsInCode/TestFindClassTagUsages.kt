package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages.ClassTagFindUsagesHandlerFactory
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.references.LibGDXTagUsageTargetProvider
import com.intellij.find.findUsages.PsiElement2UsageTargetAdapter
import com.intellij.psi.PsiElement
import com.intellij.usageView.UsageInfo
import com.intellij.util.ArrayUtil
import com.intellij.util.CommonProcessors
import junit.framework.TestCase
import java.util.*


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
class TestFindClassTagUsages : LibGDXCodeInsightFixtureTestCase() {

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

        configureByFile(filename)
        LibGDXTagUsageTargetProvider().getTargets(editor, file)?.firstOrNull()?.let { usageTarget ->
            (usageTarget as? PsiElement2UsageTargetAdapter)?.element?.let { targetElement ->
                val usages = findUsages(targetElement)
                TestCase.assertEquals(numberOfUsagesToFind, usages.size)
                usages.forEach { usage ->
                    TestCase.assertEquals(tagName, (usage.element as? SkinClassName)?.value?.plainName)
                }
                return
            }
        }

        throw AssertionError()

    }

    private fun findUsages(targetElement: PsiElement): Collection<UsageInfo> {
        val handler =
            ClassTagFindUsagesHandlerFactory().createFindUsagesHandler(targetElement, false) ?: throw AssertionError()
        val processor = CommonProcessors.CollectProcessor<UsageInfo>(Collections.synchronizedList(mutableListOf()))
        val psiElements = ArrayUtil.mergeArrays(handler.primaryElements, handler.secondaryElements)
        val options = handler.getFindUsagesOptions(null)
        for (element in psiElements) {
            handler.processElementUsages(element, processor, options)
        }
        return processor.results
    }

    override fun getBasePath() = "assetsInCode/findClassTagUsages"

    override fun setUp() {
        super.setUp()

        addDummyLibGDX199()
        addAnnotations()

        listOf("skin1.skin", "skin2.skin").forEach {
            copyFileToProject(it)
        }

    }

}
