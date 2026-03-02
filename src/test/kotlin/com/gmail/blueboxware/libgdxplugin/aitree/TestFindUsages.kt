/*
 * Copyright 2026 Blue Box Ware
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

@file:Suppress("ReplaceNotNullAssertionWithElvisReturn", "SameParameterValue")

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.findUsages.DefaultImportUsageSearcher
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeAttributeName
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeElement
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.TreeTaskName
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.ProjectAndLibrariesScope
import com.intellij.psi.util.parentOfType
import com.intellij.usages.Usage
import com.intellij.usages.UsageInfo2UsageAdapter

class TestFindUsages : LibGDXCodeInsightFixtureTestCase() {

    fun testImportName() = doTestImportName(
        """
        (b) b
        import a:"" <caret>b:""
        (b) a b:""
        (a) b b:""
        (a) (b) c
        (b?) b?
    """.trimIndent(), 5
    )

    fun testClass1() = doTestClass("org.example.BarkTask", 7)

    fun testKotlinClass() = doTestClass("org.example.KotlinBarkTask", 2)

    fun testDefaultClass() {
        addAI()
        addLibGDX()
        myFixture.copyDirectoryToProject("testProject", "")
        val clazz = JavaPsiFacade.getInstance(project)
            .findClass("com.badlogic.gdx.ai.btree.branch.Selector", ProjectAndLibrariesScope(project))
            ?: throw AssertionError()
        val usages: MutableSet<Usage> = mutableSetOf()
        DefaultImportUsageSearcher().processElementUsages(clazz, {
            usages.add(it)
            true
        }, FindUsagesOptions(project))
        assertEquals(2, usages.size)
        for (usage in usages) {
            assertTrue((usage as UsageInfo2UsageAdapter).usageInfo.element!!.references.any { it.resolve() == clazz })
        }
    }

    fun testCustomField() = doTestField("org.example.BarkTask", "times", 3)

    fun testCustomFieldKotlin() = doTestField("org.example.KotlinBarkTask", "times", 2)

    fun testDefaultClassField1() = doTestField("com.badlogic.gdx.ai.btree.leaf.Wait", "seconds", 1)

    fun testDefaultClassField2() = doTestField("com.badlogic.gdx.ai.btree.branch.Parallel", "policy", 1)

    private fun doTestField(className: String, fieldName: String, nrOfUsages: Int) {
        addAI()
        myFixture.copyDirectoryToProject("testProject", "")
        val clazz = JavaPsiFacade.getInstance(project).findClass(className, ProjectAndLibrariesScope(project))!!
        val field = clazz.findFieldByName(fieldName, false)!!.navigationElement
        val usages = myFixture.findUsages(field).filter { it.element is TreeElement }
        assertEquals(nrOfUsages, usages.size)
        for (usage in usages) {
            assertTrue((usage.element as PsiTreeAttributeName).name == fieldName)
        }
    }

    private fun doTestClass(fqn: String, nrOfUsages: Int) {
        addAI()
        myFixture.copyDirectoryToProject("testProject", "")
        val clazz = JavaPsiFacade.getInstance(project).findClass(fqn, ProjectAndLibrariesScope(project))
            ?: throw AssertionError()
        val usages = myFixture.findUsages(clazz)
        assertEquals(nrOfUsages, usages.size)
        for (usage in usages) {
            assertTrue(usage.element!!.references.any { it.isReferenceTo(clazz) })
        }
    }

    private fun doTestImportName(text: String, nrOfUsages: Int) {
        myFixture.configureByText(TreeFileType, text)
        val element =
            file.findElementAt(myFixture.caretOffset)?.parentOfType<TreeAttributeName>(true) ?: throw AssertionError()
        val usages = myFixture.findUsages(element)
        assertEquals(nrOfUsages, usages.size)
        for (usage in usages) {
            assertTrue((usage.element as TreeTaskName).reference!!.isReferenceTo(element))
        }
    }

    override fun getBasePath() = "/filetypes/aitree/"

}
