package com.gmail.blueboxware.libgdxplugin.properties

import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.lang.properties.psi.Property
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
class TestRefactor: PropertiesCodeInsightFixtureTestCase() {

  fun testRename() {
    FilenameIndex.getFilesByName(project, "messages_en_GB.properties", project.projectScope()).first().let { propertiesFile ->
      val property = (propertiesFile as PropertiesFile).findPropertyByKey("oldName") as Property
      configureByFile("refactor/JavaClass.java")
      myFixture.renameElement(property, "newName1")
      myFixture.checkResultByFile("refactor/JavaClass.after")
      configureByFile("refactor/KotlinFile.kt")
      myFixture.renameElement(property, "newName2")
      myFixture.checkResultByFile("refactor/KotlinFile.after")
    }
  }

}