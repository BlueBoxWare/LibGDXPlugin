package com.gmail.blueboxware.libgdxplugin/*
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
import com.gmail.blueboxware.libgdxplugin.inspections.global.DesignedForTabletsInspection
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradlePropertiesTestIdsInspection
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradleTestIdsInspection
import com.gmail.blueboxware.libgdxplugin.inspections.java.*
import com.gmail.blueboxware.libgdxplugin.inspections.kotlin.*
import com.gmail.blueboxware.libgdxplugin.inspections.xml.MissingExternalFilesPermissionInspection
import com.gmail.blueboxware.libgdxplugin.inspections.xml.OpenGLESDirectiveInspection
import com.gmail.blueboxware.libgdxplugin.inspections.xml.XmlTestIdsInspection
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_MAP
import com.intellij.analysis.AnalysisScope
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInspection.CommonProblemDescriptor
import com.intellij.codeInspection.GlobalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ex.GlobalInspectionToolWrapper
import com.intellij.codeInspection.ex.InspectionManagerEx
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.testFramework.InspectionTestUtil
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl.createGlobalContextForTool
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
import junit.framework.Assert
import org.jetbrains.plugins.groovy.GroovyFileType
import java.io.File

class TestInspections : LibGDXCodeInsightFixtureTestCase() {

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addKotlin()

    object: WriteCommandAction<Unit>(project) {
      override fun run(result: Result<Unit>) {
        FileTypeManager.getInstance().associateExtension(GroovyFileType.GROOVY_FILE_TYPE, "gradle")
      }
    }.execute()

    ServiceManager.getService(project, LibGDXPluginSettings::class.java).enableColorAnnotations = false

  }

  fun performInspectionsTest(inspection: LocalInspectionTool, vararg fileNames: String) {
    myFixture.enableInspections(inspection)
    myFixture.testHighlightingAllFiles(true, false, false, *fileNames)
  }

  fun performInspectionTestWithString(text: String, inspection: LocalInspectionTool, fileName: String) {

    myFixture.configureByText(fileName, text)
    myFixture.enableInspections(inspection)
    myFixture.checkHighlighting(true, false, false)

  }

  fun getHighLightsWithDescription(inspection: LocalInspectionTool, vararg fileNames: String, warningDescription: String): List<HighlightInfo> {

    myFixture.configureByFiles(*fileNames)
    myFixture.enableInspections(inspection)
    return myFixture.doHighlighting().filter { it.description?.contains(warningDescription) ?: false }

  }

  fun getGlobalInspectionResults(testDir: String, inspection: GlobalInspectionTool): Collection<CommonProblemDescriptor> {

    val toolWrapper = GlobalInspectionToolWrapper(inspection)

    val sourceDir = myFixture.copyDirectoryToProject(File(testDir, "src").path, "src")
    val psiDirectory = myFixture.psiManager.findDirectory(sourceDir) ?: throw AssertionError("Could not find $sourceDir")

    val scope = AnalysisScope(psiDirectory)
    scope.invalidate()

    val globalContext = createGlobalContextForTool(scope, project, InspectionManager.getInstance(project) as InspectionManagerEx, toolWrapper)

    InspectionTestUtil.runTool(toolWrapper, scope, globalContext)

    (myFixture.tempDirFixture as? LightTempDirTestFixtureImpl)?.deleteAll()

    return globalContext.getPresentation(toolWrapper).problemDescriptors
  }

  fun testGlobalInspection(testDir: String, inspection: GlobalInspectionTool, warnings: List<String>) {
    val expectedWarnings = warnings.toMutableList()
    val problemDescriptors = getGlobalInspectionResults(testDir, inspection)

    for (problem in problemDescriptors) {
      val msg = problem.descriptionTemplate
      if (!expectedWarnings.remove(msg)) {
        fail("Unexpected warning $msg in:\n" + ppDirContents(testDir))
      }
    }

    if (!expectedWarnings.isEmpty()) {
      fail("Expected warning(s) not found: " + expectedWarnings.joinToString("\n") + " in:\n" + ppDirContents(testDir))
    }

  }

  private fun ppDirContents(testDir: String): String {
    val dirName = testDataPath + testDir + "src/"
    val dir = File(dirName)
    val stringBuilder = StringBuilder()

    val files = dir.listFiles().flatMap { if (it.isDirectory) it.listFiles().toList() else listOf(it) }.sortedBy { it.absolutePath }

    for (file in files) {
      if (file.isFile) {
        val name = file.path.removePrefix(dirName)
        stringBuilder.append("\t[" + name + "]: \"" + file.readText() + "\"\n")
      }
    }

    return stringBuilder.toString()
  }


  /*
   * Unsafe iterators inspection
   */

  fun performUnsafeIteratorInspectionTest(name: String) {
    val lName = "inspections/unsafeIterators/" + name
    performInspectionsTest(JavaUnsafeIteratorInspection(), lName + ".java")
    performInspectionsTest(KotlinUnsafeIteratorInspection(), lName + ".kt")
  }

  fun testUnsafeIteratorInspection() {
    for (clazz in listOf(
            "Array",
            "ArrayMap",
            "IdentityMap",
            "IntFloatMap",
            "IntIntMap",
            "IntMap",
            "IntSet",
            "LongMap",
            "ObjectFloatMap",
            "ObjectIntMap",
            "ObjectMap",
            "ObjectSet",
            "OrderedMap",
            "OrderedSet",
            "Predicate.PredicateIterable",
            "Queue",
            "SortedIntList"
    )) {
      performUnsafeIteratorInspectionTest(clazz)
    }
  }

  /*
   * OpenGL Directive inspection
   */
  fun performOpenGLDirectiveInspectionTest(name: String, warningExpected: Boolean, problemElement: String? = "uses-feature") {
    val lName = "inspections/missingOpenGLDirective/" + name
    val hightLights = getHighLightsWithDescription(OpenGLESDirectiveInspection(), lName, warningDescription = message("no.opengl.directive.problem.descriptor"))

    for (hightLight in hightLights) {
      if (warningExpected) {
        if (problemElement != null && !hightLight.text.startsWith("<" + problemElement)) {
          UsefulTestCase.fail(name + ": Hightlight starts at wrong element: '" + hightLight.text.substring(0, 30) + "'")
          return
        } else {
          return
        }
      } else {
        UsefulTestCase.fail(name + ": Unexpected highlight starting at '" + hightLight.text.substring(0, 30) + "'")
        return
      }
    }

    if (warningExpected) {
      UsefulTestCase.fail(name + ": Expected highlight not found")
    }

  }

  fun testOpenGLDirectiveInspectionWrongFilename() {
    performOpenGLDirectiveInspectionTest("wrongFilename1/Androidmanifest.xml", false)
  }

  fun testOpenGLDirectiveInspectionDirectiveIsMissing() {
    performOpenGLDirectiveInspectionTest("missingDirective1/AndroidManifest.xml", true, problemElement = null)
    performOpenGLDirectiveInspectionTest("missingDirective2/AndroidManifest.xml", true)
    performOpenGLDirectiveInspectionTest("missingDirective3/AndroidManifest.xml", true)
    performOpenGLDirectiveInspectionTest("missingDirective4/AndroidManifest.xml", true, problemElement = null)
  }

  fun testOpenGLDirectiveInspectionDirectiveIsPresent() {
    performOpenGLDirectiveInspectionTest("directiveIsPresent1/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent2/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent3/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent4/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent5/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent6/AndroidManifest.xml", false)
    performOpenGLDirectiveInspectionTest("directiveIsPresent7/AndroidManifest.xml", false)
  }

  /*
   * Test ids inspection
   */
  fun testTestIdsInspectionWithIdMap() {
    val stringBuilderKotlin = StringBuilder()
    val stringBuilderJava = StringBuilder("class Test {\n")
    val stringBuilderXml = StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>")
    val stringBuilderBuildGradle = StringBuilder()
    val stringBuilderGradleProperties = StringBuilder()

    for ((idCount, testId) in TEST_ID_MAP.keys.withIndex()) {

      stringBuilderKotlin.append("val id$idCount = \"<warning>$testId</warning>\"\n")
      stringBuilderJava.append("String id$idCount = <warning>\"$testId\"</warning>;\n")
      stringBuilderXml.append("<warning><string name=\"id$idCount\">$testId</string></warning>\n")
      stringBuilderBuildGradle.append("single$idCount = <warning>'$testId'</warning>\n")
      stringBuilderBuildGradle.append("double$idCount = <warning>\"$testId\"</warning>\n")
      stringBuilderBuildGradle.append("triple$idCount = <warning>'''$testId'''</warning>\n")
      stringBuilderGradleProperties.append("<warning>id$idCount=$testId</warning>\n")
      stringBuilderGradleProperties.append("<warning>idQuoted$idCount=\"$testId\"</warning>\n")

    }

    stringBuilderJava.append("}")
    stringBuilderXml.append("</resources>")

    performInspectionTestWithString(stringBuilderKotlin.toString(), KotlinTestIdsInspection(), "Test.kt")
    performInspectionTestWithString(stringBuilderJava.toString(), JavaTestIdsInspection(), "Test.java")
    performInspectionTestWithString(stringBuilderXml.toString(), XmlTestIdsInspection(), "Test.xml")
    performInspectionTestWithString(stringBuilderBuildGradle.toString(), GradleTestIdsInspection(), "build.gradle")
    performInspectionTestWithString(stringBuilderGradleProperties.toString(), GradlePropertiesTestIdsInspection(), "gradle.properties")

  }

  /*
   *  Designed for Tablets inspection
   */

  private fun cleanUp(dirName: String) {
    val dir = File(dirName)

    for (file in dir.listFiles().flatMap { if (it.isDirectory) it.listFiles().toList() else listOf(it) }) {
      if (file.isFile) {
        file.delete()
      }
    }
  }

  fun doDesignedForTabletsTest(files: Map<String, String>, warnings: List<String>?) {

    val fakeProjectDir = "/inspections/designedForTablets/"
    val fullDir = testDataPath + fakeProjectDir + "src/"

    cleanUp(fullDir)

    for ((fileName, content) in files.entries) {
      val file = File(fullDir + fileName)
      file.writeText(content)
    }

    testGlobalInspection(fakeProjectDir, DesignedForTabletsInspection(), warnings?.map { message("designed.for.tablets.problem.descriptor."+it) } ?: listOf())

  }

  fun testDesignedForTabletsManifestOnly() {

    val tests = mapOf(
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11" android:maxSdkVersion="11" />""" to null,
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="11" android:maxSdkVersion="11" />""" to listOf("missing.support.screens"),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="10" android:maxSdkVersion="11" />""" to null,
            """<uses-sdk android:minSdkVersion="13" />""" to null,
            """<uses-sdk android:targetSdkVersion="11" />""" to listOf("missing.support.screens"),
            """<uses-sdk android:minSdkVersion="99" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11" android:maxSdkVersion="10" />""" to listOf("max"),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" android:maxSdkVersion="10" />""" to listOf("target.or.min", "missing.support.screens"),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" android:maxSdkVersion="11" />""" to listOf("target.or.min", "missing.support.screens"),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" />""" to listOf("target.or.min", "missing.support.screens"),
            """<uses-sdk android:maxSdkVersion="10" />""" to listOf("target.or.min", "missing.support.screens"),

            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="true" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true"  android:smallScreens="false" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens  android:normalScreens="false" android:smallScreens="false"  />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="false" />""" to listOf("large.false"),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="false" />""" to listOf("large.false"),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="true" android:largeScreens="false"/>""" to listOf("large.false"),
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="true" />""" to null,
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" />""" to listOf("large.missing"),
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="true" />""" to listOf("large.missing"),
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:xlargeScreens="true" />""" to null,
            """<uses-sdk android:minSdkVersion="13" /><supports-screens android:largeScreens="true" android:xlargeScreens="true" />""" to null
    )

    for ((str, warnings) in tests) {
      val content = "<manifest>$str</manifest>"
      doDesignedForTabletsTest(mapOf("AndroidManifest.xml" to content), warnings)
    }
  }

  fun testDesignedForTabletsGradleOnly() {

    val tests = mapOf(
            "" to null,
            "minSdkVersion 11\ntargetSdkVersion 11" to null,
            "minSdkVersion 1\ntargetSdkVersion 11" to null,
            "minSdkVersion 11\ntargetSdkVersion 1" to null,
            "minSdkVersion 10" to null,
            "targetSdkVersion 10" to null,
            "minSdkVersion 10\ntargetSdkVersion 10" to listOf("target.or.min", "target.or.min"),
            "minSdkVersion 11\ntargetSdkVersion 11\nmaxSdkVersion 10" to listOf("max"),
            "minSdkVersion 11\ntargetSdkVersion 11\nmaxSdkVersion 11" to null
    )

    for ((content, warnings) in tests) {
      doDesignedForTabletsTest(mapOf("build.gradle" to content), warnings)
    }
  }

  fun testDesignedForTabletsMultipleFiles() {
    val tests: Map<Map<String, String>, List<String>?> = mapOf(
           mapOf(
                   "build.gradle" to "minSdkVersion 11",
                   "a/build.gradle" to "targetSdkVersion 11"
           ) to null,
            mapOf(
                    "build.gradle" to "minSdkVersion 10",
                    "a/build.gradle" to "targetSdkVersion 10"
            ) to listOf("target.or.min"),
            mapOf(
                    "build.gradle" to "targetSdkVersion 10",
                    "a/build.gradle" to "minSdkVersion 10"
            ) to listOf("target.or.min"),
            mapOf(
                    "build.gradle" to "targetSdkVersion 10\nminSdkVersion 11",
                    "a/build.gradle" to "minSdkVersion 10"
            ) to null,
            mapOf(
                    "build.gradle" to "targetSdkVersion 11\nminSdkVersion 11",
                    "a/build.gradle" to "minSdkVersion 10",
                    "b/build.gradle" to "targetSdkVersion 10"

            ) to listOf("target.or.min"), // a/build.gradle and b/build.gradle are inspected before build.gradle
            mapOf(
                    "a/build.gradle" to "minSdkVersion 10",
                    "b/build.gradle" to "targetSdkVersion 10\nmaxSdkVersion 10",
                    "build.gradle" to "targetSdkVersion 11\nminSdkVersion 11"
            ) to listOf("max", "target.or.min"), // a/build.gradle and b/build.gradle are inspected before build.gradle

            mapOf(
                    "build.gradle" to "targetSdkVersion 11",
                    "AndroidManifest.xml" to """<manifest><uses-sdk android:minSdkVersion="11" /></manifest>"""
            ) to listOf("missing.support.screens"),
            mapOf(
                    "build.gradle" to "targetSdkVersion 10",
                    "AndroidManifest.xml" to """<manifest><uses-sdk android:minSdkVersion="13" /></manifest>"""
            ) to null,
            mapOf(
                    "build.gradle" to "targetSdkVersion 10\nminSdkVersion 10",
                    "a/build.gradle" to "targetSdkVersion 11",
                    "AndroidManifest.xml" to """<manifest><uses-sdk android:minSdkVersion="13" /></manifest>"""
            ) to null,
            mapOf(
                    "build.gradle" to "minSdkVersion 10",
                    "a/build.gradle" to "minSdkVersion 11",
                    "b/build.gradle" to "targetSdkVersion 12",
                    "AndroidManifest.xml" to """<manifest><supports-screens android:largeScreens="true" android:xlargeScreens="true" /></manifest>"""
            ) to null,
            mapOf(
                    "a/build.gradle" to "maxSdkVersion 10",
                    "build.gradle" to "maxSdkVersion 11"
            ) to listOf("max"),
            mapOf(
                    "a/build.gradle" to "maxSdkVersion 11",
                    "build.gradle" to "maxSdkVersion 10"
            ) to null
    )

    for ((files, warnings) in tests.entries) {
      doDesignedForTabletsTest(files, warnings)
    }
  }

  /*
   * External files permission inspection
   */
  val externalFilesPermissionJavaTests = arrayOf(
          true to """Gdx.files.external("");""",
          true to """Gdx.files.absolute("");""",
          false to """Gdx.files.classpath("");""",
          false to """Gdx.files.internal("");""",
          false to """Gdx.files.local("");""",
          true to """Gdx.files.getFileHandle("", Files.FileType.External);""",
          true to """Gdx.files.getFileHandle("", Files.FileType.Absolute);""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Classpath);""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Internal);""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Local);""",
          true to """new FileHandle("")"""
  )

  fun testExternalFilesPermissionInspectionJava() {
    for ((warningExpected, content) in externalFilesPermissionJavaTests) {
      val fileContents = """
        import com.badlogic.gdx.Files;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.files.FileHandle;
        class Test {
          void f() {
            $content
          }
        }
      """
      myFixture.configureByText("Test.java", fileContents)
      doTestExternalFilesPermissionInspection("inspections/missingExternalFilesPermission/manifestWithoutPermission/AndroidManifest.xml", warningExpected)
      doTestExternalFilesPermissionInspection("inspections/missingExternalFilesPermission/manifestWithPermission/AndroidManifest.xml", false)
    }
  }

  val externalFilesPermissionKotlinTests = arrayOf(
          true to """Gdx.files.external("")""",
          true to """Gdx.files.absolute("")""",
          false to """Gdx.files.classpath("")""",
          false to """Gdx.files.internal("")""",
          false to """Gdx.files.local("")""",
          true to """Gdx.files.getFileHandle("", Files.FileType.External)""",
          true to """Gdx.files.getFileHandle("", Files.FileType.Absolute)""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Classpath)""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Internal)""",
          false to """Gdx.files.getFileHandle("", Files.FileType.Local)""",
          true to """FileHandle("")"""
  )

  fun testExternalFilesPermissionInspectionKotlin() {
    for ((warningExpected, content) in externalFilesPermissionKotlinTests) {
      val fileContents = """
        import com.badlogic.gdx.Files
        import com.badlogic.gdx.Gdx
        import com.badlogic.gdx.files.FileHandle
        fun f() {
          $content
        }
      """
      myFixture.configureByText("Test.kt", fileContents)
      doTestExternalFilesPermissionInspection("inspections/missingExternalFilesPermission/manifestWithoutPermission/AndroidManifest.xml", warningExpected)
      doTestExternalFilesPermissionInspection("inspections/missingExternalFilesPermission/manifestWithPermission/AndroidManifest.xml", false)
    }
  }

  fun doTestExternalFilesPermissionInspection(manifestFileName: String, warningExpected: Boolean) {
    myFixture.enableInspections(MissingExternalFilesPermissionInspection())
    myFixture.configureByFile(manifestFileName)
    val hasWarning = myFixture.doHighlighting().any { it.description == message("missing.files.permissions.problem.descriptor") }
    if (warningExpected && !hasWarning) {
      Assert.fail("Warning expected, but no warning found.")
    } else if (!warningExpected && hasWarning) {
      Assert.fail("Unexpected warning")
    }
  }

  /*
   * Other inspections
   */

  fun testStaticResourceInspection() {

    performInspectionsTest(JavaStaticResourceInspection(), "inspections/staticResources/Test1.java")
    performInspectionsTest(KotlinStaticResourceInspection(), "inspections/staticResources/Test1.kt")

  }


  fun testMissingFlushInspection() {

    performInspectionsTest(KotlinMissingFlushInspection(), "inspections/missingFlush/Test.kt")
    performInspectionsTest(JavaMissingFlushInspection(), "inspections/missingFlush/Test.java")

  }

  fun testProfilingCodeInspection() {

    performInspectionsTest(KotlinProfilingCodeInspection(), "inspections/profilingCode/Test.kt")
    performInspectionsTest(JavaProfilingCodeInspection(), "inspections/profilingCode/Test.java")

  }

  fun testLogLevelInspection() {

    performInspectionsTest(JavaLogLevelInspection(), "inspections/logLevel/Test.java")
    performInspectionsTest(KotlinLogLevelInspection(), "inspections/logLevel/Test.kt")

  }

  fun testFlushInsideLoopInspection() {

    performInspectionsTest(KotlinFlushInsideLoopInspection(), "inspections/flushInsideLoop/Test1.kt")
    performInspectionsTest(JavaFlushInsideLoopInspection(), "inspections/flushInsideLoop/Test2.kt", "inspections/flushInsideLoop/Test2.java")
    performInspectionsTest(KotlinFlushInsideLoopInspection(), "inspections/flushInsideLoop/Test3.java", "inspections/flushInsideLoop/Test3.kt")
    performInspectionsTest(KotlinFlushInsideLoopInspection(), "inspections/flushInsideLoop/Test4.kt")
    performInspectionsTest(JavaFlushInsideLoopInspection(), "inspections/flushInsideLoop/Test5.java")

  }

  fun testGDXAssetsFileNameErrorInspection() {

    addAnnotations()
    myFixture.copyDirectoryToProject("assetsInCode/assets", "/")

    performInspectionsTest(JavaGDXAssetsFileNameErrorInspection(), "inspections/GDXAssetsFileName/Test.java")
    performInspectionsTest(KotlinGDXAssetsFileNameErrorInspection(), "inspections/GDXAssetsFileName/Test.kt")

  }

}
