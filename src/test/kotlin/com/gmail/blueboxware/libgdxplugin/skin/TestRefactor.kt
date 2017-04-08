package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase

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
    myFixture.renameElementAtCaret("green")
    myFixture.checkResultByFile("renameResource.after")
  }

  fun testRenameJavaField1() {
    myFixture.configureByFile("ColorArrayHolder.java")
    val fieldToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameJavaField.skin")
    myFixture.renameElement(fieldToRename, "someColors")
    myFixture.checkResultByFile("renameJavaField.after")
  }

  fun testRenameJavaClass1() {
    myFixture.configureByFile("JavaClass.java")
    val classToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameJavaClass1.skin")
    myFixture.renameElement(classToRename, "MyClass")
    myFixture.checkResultByFile("renameJavaClass1.after")
  }

  fun testRenameKotlinClass1() {
    myFixture.configureByFile("KotlinClass.kt")
    val classToRename = myFixture.elementAtCaret
    myFixture.configureByFile("renameKotlinClass1.skin")
    myFixture.renameElement(classToRename, "MyClass")
    myFixture.checkResultByFile("renameKotlinClass1.after")
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()


  }

  override fun getBasePath() = "/filetypes/skin/refactor/"

}