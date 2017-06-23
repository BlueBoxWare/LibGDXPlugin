package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsSkin
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.undo.UndoManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.refactoring.PackageWrapper
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesProcessor
import com.intellij.refactoring.move.moveClassesOrPackages.SingleSourceRootMoveDestination
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor
import com.intellij.util.ActionRunner
import org.jetbrains.kotlin.idea.core.moveCaret
import org.jetbrains.kotlin.idea.refactoring.move.changePackage.KotlinChangePackageRefactoring
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.endOffset

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
class TestRefactor : LibGDXCodeInsightFixtureTestCase() {

  fun testRenameResource() {
    myFixture.configureByFile("ColorArrayHolder.java")
    myFixture.configureByFile("renameResource.skin")
    runCommand {
      myFixture.renameElementAtCaret("green")
    }
    myFixture.checkResultByFile("renameResource.after")
    undo()
    myFixture.checkResultByFile("renameResource.skin")
  }

  fun testRenameJavaField() {
    myFixture.configureByFile("ColorArrayHolder.java")
    val fieldToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameJavaField.skin")
    runCommand {
      myFixture.renameElement(fieldToRename, "someColors")
    }
    myFixture.checkResultByFile("renameJavaField.after")
    undo()
    myFixture.checkResultByFile("renameJavaField.skin")
  }

  fun testRenameKotlinField() {
    myFixture.configureByFile("KotlinClass.kt")
    val property = PsiTreeUtil.findChildOfType(myFixture.file, KtProperty::class.java) as KtProperty
    myFixture.configureByFile("ColorArrayHolder.java")
    myFixture.configureByFile("renameKotlinField.skin")
    runCommand {
      myFixture.renameElement(property, "fooBar")
    }
    myFixture.checkResultByFile("renameKotlinField.after")
    undo()
    myFixture.checkResultByFile("renameKotlinField.skin")
  }

  fun testRenameJavaClass() {
    myFixture.configureByFile("JavaClass.java")
    val classToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameJavaClass1.json")
    markFileAsSkin(project, file.virtualFile)
    runCommand {
      myFixture.renameElement(classToRename, "MyClass")
    }
    myFixture.checkResultByFile("renameJavaClass1.after")
    undo()
    myFixture.checkResultByFile("renameJavaClass1.json")
  }

  fun testRenameJavaInnerClass() {
    myFixture.configureByFile("JavaClass.java")
    val topLevelClass = myFixture.elementAtCaret as PsiClass
    val innerClass = topLevelClass.innerClasses.first()
    myFixture.configureByFile("renameJavaInnerClass.skin")
    runCommand {
      myFixture.renameElement(innerClass, "FooBar")
    }
    myFixture.checkResultByFile("renameJavaInnerClass.after")
    undo()
    myFixture.checkResultByFile("renameJavaInnerClass.skin")
  }

  fun testRenameKotlinClass() {
    myFixture.configureByFile("KotlinClass.kt")
    val classToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameKotlinClass1.skin")
    runCommand {
      myFixture.renameElement(classToRename, "MyClass")
    }
    myFixture.checkResultByFile("renameKotlinClass1.after")
    undo()
    myFixture.checkResultByFile("renameKotlinClass1.skin")
  }

  fun testRenameKotlinInnerClass() {
    myFixture.configureByFile("KotlinClass.kt")
    val innerClass = (myFixture.elementAtCaret as KtClass).declarations.find { (it as? KtClass)?.fqName?.shortName()?.asString() == "Inner" }
    myFixture.configureByFile("renameKotlinInnerClass.json")
    markFileAsSkin(project, file.virtualFile)
    runCommand {
      myFixture.renameElement(innerClass!!, "FooBar")
    }
    myFixture.checkResultByFile("renameKotlinInnerClass.after")
    undo()
    myFixture.checkResultByFile("renameKotlinInnerClass.json")
  }

  fun testRenamePackage() {
    myFixture.copyFileToProject("JavaClass2.java", "com/example/JavaClass2.java")
    myFixture.configureByFiles("renamePackage.skin", "KotlinClass.kt")
    val pkg = JavaPsiFacade.getInstance(project).findPackage("com.example") ?: throw AssertionError()
    runCommand {
      myFixture.renameElement(pkg, "foo")
    }
    myFixture.checkResultByFile("renamePackage.after")
    undo()
    myFixture.checkResultByFile("renamePackage.skin")
  }

  fun testMovePackage() {
    myFixture.copyDirectoryToProject("org", "org")
    myFixture.copyFileToProject("JavaClass2.java", "com/example/JavaClass2.java")
    myFixture.copyFileToProject("TopLevelClass.kt", "com/example/TopLevelClass.kt")
    myFixture.copyFileToProject("KotlinClass3.kt", "com/example/KotlinClass3.kt")
    myFixture.configureByFile("movePackage.json")
    markFileAsSkin(project, file.virtualFile)
    runCommand {
      movePackage("com.example", "org.something")
    }
    myFixture.checkResultByFile("movePackage.after")
    undo()
    myFixture.checkResultByFile("movePackage.json")
  }

