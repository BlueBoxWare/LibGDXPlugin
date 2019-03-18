package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.utils.getLibraryInfoFromIdeaLibrary
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryImpl
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.kotlin.psi.psiUtil.startOffset
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
abstract class LibGDXCodeInsightFixtureTestCase: LightCodeInsightFixtureTestCase() {

  private fun getTestDataBasePath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/")

  override fun getTestDataPath() = FileUtil.toSystemDependentName(getTestDataBasePath() + basePath)

  fun addLibGDX() = addLibrary(getTestDataBasePath() + "/lib/gdx.jar")

  fun addLibGDXSources() =
          WriteCommandAction.runWriteCommandAction(project) {
            ProjectLibraryTable.getInstance(project).libraries.find { it.name == "gdx.jar" }?.let { library ->
              library.modifiableModel.let {
                it.addRoot(JarFileSystem.getInstance().findFileByPath(getTestDataBasePath() + "/lib/gdx-sources.jar!/")!!, OrderRootType.SOURCES)
                it.commit()
              }
            }
          }

  fun addKotlin() = addLibrary(getTestDataBasePath() + "/lib/kotlin-runtime.jar")

  fun addFreeType() = addLibrary(getTestDataBasePath() + "/lib/gdx-freetype.jar")

  fun addAnnotations() {
    addLibrary(File("build/libs/").listFiles { _, name ->
      name.startsWith("libgdxpluginannotations-") && !name.contains("sources")
    }.sortedByDescending { file ->
      MavenComparableVersion(file.name.substring(file.name.indexOf("-") + 1, file.name.indexOf(".jar")))
    }.first().absolutePath)
  }

  fun addDummyLibGDX199() = addDummyLibrary(Libraries.LIBGDX, "1.9.9")

  fun removeDummyLibGDX199() = removeDummyLibrary(Libraries.LIBGDX, "1.9.9")

  fun addDummyLibrary(library: Libraries, version: String) {

    WriteCommandAction.writeCommandAction(project).run<Throwable> {

      val projectModel = ServiceManager.getService(project, ProjectLibraryTable::class.java)?.modifiableModel
              ?: throw AssertionError()

      for (lib in projectModel.libraries) {
        getLibraryInfoFromIdeaLibrary(lib)?.let { (libraries) ->
          if (libraries == library) {
            projectModel.removeLibrary(lib)
          }
        }
      }

      val libraryModel = (projectModel.createLibrary(library.library.artifactId) as LibraryImpl).modifiableModel

      libraryModel.addRoot("/" + library.library.groupId + "/" + library.library.artifactId + "/" + version + "/", OrderRootType.CLASSES)

      libraryModel.commit()
      projectModel.commit()
    }

  }

  inline fun <reified T: PsiElement> doAllIntentions(familyNamePrefix: String) =
          doAllIntentions(familyNamePrefix, T::class.java)

  fun <T: PsiElement> doAllIntentions(familyNamePrefix: String, elementType: Class<T>) {

    myFixture.file?.accept(object: PsiRecursiveElementVisitor() {

      override fun visitElement(element: PsiElement?) {
        super.visitElement(element)

        if (element != null && elementType.isInstance(element)) {
          myFixture.editor.caretModel.moveToOffset(element.startOffset)
          myFixture.availableIntentions.forEach {
            if (it.familyName.startsWith(familyNamePrefix)) {
              myFixture.launchAction(it)
            }
          }
        }

      }
    })

  }

  private fun assertIdeaHomePath() {
    val path = System.getProperty(PathManager.PROPERTY_HOME_PATH)
    if (path == null || path == "") {
      throw AssertionError("Set ${PathManager.PROPERTY_HOME_PATH} to point to the IntelliJ source directory")
    }
  }

  private fun removeDummyLibrary(library: Libraries, version: String? = null) {

    WriteCommandAction.runWriteCommandAction(project) {

      val projectModel = ServiceManager.getService(project, ProjectLibraryTable::class.java)?.modifiableModel
              ?: throw AssertionError()

      for (lib in projectModel.libraries) {
        getLibraryInfoFromIdeaLibrary(lib)?.let { (thisLibrary, thisVersion) ->
          if (thisLibrary == library && (version == null || version == thisVersion.canonical)) {
            projectModel.removeLibrary(lib)
          }
        }
      }

      projectModel.commit()

    }

  }

  private fun addLibrary(lib: String) {
    File(lib).let { file ->
      PsiTestUtil.addLibrary(
              myFixture.testRootDisposable,
              myFixture.module,
              file.name,
              file.parent,
              file.name
      )
    }
    project.getComponent(VersionManager::class.java).updateUsedVersions()
  }

  fun doTestCompletion(
          fileName: String,
          content: String,
          expectedCompletionStrings: List<String>,
          notExpectedCompletionStrings: List<String> = listOf()
  ) {

    myFixture.configureByText(fileName, content)

    val completionResults = myFixture.complete(CompletionType.BASIC, 0)

    if (completionResults == null) {

      // the only item was auto-completed?
      assertEquals("Got only 1 result. Expected results: $expectedCompletionStrings. Content: \n'$content'", 1, expectedCompletionStrings.size)
      val text = myFixture.editor.document.text
      val expectedString = expectedCompletionStrings.first()
      val msg = "\nExpected string '$expectedString' not found. Content: '$content'"
      assertTrue(msg, text.contains(expectedString))

    } else {

      val strings = myFixture.lookupElementStrings
      assertNotNull(strings)

      for (expected in expectedCompletionStrings) {
        assertTrue("Expected to find $expected, content:\n$content\nFound: $strings", expected in strings!!)
      }

      for (notExpected in notExpectedCompletionStrings) {
        assertFalse("Not expected to find '$notExpected'. Content: '$content'", strings!!.contains(notExpected))
      }

    }

  }

  override fun setUp() {
    assertIdeaHomePath()

    Library.TEST_URL = "http://127.0.0.1/maven/"

    super.setUp()

    project.getComponent(VersionManager::class.java)?.updateUsedVersions()
  }

}