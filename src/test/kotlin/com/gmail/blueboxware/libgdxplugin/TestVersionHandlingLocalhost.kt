package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradleOutdatedVersionsInspection
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradlePropertiesOutdatedVersionsInspection
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXLibrary
import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXVersionPostfixedLibrary
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.util.text.DateFormatUtil
import org.apache.log4j.Level
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.plugins.groovy.GroovyFileType
import org.junit.Before

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
class TestVersionHandlingLocalhost : LibGDXCodeInsightFixtureTestCase() {

  private val RUN_TESTS = false

  private lateinit var versionManager: VersionManager

  override fun shouldRunTest(): Boolean {
    @Suppress("ConstantConditionIf")
    if (RUN_TESTS) {
      return getTestName(true) != "testingAgainstLocalHostIsDisabled"
    } else {
      return getTestName(true) == "testingAgainstLocalHostIsDisabled"
    }
  }

  fun testTestingAgainstLocalHostIsDisabled() {
    fail("Testing against localhost is disabled")
  }

  fun testOutdatedVersionsGradleInspection1() {

    myFixture.enableInspections(GradleOutdatedVersionsInspection())
    myFixture.testHighlightingAllFiles(true, false, false, "test1.gradle")

  }

  fun testOutdatedVersionsGradleInspection2() {

    myFixture.enableInspections(GradleOutdatedVersionsInspection())
    myFixture.testHighlightingAllFiles(true, false, false, "test2.gradle")

  }

  fun testOutdatedVersionsGradleInspection3() {

    myFixture.enableInspections(GradleOutdatedVersionsInspection())
    myFixture.testHighlightingAllFiles(true, false, false, "test3.gradle")

  }

  fun testOutdatedVersionsGradlePropertiesInspection() {

    myFixture.enableInspections(GradlePropertiesOutdatedVersionsInspection())
    myFixture.testHighlightingAllFiles(true, false, false, "gradle.properties")

  }

  fun testGetLatestVersions() {

    assertEquals(MavenComparableVersion("1.0"), versionManager.getLatestVersion(Libraries.LIBGDX_ANNOTATIONS))
    assertEquals(MavenComparableVersion("1.9.5"), versionManager.getLatestVersion(Libraries.LIBGDX))
    assertEquals(MavenComparableVersion("1.8.1"), versionManager.getLatestVersion(Libraries.AI))
    assertEquals(MavenComparableVersion("1.7.3"), versionManager.getLatestVersion(Libraries.ASHLEY))
    assertEquals(MavenComparableVersion("1.4"), versionManager.getLatestVersion(Libraries.BOX2dLIGHTS))
    assertEquals(MavenComparableVersion("0.1.1"), versionManager.getLatestVersion(Libraries.OVERLAP2D))
    assertEquals(MavenComparableVersion("1.9.5"), versionManager.getLatestVersion(Libraries.BOX2D))
    assertEquals(MavenComparableVersion("1.7.1.9.3"), versionManager.getLatestVersion(Libraries.KIWI))
    addDummyLibrary(Libraries.LIBGDX, "1.7.0")
    Thread.sleep(2 * VersionManager.LIBRARY_CHANGED_TIME_OUT)
    assertEquals(MavenComparableVersion("1.1.1.7.0"), versionManager.getLatestVersion(Libraries.KIWI))
    addDummyLibrary(Libraries.LIBGDX, "1.9.4")
    Thread.sleep(2 * VersionManager.LIBRARY_CHANGED_TIME_OUT)
    assertEquals(MavenComparableVersion("1.8.1.9.4"), versionManager.getLatestVersion(Libraries.KIWI))
    addDummyLibrary(Libraries.LIBGDX, "1.9.9")
    Thread.sleep(2 * VersionManager.LIBRARY_CHANGED_TIME_OUT)
    assertEquals(MavenComparableVersion("2.2.1.9.9-b1"), versionManager.getLatestVersion(Libraries.KIWI))

  }

  fun testLatestVersionAvailability() {

    for (lib in Libraries.values()) {
      assertNotNull(lib.toString(), versionManager.getLatestVersion(lib))
    }

  }

  fun testUsedVersions() {

    addDummyLibrary(Libraries.AUTUMN_MVC, "1.2.3")
    assertEquals("1.2.3", versionManager.getUsedVersion(Libraries.AUTUMN_MVC).toString())
    addDummyLibrary(Libraries.KTX_ACTORS, "4.5.6")
    assertEquals("4.5.6", versionManager.getUsedVersion(Libraries.KTX_ACTORS).toString())
    addDummyLibrary(Libraries.LIBGDXUTILS_BOX2D, "7.8")
    assertEquals("7.8", versionManager.getUsedVersion(Libraries.LIBGDXUTILS_BOX2D).toString())

  }

  @Before
  fun testSavedState() {

    PropertiesComponent.getInstance()?.let { propertiesComponent ->

      for (lib in Libraries.values()) {
        if (lib.library !is LibGDXLibrary && lib.library !is LibGDXVersionPostfixedLibrary) {
          assertEquals(lib.library.name, versionManager.getLatestVersion(lib).toString(), propertiesComponent.getValue(lib.library.versionKey))
        }
      }

    }

  }

  override fun setUp() {

    VersionManager.BATCH_SIZE = Libraries.values().size / 2
    VersionManager.SCHEDULED_UPDATE_INTERVAL = 2 * DateFormatUtil.SECOND
    VersionManager.LIBRARY_CHANGED_TIME_OUT = 5 * DateFormatUtil.SECOND
    VersionManager.LOG.setLevel(Level.DEBUG)
    Library.TEST_URL = "http://127.0.0.1/maven/"

    super.setUp()

    object: WriteCommandAction<Unit>(project) {
      override fun run(result: Result<Unit>) {
        FileTypeManager.getInstance().associateExtension(GroovyFileType.GROOVY_FILE_TYPE, "gradle")
      }
    }.execute()

    versionManager = project.getComponent(VersionManager::class.java)

    for (lib in Libraries.values()) {
      addDummyLibrary(lib, "0.0")
    }

    addDummyLibrary(Libraries.LIBGDX, "1.9.3")

    if (getTestName(true) !in listOf("usedVersions", "testingAgainstLocalHostIsDisabled")) {
      Thread.sleep(2 * VersionManager.LIBRARY_CHANGED_TIME_OUT)
    }

  }

  override fun getBasePath() = "versions/"

}