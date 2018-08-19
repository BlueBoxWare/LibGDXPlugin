package com.gmail.blueboxware.libgdxplugin
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.FileTreeAccessFilter
import com.intellij.testFramework.fixtures.impl.JavaCodeInsightTestFixtureImpl

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
class TestColorAnnotator : LibGDXCodeInsightFixtureTestCase() {

  fun testJava1() {
    doTest("Java1.java")
  }

  fun testJava2() {
    doTest("Java2.java")
  }

  fun testKotlin() {
    doTest("Kotlin1.kt")
  }

  fun testJavaToKotlin() {
    doTest("JavaToKotlin.kt", "JavaToKotlin.java")
  }

  fun testKotlinToJava() {
    doTest("KotlinToJava.java", "KotlinToJava.kt")
  }

  fun doTest(vararg files: String) {
    myFixture.configureByFiles(*(files.map { "annotators/colorAnnotator/$it" }.toTypedArray()))
    myFixture.checkHighlighting(false, false, true)
  }

  override fun setUp() {
    super.setUp()

    addAnnotations()
    addLibGDX()
    addDummyLibGDX199()
    addKotlin()

    (myFixture as JavaCodeInsightTestFixtureImpl).setVirtualFileFilter(object: FileTreeAccessFilter() {
      override fun accept(file: VirtualFile?): Boolean {
        return false
      }
    })

    myFixture.copyFileToProject("annotators/colorAnnotator/assets/libgdx.skin", "libgdx.skin")

  }

}