  fun testMoveJavaClass() {
    myFixture.copyDirectoryToProject("org", "org")
    myFixture.configureByFiles("moveJavaClass1.skin", "JavaClass.java")
    runCommand {
      moveJavaClass("com.example.JavaClass", "org.something")
    }
    myFixture.checkResultByFile("moveJavaClass1.after")
    undo()
    myFixture.checkResultByFile("moveJavaClass1.skin")
  }

  fun testMoveKotlinFile() {
    myFixture.copyDirectoryToProject("org", "org")
    val file = myFixture.configureByFile("KotlinClass.kt") as KtFile
    myFixture.configureByFile("changeKotlinPackageDirective1.skin")
    moveKotlinFile(file, "org.something")
    myFixture.checkResultByFile("changeKotlinPackageDirective1.skin") // no changes
  }

  fun testChangeKotlinPackageDirective1() {
    doTestChangeKotlinPackageDirective("changeKotlinPackageDirective1.skin", "changeKotlinPackageDirective1.after", "KotlinClass.kt", "org.something")
  }

  fun testChangeKotlinPackageDirective2() {
    doTestChangeKotlinPackageDirective("changeKotlinPackageDirective2.skin", "changeKotlinPackageDirective2.after", "TopLevelClass.kt", "org.something")
  }

  fun testChangeKotlinPackageDirective3() {
    doTestChangeKotlinPackageDirective("changeKotlinPackageDirective3.skin", "changeKotlinPackageDirective3.after", "KotlinClass.kt", "")
  }

  fun testChangeKotlinPackageDirectiveManually() {
    myFixture.configureByFiles("KotlinClass2.kt", "changeKotlinPackageDirective1.skin")
    myFixture.type("foo")
    commit()
    myFixture.checkResultByFile("changeKotlinPackageDirective1.skin", "changeKotlinPackageDirective1.skin", false)
  }

  fun testAddKotlinPackageDirectiveManually() {
    myFixture.configureByFiles("TopLevelClass.kt", "changeKotlinPackageDirective2.skin")
    myFixture.editor.moveCaret(0)
    myFixture.type("package foo.bar\n")
    commit()
    myFixture.checkResultByFile("changeKotlinPackageDirective2.skin", "changeKotlinPackageDirective2.skin", false)
  }

  fun testRemoveKotlinPackageDirectiveManually() {
    myFixture.configureByFiles("KotlinClass2.kt", "changeKotlinPackageDirective1.skin")
    val endOfPackageDirective = (file as KtFile).packageDirective!!.endOffset
    editor.caretModel.moveToOffset(endOfPackageDirective)
    myFixture.type("\b".repeat(100))
    commit()
    myFixture.checkResultByFile("changeKotlinPackageDirective1.skin", "changeKotlinPackageDirective1.skin", false)
  }

  fun moveKotlinFile(file: KtFile, newPackageName: String) {

    val pkg = JavaPsiFacade.getInstance(project).findPackage(newPackageName)
    assertNotNull(pkg)

    val dirs = pkg!!.directories

    ActionRunner.runInsideWriteAction {
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

  fun moveJavaClass(className: String, newPackageName: String) {

    BaseRefactoringProcessor.ConflictsInTestsException.setTestIgnore(true)

    val clazz = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.projectScope(project)) ?: throw AssertionError()
    val pkg = JavaPsiFacade.getInstance(project).findPackage(newPackageName) ?: throw AssertionError()
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

  fun movePackage(packageName: String, newPackageName: String) {

    BaseRefactoringProcessor.ConflictsInTestsException.setTestIgnore(true)

    val oldPackage = JavaPsiFacade.getInstance(project).findPackage(packageName) ?: throw AssertionError()
    val newPackage = JavaPsiFacade.getInstance(project).findPackage(newPackageName) ?: throw AssertionError()
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

  fun doTestChangeKotlinPackageDirective(beforeFileName: String, afterFileName: String, kotlinFileName: String, newPackageName: String) {

    val kotlinFile = myFixture.configureByFile(kotlinFileName) as KtFile
    myFixture.configureByFile(beforeFileName)

    runCommand {
      KotlinChangePackageRefactoring(kotlinFile).run(FqName(newPackageName))
    }

    commit()

    myFixture.checkResultByFile(afterFileName)

    undo()

    myFixture.checkResultByFile(beforeFileName)

  }

  fun runCommand(f: () -> Unit) {
    CommandProcessor.getInstance().executeCommand(project, f, "", null, UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION)
  }

  fun undo() {
    val fileEditor = FileEditorManager.getInstance(project).getEditors(file.virtualFile).first()
    val undoManager = UndoManager.getInstance(project)

    assertTrue(undoManager.isUndoAvailable(fileEditor))
    undoManager.undo(fileEditor)
  }

  fun commit() {

    PsiDocumentManager.getInstance(project).commitAllDocuments()
    FileDocumentManager.getInstance().saveAllDocuments()

  }

  override fun setUp() {
    super.setUp()

    addLibGDX()

  }

  override fun getBasePath() = "/filetypes/skin/refactor/"

}