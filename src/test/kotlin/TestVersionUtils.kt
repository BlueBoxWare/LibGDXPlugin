
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.gmail.blueboxware.libgdxplugin.inspections.utils.GDXLibrary
import com.gmail.blueboxware.libgdxplugin.inspections.utils.GitHub
import com.gmail.blueboxware.libgdxplugin.inspections.utils.compareVersionStrings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
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


/*
 *
 * RUN CONFIGURATION VM OPTIONS:
 *
 * -ea -Dlibgdxplugin.test.path=<TESTPATH>
 *
 * where <TESTPATH> is the absolute path to the libgdxplugin/src/test/testdata directory
 *
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

  override fun getTestDataPath(): String? {
    val path = System.getProperty("libgdxplugin.test.path")
    if (path == null) {
      throw AssertionError("Use -Dlibgdxplugin.test.path=<TESTPATH> to specify the absolute path to the testdata directory")
    }
    return path
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

     myFixture.configureByFile("etc/gradle1/build.gradle")

    val projectComponent = myFixture.project.getComponent(LibGDXProjectComponent::class.java)

    if (projectComponent == null) {
      TestCase.fail()
    } else {
      assertTrue(projectComponent.isLibGDXProject)

      assert(projectComponent.getLibraryVersion(GDXLibrary.GDX) == "1.9.2")
      assert(projectComponent.getLibraryVersion(GDXLibrary.BOX2DLIGHTS) == "1.4")
      assert(projectComponent.getLibraryVersion(GDXLibrary.ASHLEY) == "1.7.0")
      assert(projectComponent.getLibraryVersion(GDXLibrary.AI) == "1.8.0")
      assert(projectComponent.getLibraryVersion(GDXLibrary.OVERLAP2D) == "0.1.0")

      (myFixture.tempDirFixture as? LightTempDirTestFixtureImpl)?.let {
        it.deleteAll()
      }
      myFixture.configureByFile("etc/gradle2/build.gradle")

      assert(projectComponent.getLibraryVersion(GDXLibrary.GDX) == "1.9.1")
      assert(projectComponent.getLibraryVersion(GDXLibrary.BOX2DLIGHTS) == "1.3")
      assert(projectComponent.getLibraryVersion(GDXLibrary.ASHLEY) == "1.7.1")
      assert(projectComponent.getLibraryVersion(GDXLibrary.AI) == "1.8.1")
      assert(projectComponent.getLibraryVersion(GDXLibrary.OVERLAP2D) == "0.1.1")
    }

  }

}
