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

@file:Suppress("ReplaceNotNullAssertionWithElvisReturn")

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.search.ProjectAndLibrariesScope
import org.jetbrains.kotlin.idea.base.util.projectScope
import org.jetbrains.kotlin.psi.KtFile

class TestRefactoring : LibGDXCodeInsightFixtureTestCase() {

    fun testRenameTaskJava1() =
        doRenameTaskTest(
            "org.example.BarkTask",
            """
               import t:"org.example.BarkTask" u:"com.example.BarkTask" v:"org.example.BarkTask"
               org.example.BarkTask times:3
            """.trimIndent(),
            """
               import t:"org.example.Foo" u:"com.example.BarkTask" v:"org.example.Foo"
               org.example.Foo times:3
            """.trimIndent()
        )

    fun testRenameTaskJava2() =
        doRenameTaskTest(
            "org.example.BarkTask",
            """
               (org.example.BarkTask times:3) t
            """.trimIndent(),
            """
               (org.example.Foo times:3) t
            """.trimIndent()
        )

    fun testRenameTaskJava3() =
        doRenameTaskTest(
            "org.example.BarkTask",
            """
               (t t:"") org.example.BarkTask times:3
            """.trimIndent(),
            """
               (t t:"") org.example.Foo times:3
            """.trimIndent()
        )

    fun testRenameTaskKotlin1() =
        doRenameTaskTest(
            "org.example.KotlinBarkTask",
            """
               import t: " org.example.KotlinBarkTask "  u:"com.example.KotlinBarkTask" v:"org.example.KotlinBarkTask"
               org.example.KotlinBarkTask times:3
            """.trimIndent(),
            """
               import t:" org.example.Foo " u:"com.example.KotlinBarkTask" v:"org.example.Foo"
               org.example.Foo times:3
            """.trimIndent()
        )

    fun testRenameTaskKotlin2() =
        doRenameTaskTest(
            "org.example.KotlinBarkTask",
            """
               (org.example.KotlinBarkTask times:3) t
            """.trimIndent(),
            """
               (org.example.Foo times:3) t
            """.trimIndent()
        )

    fun testRenameTaskKotlin3() =
        doRenameTaskTest(
            "org.example.KotlinBarkTask",
            """
               (t t:"") org.example.KotlinBarkTask times:3
            """.trimIndent(),
            """
               (t t:"") org.example.Foo times:3
            """.trimIndent()
        )

    fun testRenameAttributeJava1() =
        doRenameAttributeTest(
            "org.example.BarkTask", "times", "foo",
            """
                org.example.BarkTask t:"" times:3
                (t) (org.example.BarkTask t:"" times:3 a:"" times:"") t
            """.trimIndent(),
            """
                org.example.BarkTask t:"" foo:3
                (t) (org.example.BarkTask t:"" foo:3 a:"" foo:"") t
            """.trimIndent()
        )

    fun testRenameAttributeKotlin1() =
        doRenameAttributeTest(
            "org.example.KotlinBarkTask", "times", "foo",
            """
                org.example.KotlinBarkTask t:"" times:3
                (t) (org.example.KotlinBarkTask t:"" times:3 a:"" times:"") t
            """.trimIndent(),
            """
                org.example.KotlinBarkTask t:"" foo:3
                (t) (org.example.KotlinBarkTask t:"" foo:3 a:"" foo:"") t
            """.trimIndent()
        )

    fun testMoveJavaClass() {
        val before = """
           import t:"org.example.BarkTask" u:"com.example.BarkTask" v:"org.example.BarkTask"
           org.example.BarkTask times:3 
           (t t) (org.example.BarkTask times:3) t
        """.trimIndent()
        configureByText(TreeFileType, before)
        runCommand {
            moveJavaClass("org.example.BarkTask", "src.main.java.com.foo")
        }
        myFixture.checkResult(
            """
                import t:"src.main.java.com.foo.BarkTask" u:"com.example.BarkTask" v:"src.main.java.com.foo.BarkTask"
                src.main.java.com.foo.BarkTask times:3
                (t t) (src.main.java.com.foo.BarkTask times:3) t
        """.trimIndent()
        )
        undo()
        myFixture.checkResult(before)
    }

    fun testMoveKotlinClass() {
        val before = """
           import t:"org.example.KotlinBarkTask" u:"com.example.KotlinBarkTask" v:"org.example.KotlinBarkTask"
           org.example.KotlinBarkTask times:3 
           (t t) (org.example.KotlinBarkTask times:3) t
        """.trimIndent()
        configureByText(TreeFileType, before)
        val file = JavaPsiFacade.getInstance(project)
            .findClass(
                "org.example.KotlinBarkTask",
                project.projectScope()
            )!!.containingFile.navigationElement as KtFile
        runCommand {
            moveKotlinFile(file, "src.main.kotlin.com.foo")
        }
        myFixture.checkResult(
            """
                import t:"src.main.kotlin.com.foo.KotlinBarkTask" u:"com.example.KotlinBarkTask" v:"src.main.kotlin.com.foo.KotlinBarkTask"
                src.main.kotlin.com.foo.KotlinBarkTask times:3
                (t t) (src.main.kotlin.com.foo.KotlinBarkTask times:3) t
        """.trimIndent()
        )
        undo()
        myFixture.checkResult(before)
    }

    @Suppress("SameParameterValue")
    private fun doRenameAttributeTest(
        className: String,
        attributeName: String,
        newName: String,
        treeBefore: String,
        treeAfter: String
    ) {
        val clazz = JavaPsiFacade.getInstance(project).findClass(className, ProjectAndLibrariesScope(project))!!
        val field = clazz.findFieldByName(attributeName, false)!!.navigationElement
        doRenameTest(field, newName, treeBefore, treeAfter)
    }

    private fun doRenameTaskTest(
        className: String,
        treeBefore: String,
        treeAfter: String
    ) {
        val clazz = JavaPsiFacade.getInstance(project)
            .findClass(className, ProjectAndLibrariesScope(project))!!
        doRenameTest(clazz, "Foo", treeBefore, treeAfter)
    }

    private fun doRenameTest(elementToRename: PsiElement, newName: String, treeBefore: String, treeAfter: String) {
        configureByText(TreeFileType, treeBefore)
        runCommand {
            myFixture.renameElement(elementToRename, newName)
        }
        myFixture.checkResult(treeAfter)
        undo()
        myFixture.checkResult(treeBefore)
    }

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addAI()

        myFixture.copyDirectoryToProject("testProject", "")
    }

    override fun getBasePath() = "/filetypes/aitree/"

}
