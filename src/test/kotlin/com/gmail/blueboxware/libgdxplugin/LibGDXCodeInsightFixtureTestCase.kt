package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
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

  private fun getTestDataBasePath() = FileUtil.toSystemDependentName(System.getProperty("user.dir") + "/src/test/testdata/")

  override fun getTestDataPath() =  FileUtil.toSystemDependentName(getTestDataBasePath() + basePath)

  fun addLibGDX() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/gdx.jar")
  }

  fun addKotlin() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/kotlin-runtime.jar")
  }

  fun addAnnotations() {
    PsiTestUtil.addLibrary(myFixture.module, getTestDataBasePath() + "/lib/libgdxpluginannotations-1.13.jar")
  }

  private fun assertIdeaHomePath() {
    val path = System.getProperty(PathManager.PROPERTY_HOME_PATH)
    if (path == null || path == "") {
      throw AssertionError("Set ${PathManager.PROPERTY_HOME_PATH} to point to the IntelliJ source directory")
    }
  }

  override fun setUp() {
    assertIdeaHomePath()

    Library.TEST_URL = "http://127.0.0.1/maven/"

    super.setUp()

    project.getComponent(VersionManager::class.java).updateUsedVersions()
  }

  override fun tearDown() {

    WriteCommandAction.runWriteCommandAction(null) {
      ProjectLibraryTable.getInstance(project).modifiableModel.let { model ->
        listOf("gdx.jar", "kotlin-runtime.jar", "libgdxpluginannotations-1.13.jar").forEach {
          model.getLibraryByName(it)?.let { lib ->
            model.removeLibrary(lib)
          }
        }
        model.commit()
      }
    }

    super.tearDown()
  }

  fun addDummyLibGDX199() = addDummyLibrary(Libraries.LIBGDX, "1.9.9")

  fun removeDummyLibGDX199() = removeLibrary(Libraries.LIBGDX, "1.9.9")

  fun addDummyLibrary(library: Libraries, version: String) {

    WriteCommandAction.writeCommandAction(project).run<Throwable> {

      val projectModel = ServiceManager.getService(project, ProjectLibraryTable::class.java)?.modifiableModel
              ?: throw AssertionError()

      for (lib in projectModel.libraries) {
        Libraries.extractLibraryInfoFromIdeaLibrary(lib)?.let { (libraries) ->
          if (libraries == library) {
            projectModel.removeLibrary(lib)
          }
        }
      }

      val libraryModel = (projectModel.createLibrary(library.library.artifactId) as LibraryImpl).modifiableModel

      libraryModel.addRoot("/" + library.library.groupId + "/" + library.library.artifactId + "/" + version + "/", OrderRootType.CLASSES)

      libraryModel.commit()
      projectModel.commit()
    }

  }

  private fun removeLibrary(library: Libraries, version: String? = null) {

    WriteCommandAction.runWriteCommandAction(project) {

      val projectModel = ServiceManager.getService(project, ProjectLibraryTable::class.java)?.modifiableModel
              ?: throw AssertionError()

      for (lib in projectModel.libraries) {
        Libraries.extractLibraryInfoFromIdeaLibrary(lib)?.let { (thisLibrary, thisVersion) ->
          if (thisLibrary == library && (version == null || version == thisVersion.canonical)) {
            projectModel.removeLibrary(lib)
          }
        }
      }

      projectModel.commit()

    }

  }

}