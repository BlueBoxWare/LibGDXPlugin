package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.references.ColorsReference
import com.gmail.blueboxware.libgdxplugin.utils.asPlainString
import com.gmail.blueboxware.libgdxplugin.utils.asString
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import junit.framework.TestCase
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateExpression


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
@Suppress("ReplaceNotNullAssertionWithElvisReturn")
class TestColorsHandling : LibGDXCodeInsightFixtureTestCase() {

    fun testCompletionJava() {

        listOf(
            """Colors.get("<caret>");""" to (
                    listOf(
                        "BLUE",
                        "RED",
                        "singlePutJava",
                        "multiJava",
                        "singleGetColorsJava",
                        "singlePutKotlin",
                        "singleGetColorsKotlin",
                        "multiKotlin"
                    ) to listOf("")
                    ),
            """Colors.get("sin<caret>");""" to (
                    listOf(
                        "singlePutJava",
                        "singleGetColorsJava",
                        "singlePutKotlin",
                        "singleGetColorsKotlin"
                    ) to listOf("multiJava", "multiKotlin", "RED")
                    )
        ).forEach { (first, second) ->
            doTestCompletion(
                JavaFileType.INSTANCE,
                """
                import com.badlogic.gdx.graphics.Colors;
                class Test {
                  void m() {
                    $first
                  }
                }
              """.trimIndent(),
                second.first,
                second.second
            )
        }

    }

    fun testCompletionKotlin() {

        listOf(
            """Colors.get("<caret>")""" to (
                    listOf(
                        "BLUE",
                        "RED",
                        "singlePutJava",
                        "multiJava",
                        "singleGetColorsJava",
                        "singlePutKotlin",
                        "singleGetColorsKotlin",
                        "multiKotlin"
                    ) to listOf("")),
            """Colors.get("sin<caret>")""" to (
                    listOf(
                        "singlePutJava",
                        "singleGetColorsJava",
                        "singlePutKotlin",
                        "singleGetColorsKotlin"
                    ) to listOf(
                        "multiJava",
                        "multiKotlin",
                        "RED"
                    ))
        ).forEach { (first, second) ->
            doTestCompletion(
                KotlinFileType.INSTANCE,
                """
                import com.badlogic.gdx.graphics.Colors
                fun
                  val c = $first
                }
              """.trimIndent(),
                second.first,
                second.second
            )
        }

    }

    fun testJavaReferences() {

        listOf(
            """Colors.get("singlePutJava");""",
            """Colors.get("multiJava");""",
            """Colors.get("singleGetColorsJava");""",
            """Colors.getColors().get("singlePutJava");""",
            """Colors.get("singlePutKotlin");""",
            """Colors.getColors().get("multiKotlin");""",
            """Colors.getColors().get("singleGetColorsKotlin");""",
            """Colors.get("YELLOW");"""
        ).forEach {
            doTestReference(
                JavaFileType.INSTANCE, """
        import com.badlogic.gdx.graphics.Colors;
        class Test {
          void m() {
            ${it.replaceFirst("\"", "\"<caret>")}
          }
        }
      """.trimIndent()
            )
        }

        doTestReference(
            JavaFileType.INSTANCE, """
        import com.badlogic.gdx.graphics.Colors;
        class Test {
          void m() {
            Colors.get("nothing");
          }
        }
    """.trimIndent(), shouldFindSomething = false
        )

    }

    fun testKotlinReferences() {

        listOf(
            """Colors.get("singlePutJava")""",
            """Colors.get("multiJava")""",
            """Colors.get("singleGetColorsJava")""",
            """Colors.getColors().get("singlePutJava")""",
            """Colors.get("singlePutKotlin")""",
            """Colors.getColors().get("multiKotlin")""",
            """Colors.getColors().get("singleGetColorsKotlin")""",
            """Colors.get("YELLOW")"""
        ).forEach {
            doTestReference(
                KotlinFileType.INSTANCE, """
        import com.badlogic.gdx.graphics.Colors
        fun f() {
          val c = ${it.replaceFirst("\"", "\"<caret>")}
        }
      """.trimIndent()
            )
        }

        doTestReference(
            KotlinFileType.INSTANCE, """
        import com.badlogic.gdx.graphics.Colors
        fun f() {
          val c = Colors.get("nothing")
        }
    """.trimIndent(), shouldFindSomething = false
        )
    }

    private fun doTestReference(fileType: FileType, content: String, shouldFindSomething: Boolean = true) {

        configureByText(fileType, content)
        val element = file.findElementAt(myFixture.caretOffset)!!.parent.let {
            if (it is KtStringTemplateEntry) it.parent else it
        }
        val originalColorName = plainText(element)

        element.references.filterIsInstance<ColorsReference>().let { references ->
            TestCase.assertEquals(1, references.size)
            val referents = references.first().multiResolve(true)
            TestCase.assertEquals(shouldFindSomething, referents.isNotEmpty())
            referents.map { it.element }.forEach { referent ->
                TestCase.assertEquals(originalColorName, plainText(referent!!))
            }
        }

    }

    private fun plainText(element: PsiElement) =
        (element as? PsiLiteralExpression)?.asString()
            ?: (element as? KtStringTemplateExpression)?.asPlainString()
            ?: throw AssertionError()

    override fun setUp() {

        super.setUp()

        addLibGDX()
        addLibGDXSources()
        addKotlin()

        copyFileToProject("src/JavaColorDefinitions.java")
        copyFileToProject("src/KotlinColorDefinitions.kt")

    }

    override fun getBasePath() = "colorsHandling"

}
