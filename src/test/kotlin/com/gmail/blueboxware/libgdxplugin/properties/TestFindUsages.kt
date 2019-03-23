package com.gmail.blueboxware.libgdxplugin.properties

import com.gmail.blueboxware.libgdxplugin.filetypes.properties.GDXPropertyReference
import com.gmail.blueboxware.libgdxplugin.references.FileReference
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.lang.properties.psi.Property
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl
import com.intellij.psi.search.FilenameIndex
import org.jetbrains.kotlin.idea.search.projectScope


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
class TestFindUsages: PropertiesCodeInsightFixtureTestCase() {

  fun testFindUsages1() {
    doTest(8, "messages.properties", "noTranslation")
  }

  fun testFindUsages2() {
    doTest(4, "test_es.properties", "spain")
  }

  fun testFindPropertiesFileUsagesInAnnotation() {
    FilenameIndex.getFilesByName(project, "messages.properties", project.projectScope()).first().let { psiFile ->
      val usages = myFixture.findUsages(psiFile)
      assertEquals(4, usages.size)
      usages.forEach { usage ->
        usage.element!!.references.forEach { reference ->
          if (reference is FileReference) {
            assertEquals("messages.properties", (reference.resolve() as PropertiesFileImpl).name)
          }
        }
      }
    }
  }

  fun doTest(nrOfUsages: Int, propertiesFileName: String, key: String) {

    val property = FilenameIndex.getFilesByName(project, propertiesFileName, project.projectScope()).first().let { file ->
      (file as PropertiesFile).findPropertyByKey(key)!!.let {
        it as Property
      }
    }

    val usagesInfos = myFixture.findUsages(property)
    assertEquals(nrOfUsages, usagesInfos.size)

    for (usageInfo in usagesInfos) {
      usageInfo.element?.let { element ->
        val references = element.references.filter { it is GDXPropertyReference }
        assertFalse(references.isEmpty())
        references.forEach { reference ->
          (reference as? GDXPropertyReference)?.multiResolve(true)?.forEach { resolved ->
            assertTrue(resolved.element is Property)
            (resolved.element as? Property)?.let {
              assertEquals(property.name, it.name)
            }
          }
        }
      }
    }
  }

  override fun setUp() {
    super.setUp()

    copyFileToProject("findUsages/JavaClass.java")
    copyFileToProject("findUsages/KotlinFile.kt")
  }
}