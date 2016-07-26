
import com.gmail.blueboxware.libgdxplugin.inspections.utils.*
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
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

class TestVersionUtils: LightCodeInsightFixtureTestCase() {

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
      TestCase.assertEquals("${pair.first} <=> ${pair.second}", 1, compareVersionStrings(pair.first, pair.second))
      TestCase.assertEquals("${pair.second} <=> ${pair.first}", -1, compareVersionStrings(pair.second, pair.first))
    }

    TestCase.assertEquals(0, compareVersionStrings("1.1.9", "1.1.9"))
    TestCase.assertEquals(0, compareVersionStrings("0.99", "0.99"))
  }

  fun testFetchingLatestVersions() {

    GitHub.setPropertiesComponent(PropertiesComponent.getInstance(myFixture.project))

    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.GDX) ?: "0", "1.9.2") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.BOX2DLIGHTS) ?: "0", "1.3") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.ASHLEY) ?: "0", "1.7.1") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.AI) ?: "0", "1.7.0") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.OVERLAP2D) ?: "0", "0.0.9") > 0)

    // caching
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.GDX, forceFromCache = true) ?: "0", "1.9.2") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.BOX2DLIGHTS, forceFromCache = true) ?: "0", "1.3") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.ASHLEY, forceFromCache = true) ?: "0", "1.7.1") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.AI, forceFromCache = true) ?: "0", "1.7.0") > 0)
    assert(compareVersionStrings(GitHub.getLatestVersion(GDXLibrary.OVERLAP2D, forceFromCache = true) ?: "0", "0.0.9") > 0)
  }

  fun testGetVersionFromGradle() {

     myFixture.configureByText("build.gradle", """
        gdxVersion = '1.9.2'
        box2DLightsVersion = '1.4'
        ashleyVersion = '1.7.0'
        aiVersion = '1.8.0'

        compile "com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.0"
    """)

    assertTrue(isLibGDXProject(myFixture.project))

    assert(getLibraryVersion(GDXLibrary.GDX, myFixture.project) == "1.9.2")
    assert(getLibraryVersion(GDXLibrary.BOX2DLIGHTS, myFixture.project) == "1.4")
    assert(getLibraryVersion(GDXLibrary.ASHLEY, myFixture.project) == "1.7.0")
    assert(getLibraryVersion(GDXLibrary.AI, myFixture.project) == "1.8.0")
    assert(getLibraryVersion(GDXLibrary.OVERLAP2D, myFixture.project) == "0.1.0")

    myFixture.configureByText("build.gradle", """

      "com.badlogicgames.gdx:gdx:1.9.1"
      "com.badlogicgames.box2dlights:box2dlights:1.3"
      "com.badlogicgames.ashley:ashley:1.7.1"
      "com.badlogicgames.gdx:gdx-ai:1.8.1"
      "com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.1"

    """)

    assert(getLibraryVersion(GDXLibrary.GDX, myFixture.project) == "1.9.1")
    assert(getLibraryVersion(GDXLibrary.BOX2DLIGHTS, myFixture.project) == "1.3")
    assert(getLibraryVersion(GDXLibrary.ASHLEY, myFixture.project) == "1.7.1")
    assert(getLibraryVersion(GDXLibrary.AI, myFixture.project) == "1.8.1")
    assert(getLibraryVersion(GDXLibrary.OVERLAP2D, myFixture.project) == "0.1.1")

  }

}
