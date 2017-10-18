package com.gmail.blueboxware.libgdxplugin.properties

import com.gmail.blueboxware.libgdxplugin.inspections.java.JavaInvalidPropertyKeyInspection
import com.gmail.blueboxware.libgdxplugin.inspections.kotlin.KotlinInvalidPropertyKeyInspection
import com.intellij.codeInspection.LocalInspectionTool


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
class TestInspections: PropertiesCodeInsightFixtureTestCase() {

  fun testJavaInvalidPropertyKeyInspection() {
    doTest(JavaInvalidPropertyKeyInspection(), "inspections/JavaClass.java")
  }

  fun testKotlinInvalidPropertyKeyInspection() {
    addKotlin()
    doTest(KotlinInvalidPropertyKeyInspection(), "inspections/KotlinFile.kt")
  }

  private fun doTest(inspection: LocalInspectionTool, filename: String) {
    myFixture.enableInspections(inspection)
    myFixture.testHighlighting(true, false, false, filename)
  }

}