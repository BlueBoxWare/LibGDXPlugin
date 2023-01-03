package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.testname
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.gmail.blueboxware.libgdxplugin.utils.findClass
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsSkin
import com.gmail.blueboxware.libgdxplugin.utils.psiFacade
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesProcessor
import com.intellij.refactoring.move.moveClassesOrPackages.SingleSourceRootMoveDestination
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor
import org.jetbrains.kotlin.idea.base.util.projectScope
import org.jetbrains.kotlin.idea.core.moveCaret
import org.jetbrains.kotlin.idea.refactoring.move.changePackage.KotlinChangePackageRefactoring
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPackageDirective
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.findDescendantOfType

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
@Suppress("ReplaceNotNullAssertionWithElvisReturn")
class TestRefactor : LibGDXCodeInsightFixtureTestCase() {

    fun testRenameResource() {
        configureByFile("ColorArrayHolder.java")
        configureByFile("renameResource.skin")
        doSimpleTest {
            myFixture.renameElementAtCaret("green")
        }
    }

    fun testRenameParentResource() {
        addDummyLibGDX199()
        addKotlin()
        copyFileToProject("KotlinClass3.kt")
        configureByFile("renameParentResource.skin")
        doSimpleTest {
            myFixture.renameElementAtCaret("foo")
        }
    }

    fun testRenameResourceWithTags() {
        configureByFile("ColorArrayHolder.java")
        configureByFile("renameResourceWithTags.skin")
        doSimpleTest {
            myFixture.renameElementAtCaret("green")
        }
    }

    fun testRenameJavaField() {
        configureByFile("ColorArrayHolder.java")
        val fieldToRename = myFixture.elementAtCaret
        configureByFile("renameJavaField.skin")
        doSimpleTest {
            myFixture.renameElement(fieldToRename, "someColors")
        }
    }

    fun testRenameJavaFieldWithTags() {
        configureByFile("ColorArrayHolder.java")
        val fieldToRename = myFixture.elementAtCaret
        configureByFile("renameJavaFieldWithTags.skin")
        doSimpleTest {
            myFixture.renameElement(fieldToRename, "someColors")
        }
    }

    fun testRenameKotlinField() {
        configureByFile("KotlinClass.kt")
        val property = file.childOfType<KtProperty>()!!
        configureByFile("ColorArrayHolder.java")
        configureByFile("renameKotlinField.skin")
        doSimpleTest {
            myFixture.renameElement(property, "fooBar")
        }
    }

    fun testRenameKotlinFieldWithTags() {
        configureByFile("KotlinClass.kt")
        val property = file.childOfType<KtProperty>()!!
        configureByFile("ColorArrayHolder.java")
        configureByFile("renameKotlinFieldWithTags.skin")
        doSimpleTest {
            myFixture.renameElement(property, "fooBar")
        }
    }


    fun testRenameJavaClass() {
        configureByFile("JavaClass.java")
        val classToRename = myFixture.elementAtCaret
        configureByFile("renameJavaClass.json")
        doSimpleTest("json") {
            myFixture.renameElement(classToRename, "MyClass")
        }
    }

    fun testRenameJavaClassWithTags() {
        configureByFile("JavaClass.java")
        val classToRename = myFixture.elementAtCaret
        configureByFile("renameJavaClassWithTags.skin")
        doSimpleTest("skin") {
            myFixture.renameElement(classToRename, "MyClass")
        }
    }

    fun testRenameJavaInnerClass() {
        configureByFile("JavaClass.java")
        val topLevelClass = myFixture.elementAtCaret as PsiClass
        val innerClass = topLevelClass.innerClasses.first()
        configureByFile("renameJavaInnerClass.skin")
        doSimpleTest {
            myFixture.renameElement(innerClass, "FooBar")
        }
    }

    fun testRenameKotlinClass() {
        configureByFile("KotlinClass.kt")
        val classToRename = myFixture.elementAtCaret
        configureByFile("renameKotlinClass.skin")
        doSimpleTest {
            myFixture.renameElement(classToRename, "MyClass")
        }
    }

