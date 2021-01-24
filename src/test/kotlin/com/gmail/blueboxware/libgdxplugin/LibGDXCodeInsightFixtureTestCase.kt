@file:Suppress("ReplaceNotNullAssertionWithElvisReturn")

package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.utils.getLibraryInfoFromIdeaLibrary
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.gmail.blueboxware.libgdxplugin.versions.VersionService
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
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
abstract class LibGDXCodeInsightFixtureTestCase: LightJavaCodeInsightFixtureTestCase() {

  private fun getTestDataBasePath() =
          FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/")

  override fun getTestDataPath() =
          FileUtil.toSystemDependentName(getTestDataBasePath() + basePath)

  fun addLibGDX() =
          addLibrary(getTestDataBasePath() + "/lib/gdx.jar")

  fun addLibGDXSources() =
          WriteCommandAction.runWriteCommandAction(project) {
            LibraryTablesRegistrar.getInstance().getLibraryTable(project).libraries.find { it.name == "gdx.jar" }
                    ?.let { library ->
                      library.modifiableModel.let {
                        it.addRoot(
                                JarFileSystem
                                        .getInstance()
                                        .findFileByPath(getTestDataBasePath() + "/lib/gdx-sources.jar!/")!!,
                                OrderRootType.SOURCES
                        )
                        it.commit()
                      }
                    }
          }

  fun addKotlin() = addLibrary(getTestDataBasePath() + "/lib/kotlin-runtime.jar")

  fun addFreeType() = addLibrary(getTestDataBasePath() + "/lib/gdx-freetype.jar")

  fun addAnnotations() {
    addLibrary(File("build/libs/").listFiles { _, name ->
      name.startsWith("libgdxpluginannotations-") && !name.contains("sources")
    }!!.maxBy { file ->
      MavenComparableVersion(file.name.substring(file.name.indexOf("-") + 1, file.name.indexOf(".jar")))
    }!!.absolutePath)
  }

  fun addDummyLibGDX199() = addDummyLibrary(Libraries.LIBGDX, "1.9.9")

  fun removeDummyLibGDX199() = removeDummyLibrary(Libraries.LIBGDX, "1.9.9")

  fun addDummyLibrary(library: Libraries, version: String) {

    WriteCommandAction.writeCommandAction(project).run<Throwable> {

      val projectModel = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel

      for (lib in projectModel.libraries) {
        getLibraryInfoFromIdeaLibrary(lib)?.let { (libraries) ->
          if (libraries == library) {
            projectModel.removeLibrary(lib)
          }
        }
      }

      projectModel.getLibraryByName(library.library.artifactId)?.let {
        projectModel.removeLibrary(it)
      }

      val libraryModel =
              (projectModel.createLibrary(library.library.artifactId) as LibraryEx).modifiableModel

      libraryModel.addRoot(
              "/" + library.library.groupId + "/" + library.library.artifactId + "/" + version + "/",
              OrderRootType.CLASSES
      )

      libraryModel.commit()
      projectModel.commit()
    }

  }

  inline fun <reified T: PsiElement> doAllIntentions(familyNamePrefix: String) =
          doAllIntentions(familyNamePrefix, T::class.java)

  fun <T: PsiElement> doAllIntentions(familyNamePrefix: String, elementType: Class<T>) {

    file.accept(object: PsiRecursiveElementVisitor() {

      override fun visitElement(element: PsiElement) {
        super.visitElement(element)

        if (elementType.isInstance(element)) {
          editor.caretModel.moveToOffset(element.startOffset)
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

      val projectModel = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel

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
    project.service<VersionService>().updateUsedVersions()
  }

  fun configureByFile(filePath: String): PsiFile =
          myFixture.configureByFile(filePath)

  fun configureByFiles(vararg filePaths: String): Array<PsiFile> =
          myFixture.configureByFiles(*filePaths)

  fun copyFileToProject(sourceFilePath: String) =
          myFixture.copyFileToProject(sourceFilePath)

  fun copyFileToProject(sourceFilePath: String, targetPath: String) =
          myFixture.copyFileToProject(sourceFilePath, targetPath)

  fun copyDirectoryToProject(sourceFilePath: String, targetPath: String) =
          myFixture.copyDirectoryToProject(sourceFilePath, targetPath)

  fun configureByText(fileType: FileType, text: String): PsiFile =
          myFixture.configureByText(fileType, text)

  fun configureByText(fileName: String, text: String): PsiFile =
          myFixture.configureByText(fileName, text)

  fun configureByFileAsGdxJson(filePath: String): PsiFile =
          myFixture.configureByFile(filePath).apply { markAsGdxJson() }

  fun doTestCompletion(
          fileName: String,
          content: String,
          expectedCompletionStrings: List<String>,
          notExpectedCompletionStrings: List<String> = listOf()
  ) {

    configureByText(fileName, content)

    val completionResults = myFixture.complete(CompletionType.BASIC, 0)

    if (completionResults == null) {

      // the only item was auto-completed?
      assertEquals(
              "Got only 1 result. Expected results: $expectedCompletionStrings. Content: \n'$content'",
              1,
              expectedCompletionStrings.size
      )
      val text = editor.document.text
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

  fun runCommand(f: () -> Unit) =
          CommandProcessor
                  .getInstance()
                  .executeCommand(
                          project,
                          f,
                          "",
                          null,
                          UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION
                  )

  fun undo() {
    val fileEditor = FileEditorManager.getInstance(project).getEditors(file.virtualFile).first()
    val undoManager = UndoManager.getInstance(project)

    assertTrue(undoManager.isUndoAvailable(fileEditor))
    undoManager.undo(fileEditor)
  }

  override fun setUp() {
    assertIdeaHomePath()

    Library.TEST_URL = "http://127.0.0.1/maven/"

    super.setUp()

    project.getComponent(VersionService::class.java)?.updateUsedVersions()
  }

}