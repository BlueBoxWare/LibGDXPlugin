package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.plugins.groovy.GroovyFileType
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import java.io.File

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
class TestVersionHandling: LibGDXCodeInsightFixtureTestCase() {

  fun testExtractVersionsFromMavenMetaData1() {
    doTestExtractVersionsFromMavenMetaData("libgdx")
  }

  fun testExtractVersionsFromMavenMetaData2() {
    doTestExtractVersionsFromMavenMetaData("kotlin")
  }

  fun testGradleBuildScriptVersionDetection() {
    val tests =
            groovyGradleVersionTests.map { it to GroovyFileType.GROOVY_FILE_TYPE } + kotlinGradleVersionTests.map { it to KotlinFileType.INSTANCE }

    for ((test, fileType) in tests) {
      val expectedLib = test.second.first
      val expectedVersion = MavenComparableVersion(test.second.second)
      var result: Pair<Libraries, MavenComparableVersion?>? = null

      configureByText(fileType, test.first)

      file.accept(object: PsiRecursiveElementVisitor() {
        override fun visitElement(element: PsiElement) {
          super.visitElement(element)
          if (result != null) {
            return
          }
          result = when (element) {
            is GrLiteral -> getLibraryInfoFromGroovyLiteral(element)
            is GrCommandArgumentList -> getLibraryInfoFromGroovyArgumentList(element)
            is GrAssignmentExpression -> getLibraryInfoFromGroovyAssignment(element)
            is KtStringTemplateExpression -> getLibraryInfoFromKotlinString(element)
            is KtValueArgumentList -> getLibraryInfoFromKotlinArgumentList(element)
            else -> null
          }
        }
      })

      assertNotNull("Input: '${test.first}'", result)
      assertEquals(expectedLib, result!!.first)
      assertEquals(expectedVersion, result!!.second)
    }
  }

  private val groovyGradleVersionTests = listOf(
          """gdxVersion = "1.9.3"""" to (Libraries.LIBGDX to "1.9.3"),
          """gdxVersion = '1.9.3'""" to (Libraries.LIBGDX to "1.9.3"),
          """ashleyVersion = '1.7.0a-beta'""" to (Libraries.ASHLEY to "1.7.0a-beta"),
          """allprojects { ext { box2DLightsVersion = '1.4' } }""" to (Libraries.BOX2dLIGHTS to "1.4"),
          """compile "com.badlogicgames.gdx:gdx:1.0.a"""" to (Libraries.LIBGDX to "1.0.a"),
          """compile 'com.badlogicgames.gdx:gdx-box2d:1.2.3-rc1@jar'""" to (Libraries.BOX2D to "1.2.3-rc1"),
          """compile 'group': 'com.underwaterapps.overlap2druntime', name: 'overlap2d-runtime-libgdx', version: '1.0a'""" to (Libraries.OVERLAP2D to "1.0a"),
          """compile 'group': 'com.badlogicgames.gdx', name: "gdx-ai", version: '99.a', d: '1'""" to (Libraries.AI to "99.a"),
          """natives "com.badlogicgames.gdx:gdx:1.2:natives-x86_64"""" to (Libraries.LIBGDX to "1.2")
  )

  private val kotlinGradleVersionTests = listOf(
          """val s = "com.badlogicgames.gdx:gdx:1.9.7"""" to (Libraries.LIBGDX to "1.9.7"),
          """
            fun f(group: String, name: String, version: String? = null) {}
            val s = f(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx", version = "1.9a-BETA")
          """.trimIndent() to (Libraries.OVERLAP2D to "1.9a-BETA")
  )

  private fun doTestExtractVersionsFromMavenMetaData(fileName: String) {
    val file = File("src/test/testdata/versions/$fileName.xml")
    val versions = Library.extractVersionsFromMavenMetaData(file.inputStream())
    assertNotNull(versions)
    versions?.let { foundVersions ->
      val expectedVersions = Regex("""<version>([^<]*)</version>""").findAll(file.inputStream().reader().readText())
      assertEquals(foundVersions.size, expectedVersions.count())
      assertTrue(foundVersions.containsAll(expectedVersions.map { it.groupValues[1] }.toList()))
    }
  }

  override fun setUp() {

    super.setUp()

    WriteCommandAction.runWriteCommandAction(project) {
      FileTypeManager.getInstance().associateExtension(GroovyFileType.GROOVY_FILE_TYPE, "gradle")
    }

  }

}