    fun testRenameKotlinClassWithTags() {
        configureByFile("KotlinClass.kt")
        val classToRename = myFixture.elementAtCaret
        configureByFile("renameKotlinClassWithTags.skin")
        doSimpleTest {
            myFixture.renameElement(classToRename, "MyClass")
        }
    }

    fun testRenameKotlinInnerClass() {
        configureByFile("KotlinClass.kt")
        val innerClass =
            (myFixture.elementAtCaret as KtClass)
                .declarations
                .find {
                    (it as? KtClass)?.fqName?.shortName()?.asString() == "Inner"
                }
        configureByFile("renameKotlinInnerClass.json")
        doSimpleTest("json") {
            myFixture.renameElement(innerClass!!, "FooBar")
        }
    }

    fun testRenamePackage() {
        copyFileToProject("JavaClass2.java", "com/example/JavaClass2.java")
        configureByFiles("renamePackage.skin", "KotlinClass.kt")
        val pkg = project.psiFacade().findPackage("com.example") ?: throw AssertionError()
        doSimpleTest {
            myFixture.renameElement(pkg, "foo")
        }
    }

    fun testMovePackage() {
        copyDirectoryToProject("org", "org")
        copyFileToProject("JavaClass2.java", "com/example/JavaClass2.java")
        copyFileToProject("TopLevelClass.kt", "com/example/TopLevelClass.kt")
        copyFileToProject("KotlinClass3.kt", "com/example/KotlinClass3.kt")
        configureByFile("movePackage.json")
        doSimpleTest("json") {
            movePackage("com.example", "org.something")
        }
    }

    fun testMoveJavaClass() {
        copyDirectoryToProject("org", "org")
        configureByFiles("moveJavaClass.skin", "JavaClass.java")
        doSimpleTest {
            moveJavaClass("com.example.JavaClass", "org.something")
        }
    }

//  fun testMoveKotlinFile() {
//    copyDirectoryToProject("org", "org")
//    val file = configureByFile("KotlinClass.kt") as KtFile
//    configureByFile("changeKotlinPackageDirective1.skin")
//    moveKotlinFile(file, "org.something")
//    myFixture.checkResultByFile("changeKotlinPackageDirective1.skin") // no changes
//  }

    fun testMoveKotlinFile() {
        copyDirectoryToProject("org", "org")
        val kotlinFile = configureByFile("KotlinClass.kt") as KtFile
        val skinFile = configureByFile("changeKotlinPackageDirective1.skin")
        moveKotlinFile(kotlinFile, "org.something")
        val newPackageName = kotlinFile.findDescendantOfType<KtPackageDirective>()!!.fqName.asString()
        val expected = skinFile
            .text
            .replace(
                Regex("""com\.example\.KotlinClass([:$])"""),
                "$newPackageName.KotlinClass$1"
            )
        myFixture.checkResult(expected, true) // no changes
    }

    fun testChangeKotlinPackageDirective1() =
        doTestChangeKotlinPackageDirective(
            "changeKotlinPackageDirective1.skin",
            "changeKotlinPackageDirective1.after",
            "KotlinClass.kt",
            "org.something"
        )

    fun testChangeKotlinPackageDirective2() =
        doTestChangeKotlinPackageDirective(
            "changeKotlinPackageDirective2.skin",
            "changeKotlinPackageDirective2.after",
            "TopLevelClass.kt",
            "org.something"
        )

    fun testChangeKotlinPackageDirective3() =
        doTestChangeKotlinPackageDirective(
            "changeKotlinPackageDirective3.skin",
            "changeKotlinPackageDirective3.after",
            "KotlinClass.kt",
            ""
        )

    fun testChangeKotlinPackageDirectiveManually() {
        configureByFiles("KotlinClass2.kt", "changeKotlinPackageDirective1.skin")
        myFixture.type("foo")
        commit()
        myFixture.checkResultByFile(
            "changeKotlinPackageDirective1.skin",
            "changeKotlinPackageDirective1.skin",
            false
        )
    }

