package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryImpl
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase

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
abstract class LibGDXCodeInsightFixtureTestCase : LightCodeInsightFixtureTestCase() {

  fun getTestDataBasePath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/")

  override fun getTestDataPath() =  FileUtil.toSystemDependentName(getTestDataBasePath() + basePath)

  fun addLibGDX() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/gdx.jar")
  }

  fun addKotlin() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/kotlin-runtime.jar")
  }

  fun addAnnotations() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/annotations.jar")
  }

  fun assertIdeaHomePath() {
    val path = System.getProperty(PathManager.PROPERTY_HOME_PATH)
    if (path == null || path == "") {
      throw AssertionError("Set ${PathManager.PROPERTY_HOME_PATH} to point to the IntelliJ source directory")
    }
  }

  override fun setUp() {
    assertIdeaHomePath()
    VersionManager.BASE_URL = "http://127.0.0.1/maven/"

    super.setUp()

    addDummyLibrary(Libraries.LIBGDX, "1.9.3")
    project.getComponent(VersionManager::class.java).updateUsedVersions()
  }


  fun addDummyLibrary(library: Libraries, version: String) {

    object : WriteCommandAction<Unit>(project) {

      override fun run(result: Result<Unit>) {

        val projectModel = ProjectLibraryTable.getInstance(project).modifiableModel

        for (lib in projectModel.libraries) {
          Libraries.extractLibraryInfoFromIdeaLibrary(lib)?.let { result ->
            if (result.first == library) {
              projectModel.removeLibrary(lib)
            }
          }
        }

        val libraryModel = (projectModel.createLibrary(library.library.artifactId) as LibraryImpl).modifiableModel

        libraryModel.addRoot("/" + library.library.groupId + "/" + library.library.artifactId + "/" + version + "/", OrderRootType.CLASSES)

        libraryModel.commit()
        projectModel.commit()
      }

    }.execute()

  }

}