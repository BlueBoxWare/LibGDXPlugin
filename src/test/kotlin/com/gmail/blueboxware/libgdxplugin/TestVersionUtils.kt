package com.gmail.blueboxware.libgdxplugin
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.gmail.blueboxware.libgdxplugin.utils.VersionUtils
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import junit.framework.TestCase
import org.jetbrains.plugins.groovy.GroovyFileType

/*
 * Copyright 2016 Blue Box Ware
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
class TestVersionUtils: LibGDXCodeInsightFixtureTestCase() {

  override fun setUp() {
    super.setUp()

    object: WriteCommandAction<Project>(project) {
      override fun run(result: Result<Project>) {
        FileTypeManager.getInstance().associateExtension(GroovyFileType.GROOVY_FILE_TYPE, "gradle")
      }
    }.execute()

  }

  fun testVersionStringComparison() {

    val tests = setOf(
            "1.0" to "0.9",
            "0.99" to "0.9",
            "1.75" to "1.8.9",
            "1.0.0.1" to "1.0",
            "9.9" to "9.8.9"
    )

    for (pair in tests.iterator()) {
      TestCase.assertEquals("${pair.first} <=> ${pair.second}", 1, VersionUtils.compareVersionStrings(pair.first, pair.second))
      TestCase.assertEquals("${pair.second} <=> ${pair.first}", -1, VersionUtils.compareVersionStrings(pair.second, pair.first))
    }

    TestCase.assertEquals(0, VersionUtils.compareVersionStrings("1.1.9", "1.1.9"))
    TestCase.assertEquals(0, VersionUtils.compareVersionStrings("0.99", "0.99"))
  }

  fun testFetchingLatestVersions() {

    val projectComponent = myFixture.project.getComponent(LibGDXProjectComponent::class.java)

    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.GDX) ?: "0", "1.9.2") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.BOX2DLIGHTS) ?: "0", "1.3") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.ASHLEY) ?: "0", "1.7.1") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.AI) ?: "0", "1.7.0") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.OVERLAP2D) ?: "0", "0.0.9") > 0)

    // caching
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.GDX, fromCache = true) ?: "0", "1.9.2") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.BOX2DLIGHTS, fromCache = true) ?: "0", "1.3") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.ASHLEY, fromCache = true) ?: "0", "1.7.1") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.AI, fromCache = true) ?: "0", "1.7.0") > 0)
    assert(VersionUtils.compareVersionStrings(projectComponent.getLatestLibraryVersion(VersionUtils.GDXLibrary.OVERLAP2D, fromCache = true) ?: "0", "0.0.9") > 0)

  }

}
