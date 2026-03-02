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


class TestCompletion : LibGDXCodeInsightFixtureTestCase() {

    fun testStatements1() = doTestCompletion("""<caret>""", listOf("import", "subtree", "root"))

    fun testStatements2() = doTestCompletion(
        """
        <caret> a:"b"
        task
    """.trimIndent(), listOf("import", "subtree", "root")
    )

    fun testTaskname1() = doTestCompletion(
        """
        <caret> a:"b"
    """.trimIndent(), listOf(
            "import", "root", "org",
            "com",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "include", "sequence", "wait"
        ), listOf(
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask"  // abstract
        )
    )

    fun testTaskname2() = doTestCompletion(
        """
        com.<caret> a:""
    """.trimIndent(), listOf(
            "badlogic",
        ), listOf(
            "org",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "org.example.Dog",
            "include", "sequence", "wait",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask"  // abstract
        )
    )

    fun testTaskname3() = doTestCompletion(
        """
        Task<caret> a:"b"
    """.trimIndent(), listOf(
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.KotlinBarkTask",
        ), listOf(
            "org.example.ThingDoer",
            "org.example.Dog",
            "java.lang.Object",
            "include", "sequence", "wait",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask", "import", "root", "org",  // abstract
        )
    )

    fun testTasknameQuestionMark1() = doTestCompletion(
        """
        <caret>? a:"b"
    """.trimIndent(), listOf(
            "import", "root", "org",
            "com",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "include", "sequence", "wait"
        ), listOf(
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask"  // abstract
        )
    )

    fun testTasknameQuestionMark2() = doTestCompletion(
        """
        Care<caret>? a:"b"
    """.trimIndent(), listOf(
            "org.example.CareTask",
        ), listOf(
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask", "import", "root", "org",
            "com", "org.example.WalkTask", // abstract
        )
    )

    fun testTasknameQuestionMark3() = doTestCompletion(
        """
        (<caret>?)  t a:"b"
    """.trimIndent(), listOf(
            "org",
            "com",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "include", "sequence", "wait"
        ), listOf(
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask", "import"  // abstract
        )
    )

    fun testGuard1() = doTestCompletion(
        """
        (<caret> a:2) t f:""
    """.trimIndent(), listOf(
            "com", "org",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "include", "sequence", "wait"
        ), listOf(
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask", "import", "root", // abstract
        )
    )

    fun testClassInImport1() = doTestCompletion(
        """
        import a:"<caret>"
    """.trimIndent(), listOf(
            "org",
            "com",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
        ), listOf(
            "include", "sequence", "wait",
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask"  // abstract
        )
    )

    fun testClassInImport2() = doTestCompletion(
        """
        import a:"com.<caret>"
    """.trimIndent(), listOf(
            "badlogic",
        ), listOf(
            "org",
            "org.example.ThingDoer",
            "org.example.KotlinBarkTask",
            "org.example.CareTask",
            "org.example.WalkTask",
            "org.example.Dog",
            "java.lang.Object",
            "com.badlogic.gdx.ai.btree.decorator.UntilSuccess",
            "com.badlogic.gdx.ai.btree.leaf.Wait",
            "com.badlogic.gdx.ai.btree.BranchTask"  // abstract
        )
    )

    fun testImportedAliases1() = doTestCompletion(
        """
            import foo:"" bar:""
            import abc:""
            <caret>
            import xyz:""
        """.trimIndent(),
        listOf("foo", "bar", "abc"), listOf("xyz")
    )

    fun testImportedAliases2() = doTestCompletion(
        """
            import foo:"" bar:""
            import abc:""
            (a) (<caret>?) a
            import xyz:""
        """.trimIndent(),
        listOf("foo", "bar", "abc"), listOf("xyz")
    )

    fun testAttributeOfDefaultTask1() = doTestCompletion(
        """
            parallel <caret>
        """.trimIndent(),
        listOf("policy"), emptyList()
    )

    fun testAttributeOfDefaultTask2() = doTestCompletion(
        """
            parallel <caret> a:""
        """.trimIndent(),
        listOf("policy"), emptyList()
    )

    fun testAttributeOfDefaultTask3() = doTestCompletion(
        """
            parallel <caret> policy:""
        """.trimIndent(),
        emptyList(), listOf("policy")
    )

    fun testAttributeOfDefaultTask4() = doTestCompletion(
        """
            (parallel? a:"" <caret> b:"") a
        """.trimIndent(),
        listOf("policy")
    )

    fun testAttributeOfCustomTask1() = doTestCompletion(
        """
            org.example.TestTask <caret>
        """.trimIndent(), listOf("a1", "a2", "a3"), listOf("a4")
    )

    fun testAttributeOfCustomTask2() = doTestCompletion(
        """
            org.example.TestTask a4:"" <caret> a2:""
        """.trimIndent(), listOf("a1", "a3"), listOf("a4", "a2")
    )

    fun testAttributeOfCustomTaskKotlin1() = doTestCompletion(
        """
            org.example.ThingDoer <caret>
        """.trimIndent(), listOf("string1", "string2"), listOf("string3")
    )

    fun testAttributeOfCustomTaskKotlin2() = doTestCompletion(
        """
            org.example.ThingDoer a4:"" <caret> string1:""
        """.trimIndent(), listOf("string2"), listOf("string3")
    )

    fun testAttributeOfImportedTaskKotlin() = doTestCompletion(
        """
           import a:"org.example.KotlinBarkTask"
           a <caret>
        """.trimIndent(), listOf("times")
    )

    private fun doTestCompletion(
        content: String, expectedCompletionStrings: List<String>, notExpectedCompletionStrings: List<String> = listOf()
    ) {
        super.doTestCompletion(TreeFileType, content, expectedCompletionStrings, notExpectedCompletionStrings)
    }

    override fun setUp() {
        super.setUp()

        addAI()

        myFixture.copyDirectoryToProject("testProject", "")
    }

    override fun getBasePath() = "/filetypes/aitree/"

}
