@file:Suppress("ReplaceNotNullAssertionWithElvisReturn")

package com.gmail.blueboxware.libgdxplugin

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.formatting.FormatterTestUtils
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.testFramework.EditorTestUtil
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
abstract class LibGDXCodeInsightFixtureTestCase : LightJavaCodeInsightFixtureTestCase() {

    private fun getTestDataBasePath() =
        FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/")

    override fun getTestDataPath() = FileUtil.toSystemDependentName(getTestDataBasePath() + basePath)

    fun addLibGDX() {
        removeLibGDX()
        addLibrary(getTestDataBasePath() + "/lib/gdx.jar")
    }

    fun addLibGDXSources() {
        addLibGDXSources("gdx.jar", "gdx-sources.jar")
    }

    fun addLibGDX113() {
        removeLibGDX()
        addLibrary(getTestDataBasePath() + "/lib/gdx-1.13.1.jar")
    }

    fun addLibGDX113Sources() {
        addLibGDXSources("gdx-1.13.1.jar", "gdx-1.13.1-sources.jar")
    }

    private fun removeLibGDX() {
        WriteCommandAction.runWriteCommandAction(project) {
            val projectModel = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel

            for (lib in projectModel.libraries) {
                if (lib.name in listOf("gdx.jar", "gdx-sources.jar", "gdx-1.13.1.jar", "gdx-1.13.1-sources.jar")) {
                    PsiTestUtil.removeLibrary(module, lib)
                }
            }

            projectModel.commit()
        }
    }

    private fun addLibGDXSources(baseJar: String, sourceJar: String) =
        WriteCommandAction.runWriteCommandAction(project) {
            LibraryTablesRegistrar.getInstance().getLibraryTable(project).libraries.find { it.name == baseJar }
                ?.let { library ->
                    library.modifiableModel.let {
                        it.addRoot(
                            JarFileSystem.getInstance().findFileByPath(getTestDataBasePath() + "/lib/$sourceJar!/")!!,
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
        }!!.maxByOrNull { file ->
            MavenComparableVersion(file.name.substring(file.name.indexOf("-") + 1, file.name.indexOf(".jar")))
        }!!.absolutePath)
    }

    inline fun <reified T : PsiElement> doAllIntentions(familyNamePrefix: String) =
        doAllIntentions(familyNamePrefix, T::class.java)

    fun <T : PsiElement> doAllIntentions(familyNamePrefix: String, elementType: Class<T>) {

        file.accept(object : PsiRecursiveElementVisitor() {

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

    private fun addLibrary(lib: String) {
        File(lib).let { file ->
            PsiTestUtil.addLibrary(
                myFixture.testRootDisposable, myFixture.module, file.name, file.parent, file.name
            )
        }
    }

    fun configureByFile(filePath: String): PsiFile = myFixture.configureByFile(filePath)

    fun configureByFiles(vararg filePaths: String): Array<PsiFile> = myFixture.configureByFiles(*filePaths)

    fun copyFileToProject(sourceFilePath: String) = myFixture.copyFileToProject(sourceFilePath)

    fun copyFileToProject(sourceFilePath: String, targetPath: String) =
        myFixture.copyFileToProject(sourceFilePath, targetPath)

    fun copyDirectoryToProject(sourceFilePath: String, targetPath: String) =
        myFixture.copyDirectoryToProject(sourceFilePath, targetPath)

    fun configureByText(fileType: FileType, text: String): PsiFile = myFixture.configureByText(fileType, text)

    fun configureByText(fileName: String, text: String): PsiFile = myFixture.configureByText(fileName, text)

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

    fun doTestFormatting(beforeFile: String, afterFileName: String, ext: String) {
        val afterFile = File(testDataPath, afterFileName)
        if (!afterFile.exists()) {
            afterFile.createNewFile()
            fail("$afterFileName does not exist. Created.")
        }
        FormatterTestUtils.testFormatting(
            project,
            ext,
            File(testDataPath, beforeFile).readText(),
            afterFile.readText(),
            FormatterTestUtils.Action.REFORMAT
        )
    }

    fun executeAction(actionId: String) {
        runCommand(editor.document) { EditorTestUtil.executeAction(editor, actionId, true) }
    }

    fun runCommand(document: Document? = null, f: () -> Unit) = CommandProcessor.getInstance().executeCommand(
        project, f, "", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION, document
    )

    fun undo() {
        val fileEditor = FileEditorManager.getInstance(project).getEditors(file.virtualFile).first()
        val undoManager = UndoManager.getInstance(project)

        assertTrue(undoManager.isUndoAvailable(fileEditor))
        undoManager.undo(fileEditor)
    }

    override fun setUp() {
        assertIdeaHomePath()

        super.setUp()
    }

}