    fun testAddKotlinPackageDirectiveManually() {
        configureByFiles("TopLevelClass.kt", "changeKotlinPackageDirective2.skin")
        editor.moveCaret(0)
        myFixture.type("package foo.bar\n")
        commit()
        myFixture.checkResultByFile(
            "changeKotlinPackageDirective2.skin",
            "changeKotlinPackageDirective2.skin",
            false
        )
    }

    fun testRemoveKotlinPackageDirectiveManually() {
        configureByFiles("KotlinClass2.kt", "changeKotlinPackageDirective1.skin")
        val endOfPackageDirective = (file as KtFile).packageDirective!!.endOffset
        editor.caretModel.moveToOffset(endOfPackageDirective)
        myFixture.type("\b".repeat(100))
        commit()
        myFixture.checkResultByFile(
            "changeKotlinPackageDirective1.skin",
            "changeKotlinPackageDirective1.skin",
            false
        )
    }

    private fun doSimpleTest(extension: String = "skin", f: () -> Unit) {
        if (extension != "skin") {
            project.markFileAsSkin(file.virtualFile)
        }
        runCommand(f)
        myFixture.checkResultByFile(testname() + ".after")
        undo()
        myFixture.checkResultByFile(testname() + ".$extension")
    }

    private fun moveKotlinFile(file: KtFile, newPackageName: String) {

        val pkg = project.psiFacade().findPackage(newPackageName)
        assertNotNull(pkg)

        val dirs = pkg!!.directories

        WriteAction.run<Throwable> {
            MoveFilesOrDirectoriesProcessor(
                project,
                arrayOf(file),
                dirs[0],
                true,
                true,
                null,
                null
            ).run()
        }

        commit()

    }

    private fun moveJavaClass(className: String, newPackageName: String) {

        BaseRefactoringProcessor.ConflictsInTestsException.withIgnoredConflicts<Throwable> {

            val clazz = project.findClass(className, project.projectScope()) ?: throw AssertionError()
            val pkg = project.psiFacade().findPackage(newPackageName) ?: throw AssertionError()
            val dirs = pkg.directories

            MoveClassesOrPackagesProcessor(
                project,
                arrayOf(clazz),
                SingleSourceRootMoveDestination(
                    PackageWrapper.create(
                        JavaDirectoryService.getInstance().getPackage(dirs[0])
                    ),
                    dirs[0]
                ),
                true,
                false,
                null
            ).run()

            commit()

        }

    }

    private fun movePackage(packageName: String, newPackageName: String) {

        BaseRefactoringProcessor.ConflictsInTestsException.withIgnoredConflicts<Throwable> {

            val oldPackage = project.psiFacade().findPackage(packageName) ?: throw AssertionError()
            val newPackage = project.psiFacade().findPackage(newPackageName) ?: throw AssertionError()
            val dirs = newPackage.directories

            MoveClassesOrPackagesProcessor(
                project,
                arrayOf(oldPackage),
                SingleSourceRootMoveDestination(
                    PackageWrapper.create(
                        JavaDirectoryService.getInstance().getPackage(dirs[0])
                    ),
                    dirs[0]
                ),
                true,
                false,
                null
            ).run()

            commit()

        }

    }

    private fun doTestChangeKotlinPackageDirective(
        beforeFileName: String,
        afterFileName: String,
        kotlinFileName: String,
        newPackageName: String
    ) {

        val kotlinFile = configureByFile(kotlinFileName) as KtFile
        configureByFile(beforeFileName)

        runCommand {
            KotlinChangePackageRefactoring(kotlinFile).run(FqName(newPackageName))
        }

        commit()

        myFixture.checkResultByFile(afterFileName)

        undo()

        myFixture.checkResultByFile(beforeFileName)

    }

    private fun commit() {

        PsiDocumentManager.getInstance(project).commitAllDocuments()
        FileDocumentManager.getInstance().saveAllDocuments()

    }

    override fun setUp() {
        super.setUp()

        addLibGDX()
        if (testname().contains("tags", ignoreCase = true)) {
            addAnnotations()
            addDummyLibGDX199()
        }

    }

    override fun getBasePath() = "/filetypes/skin/refactor/"

}
