/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*
import com.intellij.navigation.NavigationItem
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference
import com.intellij.psi.util.parentOfType

class TestReferences : LibGDXCodeInsightFixtureTestCase() {

    fun testClassInImport1() =
        doTestClassReference<TreeString>(""" import a:"or<caret>g.example.CareTask" """)

    fun testClassInImport2() =
        doTestClassReference<TreeString>(""" import a:"org.exam<caret>ple.CareTask" """)

    fun testClassInImport3() =
        doTestClassReference<TreeString>(""" import a:"org.example.Care<caret>Task" """)

    fun testClassInImport4() =
        doTestClassReference<TreeString>(""" import a:"com.badlogic.gdx.ai.btree.decorator.Rand<caret>om" """)

    fun testClassInImportKotlin() =
        doTestClassReference<TreeString>(""" import a:"org.example.Kotl<caret>inBarkTask" """)

    fun testClassInImportQuestionMark() =
        doTestClassReference<TreeString>(""" import a:"org.example.Care<caret>Task?" """)

    fun testClassAsTaskname1() =
        doTestClassReference<TreeTaskName>("""org.ex<caret>ample.BarkTask""")

    fun testClassAsTaskname2() =
        doTestClassReference<TreeTaskName>("""java.lang.Object""")

    fun testClassAsTasknameKotlin() =
        doTestClassReference<TreeTaskName>("""org.ex<caret>ample.ThingDoer""")

    fun testClassAsTasknameQuestionMark() =
        doTestClassReference<TreeTaskName>("""org.ex<caret>ample.BarkTask ?""")

    fun testClassAsTaskNameInGuard1() =
        doTestClassReference<TreeTaskName>("""(org.ex<caret>ample.BarkTask) task""")

    fun testClassAsTaskNameInGuard2() =
        doTestClassReference<TreeTaskName>("""(org.ex<caret>ample.BarkTask)""")

    fun testClassAsTaskNameInGuard3() =
        doTestClassReference<TreeTaskName>("""(guard) (org.ex<caret>ample.BarkTask) task""")

    fun testClassAsTaskNameInGuardKotlin() =
        doTestClassReference<TreeTaskName>("""(guard) (org.ex<caret>ample.KotlinBarkTask) task""")

    fun testClassAsTaskNameInGuardQuestionMark1() =
        doTestClassReference<TreeTaskName>("""(org.example.Bark<caret>Task ?) task""")

    fun testClassAsTaskNameInGuardQuestionMark2() =
        doTestClassReference<TreeTaskName>("""(org.ex<caret>ample.BarkTask?)""")

    fun testClassAsTaskNameInGuardQuestionMark3() =
        doTestClassReference<TreeTaskName>("""(guard) (org.example.Ba<caret>rkTask?) task""")

    fun testDefaultAlias1() = doTestDefaultTaskReference("""inclu<caret>de""")

    fun testDefaultAlias2() = doTestDefaultTaskReference("""(wai<caret>t?)""")

    fun testDefaultAlias3() = doTestDefaultTaskReference("""(Wai<caret>t?)""", false)

    fun testImportName1() = doTestImportNameReference(
        """
        import a:""
        <caret>a 
    """.trimIndent(), "a"
    )

    fun testImportName2() = doTestImportNameReference(
        """
        import a:"" b:""
        (<caret>b) a
    """.trimIndent(), "b"
    )

    fun testImportName3() = doTestImportNameReference(
        """
        (<caret>b) a
        import a:"" b:""
    """.trimIndent(), "b", false
    )

    fun testImportName4() = doTestImportNameReference(
        """
        mport a:""
        <caret>a 
    """.trimIndent(), "a", false
    )

    fun testImportName5() = doTestImportNameReference(
        """
        import a:""
        import b:"" a:""
        <caret>a 
    """.trimIndent(), "a"
    )

    fun testAttributeName1() = doTestAttributeNameReference(
        """
            org.example.BarkTask t<caret>imes:3
        """.trimIndent()
    )

    fun testAttributeName2() = doTestAttributeNameReference(
        """
            (org.example.BarkTask a:b t<caret>imes:3)
        """.trimIndent()
    )

    fun testAttributeName3() = doTestAttributeNameReference(
        """
            import a:"" b:"org.example.BarkTask"
            b foo:"" <caret>times:3
        """.trimIndent()
    )

    fun testAttributeName4() = doTestAttributeNameReference(
        """
            include <caret>lazy:true
        """.trimIndent()
    )

    fun testAttributeNameKotlin1() = doTestAttributeNameReference(
        """
           org.example.KotlinBarkTask t<caret>imes:3
        """.trimIndent()
    )

    fun testAttributeNameKotlin2() = doTestAttributeNameReference(
        """
            (org.example.ThingDoer a:b s<caret>tring1:3)
        """.trimIndent()
    )

    fun testAttributeNameKotlin3() = doTestAttributeNameReference(
        """
            import a:"" b:"org.example.KotlinBarkTask"
            b foo:"" <caret>times:3
        """.trimIndent()
    )

    private fun doTestAttributeNameReference(text: String, expectedResult: Boolean = true) {
        myFixture.configureByText(TreeFileType, text)
        val element =
            file.findElementAt(myFixture.caretOffset)?.parentOfType<TreeAttributeName>(true) ?: throw AssertionError()
        element.references.filterIsInstance<TreeAttributeName>().any { ref ->
            ref.resolve().let { target ->
                target is PsiField && target.name == element.name
            }
        }.let { assertEquals(expectedResult, it) }
    }

    private fun doTestDefaultTaskReference(text: String, expectedResult: Boolean = true) {
        myFixture.configureByText(TreeFileType, text)
        val element =
            file.findElementAt(myFixture.caretOffset)?.parentOfType<TreeTaskName>(true) ?: throw AssertionError()
        element.references.filterIsInstance<TreeTaskName>().any { ref ->
            ref.resolve().let { target ->
                target is PsiClass && target.name.equals(element.name, ignoreCase = true)
            }
        }.let { assertEquals(expectedResult, it) }
    }

    private fun doTestImportNameReference(text: String, importName: String, expectedResult: Boolean = true) {
        myFixture.configureByText(TreeFileType, text)
        val element =
            file.findElementAt(myFixture.caretOffset)?.parentOfType<TreeTaskName>(true) ?: throw AssertionError()
        element.references.filterIsInstance<TreeTaskName>().any { ref ->
            ref.resolve().let { target ->
                target is TreeAttribute && target.name == importName
            }
        }.let { assertEquals(expectedResult, it) }
    }

    private inline fun <reified Clazz : TreeElement> doTestClassReference(text: String) {
        myFixture.configureByText(TreeFileType, text)
        val element = file.findElementAt(myFixture.caretOffset)?.parentOfType<Clazz>(true) ?: throw AssertionError()
        element.references.filterIsInstance<JavaClassReference>().any { ref ->
            ref.multiResolve(false).map { it.element }.any { target ->
                target is PsiClass && target.qualifiedName == ((element as? TreeString)?.getValue()
                    ?: (element as NavigationItem).name)
            }
        }.let { assertTrue(it) }
    }

    override fun setUp() {
        super.setUp()

        addAI()

        myFixture.copyDirectoryToProject("testProject", "")
    }

    override fun getBasePath() = "/filetypes/aitree/"

}
