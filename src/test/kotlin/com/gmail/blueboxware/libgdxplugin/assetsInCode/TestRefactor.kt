package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.intellij.psi.PsiManager

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
class TestRefactor : AssetsInCodeCodeInsightFixtureTestCase() {

  fun testRenameSkinFileInJavaAnnotation() {
    val virtualFile = myFixture.copyFileToProject("refactor/libgdx.skin")
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: throw AssertionError()
    myFixture.configureByFile("refactor/JavaClass.java")
    myFixture.renameElement(psiFile, "newname.json")
    myFixture.checkResultByFile("refactor/JavaClass.renameSkin.after")
  }

  fun testRenameSkinFileInKotlinAnnotation() {
    val virtualFile = myFixture.copyFileToProject("refactor/libgdx.skin")
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: throw AssertionError()
    myFixture.configureByFile("refactor/KotlinFile.kt")
    myFixture.renameElement(psiFile, "newname.json")
    myFixture.checkResultByFile("refactor/KotlinFile.renameSkin.after")
  }

  fun testRenameAtlasFileInKotlinAnnotation() {
    val virtualFile = myFixture.copyFileToProject("refactor/libgdx.atlas")
    val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: throw AssertionError()
    myFixture.configureByFile("refactor/KotlinFile.kt")
    myFixture.renameElement(psiFile, "newname.atlas")
    myFixture.checkResultByFile("refactor/KotlinFile.renameAtlas.after")
  }

  fun testRenameResourceInJava() {
    val virtualFile = myFixture.copyFileToProject("refactor/libgdx.skin")
    val skinFile = PsiManager.getInstance(project).findFile(virtualFile) as? SkinFile ?: throw AssertionError()
    val element = skinFile.getResources("com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle", "green").firstOrNull() ?: throw AssertionError()
    myFixture.configureByFile("refactor/JavaClass.java")
    myFixture.renameElement(element, "yellow")
    myFixture.checkResultByFile("refactor/JavaClass.renameResource.after")
  }

  fun testRenameResourceInKotlin() {
    val virtualFile = myFixture.copyFileToProject("refactor/libgdx.skin")
    val skinFile = PsiManager.getInstance(project).findFile(virtualFile) as? SkinFile ?: throw AssertionError()
    val element = skinFile.getResources("com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle", "green").firstOrNull() ?: throw AssertionError()
    myFixture.configureByFile("refactor/KotlinFile.kt")
    myFixture.renameElement(element, "yellow")
    myFixture.checkResultByFile("refactor/KotlinFile.renameResource.after")
  }
}