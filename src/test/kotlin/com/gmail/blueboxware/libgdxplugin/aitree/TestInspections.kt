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

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.inspections.TreeUnknownAttributeInspection
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.inspections.TreeUnknownClassInspection
import com.intellij.codeInspection.LocalInspectionTool
import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass

class TestInspections : LibGDXCodeInsightFixtureTestCase() {

    fun testUnknownClassInspection() =
        doTest(
            TreeUnknownClassInspection::class, """
import bark:<error>"org.example.Barktask"</error>
import bark:<error>"com.example.BarkTask"</error>
import bark:<error>"org.xample.BarkTask"</error>
import bark:"org.example.BarkTask" care:<error>"org.example.Caretask"</error> care:"org.example.CareTask"

import bark:"org.example.BarkTask"
import care:"org.example.CareTask"
import mark:"org.example.MarkTask"
import walk:"org.example.WalkTask"
import rest:"org.example.RestTask"

root
  <error>foo</error>
  selector
    <error>play</error>
    parallel
      care urgentProb:0.8
      org.example.PlayTask
      (org.example.PlayTask) <error>foo</error>
      # noinspection GDXToplevel
      (org.example.PlayTask) <error>foo</error>
      (org.example.PlayTask) org.example.BarkTask
      (org.example.PlayTask) <error>org.example.barkTask</error>
      (org.example.PlayTask) walk 
      (mark) walk 
      (mark) <error>alk</error>
      (<error>foo</error>) walk
      (<error>org.example.layTask</error>) walk 
      (mark) (walk) (<error>org.example.layTask</error>) walk 
      <error>org.example.playTask</error>
      <error>org.xample.PlayTask</error>
    randomSelector
      untilSuccess
        sequence
          bark times:"uniform,1,2"
          walk
          mark
      parallel policy:"selector"
        wait seconds:"triangular,2.5,5.5"
        rest
        """.trimIndent()
        )

    fun testUnknownAttributeInspection() = doTest(
        TreeUnknownAttributeInspection::class, """
import bark:"org.example.BarkTask"
import care:"org.example.CareTask"
import play:"org.example.PlayTask"
import mark:"org.example.MarkTask"

root
  selector
    parallel
      care urgentProb:0.8 <error>urgentprob</error>:0.8
      org.example.PlayTask
    randomSelector
      untilSuccess
        sequence
          (bark times:"uniform,1,2") (bark times:"uniform,1,2" <error>time</error>:3) bark times:"uniform,1,2"
          mark
      parallel policy:"selector" <error>poo</error>:""
        wait seconds:"triangular,2.5,5.5"
        rest <error>a</error>:1 <error>b</error>:2
    """.trimIndent()
    )

    fun testSuppressions() = doTest(
        listOf(
            TreeUnknownClassInspection::class, TreeUnknownAttributeInspection::class
        ), """
# foo
  # file:noinspection GDXUnknownAttribute
 # bar
import bark:<error>"org.example.Barktask"</error>
# file:noinspection GDXUnknownClass
# tree:noinspection GDXUnknownClass
import bark:"com.example.BarkTask"
import bark:<error>"org.xample.BarkTask"</error>
# line:noinspection GDXUnknownClass
import bark:"org.example.BarkTask" care:"org.example.Caretask" care:"org.example.CareTask"

import bark:"org.example.BarkTask"
import care:"org.example.CareTask"
import mark:"org.example.MarkTask"
import walk:"org.example.WalkTask"
import rest:"org.example.RestTask"

root
  <error>foo</error>
  selector
    # line:noinspection GDXUnknownClass
    play
      <error>foo</error>
    parallel
      care urgentProb:0.8
      org.example.PlayTask
      (org.example.PlayTask) foo # line:noinspection GDXUnknownClass
      # noinspection GDXToplevel
      (org.example.PlayTask) <error>foo</error>
      (org.example.PlayTask) org.example.BarkTask
    # noinspection GDXUnknownAttribute, GDXUnknownClass foobar
      (org.example.PlayTask) org.example.barkTask
      (org.example.PlayTask) walk 
      (mark) walk 
      (mark) <error>alk</error>
      (<error>foo</error>) walk
      (<error>org.example.layTask</error>) walk 
      (mark) (walk) (<error>org.example.layTask</error>) walk 
      <error>org.example.playTask</error>
      <error>org.xample.PlayTask</error>
    randomSelector
      # tree:noinspection GDXUnknownClass
      untilSuccess
        sequence
          bark times:"uniform,1,2" foo:""
          walk
          foo
          mark
      parallel policy:"selector"
        wait seconds:"triangular,2.5,5.5"
        rest        
      parallel # tree:noinspection GDXUnknownClass
        foo
          bar
    """.trimIndent()
    )

    private fun <T : LocalInspectionTool> doTest(inspection: KClass<T>, @Language("LibGDXAiTree") text: String) =
        doTest(listOf(inspection), text)

    private fun <T : LocalInspectionTool> doTest(
        inspections: List<KClass<out T>>,
        @Language("LibGDXAiTree") text: String
    ) {
        myFixture.enableInspections(inspections.map { it.java })
        myFixture.configureByText(TreeFileType, text)
        myFixture.checkHighlighting(true, false, true)
    }

    override fun setUp() {
        super.setUp()

        addAI()

        myFixture.copyDirectoryToProject("testProject", "")
    }

    override fun getBasePath() = "/filetypes/aitree/"

}
