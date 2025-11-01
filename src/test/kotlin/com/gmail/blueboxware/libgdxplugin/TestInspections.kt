package com.gmail.blueboxware.libgdxplugin

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
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_PREFIXES_MAP
import com.intellij.analysis.AnalysisScope
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInspection.CommonProblemDescriptor
import com.intellij.codeInspection.GlobalInspectionTool
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ex.GlobalInspectionContextEx
import com.intellij.codeInspection.ex.GlobalInspectionToolWrapper
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.testFramework.InspectionTestUtil
import com.intellij.testFramework.createGlobalContextForTool
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
import org.jetbrains.plugins.groovy.GroovyFileType
import org.junit.Assert
import java.io.File

class TestInspections : LibGDXCodeInsightFixtureTestCase() {

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
            doUnsafeIteratorInspectionTest(clazz)
        }
    }

    fun testOpenGLDirectiveInspectionWrongFilename() {
        doOpenGLDirectiveInspectionTest("wrongFilename1/Androidmanifest.xml", false)
    }

    fun testOpenGLDirectiveInspectionDirectiveIsMissing() {
        doOpenGLDirectiveInspectionTest("missingDirective1/AndroidManifest.xml", true, problemElement = null)
        doOpenGLDirectiveInspectionTest("missingDirective2/AndroidManifest.xml", true)
        doOpenGLDirectiveInspectionTest("missingDirective3/AndroidManifest.xml", true)
        doOpenGLDirectiveInspectionTest("missingDirective4/AndroidManifest.xml", true, problemElement = null)
    }

    fun testOpenGLDirectiveInspectionDirectiveIsPresent() {
        doOpenGLDirectiveInspectionTest("directiveIsPresent1/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent2/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent3/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent4/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent5/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent6/AndroidManifest.xml", false)
        doOpenGLDirectiveInspectionTest("directiveIsPresent7/AndroidManifest.xml", false)
    }

    fun testTestIdsInspectionWithIdMap() {
        val stringBuilderKotlin = StringBuilder()
        val stringBuilderJava = StringBuilder("class Test {\n")
        val stringBuilderXml = StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>")
        val stringBuilderBuildGradle = StringBuilder()
        val stringBuilderGradleProperties = StringBuilder()

        var idCount = 0
        for (testId in TEST_ID_MAP.keys + TEST_ID_PREFIXES_MAP.keys) {

            assertFalse(testId.isEmpty())
            stringBuilderKotlin.append("val id$idCount = \"<warning>$testId</warning>\"\n")
            stringBuilderJava.append("String id$idCount = <warning>\"$testId\"</warning>;\n")
            stringBuilderXml.append("<warning><string name=\"id$idCount\">$testId</string></warning>\n")
            stringBuilderBuildGradle.append("single$idCount = <warning>'$testId'</warning>\n")
            stringBuilderBuildGradle.append("double$idCount = <warning>\"$testId\"</warning>\n")
            stringBuilderBuildGradle.append("triple$idCount = <warning>'''$testId'''</warning>\n")
            stringBuilderBuildGradle.append("//noinspection GDXGradleTestId\n")
            stringBuilderBuildGradle.append("suppressed$idCount = '$testId'\n")
            stringBuilderGradleProperties.append("<warning>id$idCount=$testId</warning>\n")
            stringBuilderGradleProperties.append("<warning>idQuoted$idCount=\"$testId\"</warning>\n")
            stringBuilderGradleProperties.append("# suppress inspection \"GDXGradlePropertiesTestId\"\n")
            stringBuilderGradleProperties.append("suppressed$idCount=$testId\n")

            idCount++

        }

        stringBuilderJava.append("}")
        stringBuilderXml.append("</resources>")

        doInspectionTestWithString(
            stringBuilderKotlin.toString(), KotlinTestIdsInspection(), "Test.kt"
        )
        doInspectionTestWithString(
            stringBuilderJava.toString(), JavaTestIdsInspection(), "Test.java"
        )
        doInspectionTestWithString(
            stringBuilderXml.toString(), XmlTestIdsInspection(), "Test.xml"
        )
        doInspectionTestWithString(
            stringBuilderBuildGradle.toString(), GradleTestIdsInspection(), "build.gradle"
        )
        doInspectionTestWithString(
            stringBuilderGradleProperties.toString(), GradlePropertiesTestIdsInspection(), "gradle.properties"
        )

    }

    fun testDesignedForTabletsManifestOnly() {

        val tests = mapOf(
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11" android:maxSdkVersion="11" />""" to null,
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="11" android:maxSdkVersion="11" />""" to listOf(
                "missing.support.screens"
            ),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="10" android:maxSdkVersion="11" />""" to null,
            """<uses-sdk android:minSdkVersion="13" />""" to null,
            """<uses-sdk android:targetSdkVersion="11" />""" to listOf("missing.support.screens"),
            """<uses-sdk android:minSdkVersion="99" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11" android:maxSdkVersion="10" />""" to listOf(
                "max"
            ),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" android:maxSdkVersion="10" />""" to listOf(
                "target.or.min", "missing.support.screens"
            ),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" android:maxSdkVersion="11" />""" to listOf(
                "target.or.min", "missing.support.screens"
            ),
            """<uses-sdk android:minSdkVersion="10" android:targetSdkVersion="9" />""" to listOf(
                "target.or.min", "missing.support.screens"
            ),
            """<uses-sdk android:maxSdkVersion="10" />""" to listOf("target.or.min", "missing.support.screens"),

            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="true" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true"  android:smallScreens="false" />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens  android:normalScreens="false" android:smallScreens="false"  />""" to null,
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="false" />""" to listOf(
                "large.false"
            ),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="false" />""" to listOf(
                "large.false"
            ),
            """<uses-sdk android:minSdkVersion="13" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="true" android:largeScreens="false"/>""" to listOf(
                "large.false"
            ),
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" android:xlargeScreens="true" />""" to null,
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:largeScreens="true" android:normalScreens="true" android:smallScreens="false" />""" to listOf(
                "large.missing"
            ),
            """<uses-sdk android:minSdkVersion="11" android:targetSdkVersion="11"/><supports-screens android:xlargeScreens="true" />""" to listOf(
                "large.missing"
            ),
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
                "build.gradle" to "minSdkVersion 11", "a/build.gradle" to "targetSdkVersion 11"
            ) to null, mapOf(
                "build.gradle" to "minSdkVersion 10", "a/build.gradle" to "targetSdkVersion 10"
            ) to listOf("target.or.min"), mapOf(
                "build.gradle" to "targetSdkVersion 10", "a/build.gradle" to "minSdkVersion 10"
            ) to listOf("target.or.min"), mapOf(
                "build.gradle" to "targetSdkVersion 10\nminSdkVersion 11", "a/build.gradle" to "minSdkVersion 10"
            ) to null, mapOf(
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
            ) to listOf("missing.support.screens"), mapOf(
                "build.gradle" to "targetSdkVersion 10",
                "AndroidManifest.xml" to """<manifest><uses-sdk android:minSdkVersion="13" /></manifest>"""
            ) to null, mapOf(
                "build.gradle" to "targetSdkVersion 10\nminSdkVersion 10",
                "a/build.gradle" to "targetSdkVersion 11",
                "AndroidManifest.xml" to """<manifest><uses-sdk android:minSdkVersion="13" /></manifest>"""
            ) to null, mapOf(
                "build.gradle" to "minSdkVersion 10",
                "a/build.gradle" to "minSdkVersion 11",
                "b/build.gradle" to "targetSdkVersion 12",
                "AndroidManifest.xml" to """<manifest><supports-screens android:largeScreens="true" android:xlargeScreens="true" /></manifest>"""
            ) to null, mapOf(
                "a/build.gradle" to "maxSdkVersion 10", "build.gradle" to "maxSdkVersion 11"
            ) to listOf("max"), mapOf(
                "a/build.gradle" to "maxSdkVersion 11", "build.gradle" to "maxSdkVersion 10"
            ) to null
        )

        for ((files, warnings) in tests.entries) {
            doDesignedForTabletsTest(files, warnings)
        }
    }

    private val externalFilesPermissionJavaTests = arrayOf(
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
            configureByText("Test.java", fileContents)
            doExternalFilesPermissionInspectionTest(
                "missingExternalFilesPermission/manifestWithoutPermission/AndroidManifest.xml", warningExpected
            )
            doExternalFilesPermissionInspectionTest(
                "missingExternalFilesPermission/manifestWithPermission/AndroidManifest.xml", false
            )
        }
    }

    private val externalFilesPermissionKotlinTests = arrayOf(
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
            configureByText("Test.kt", fileContents)
            doExternalFilesPermissionInspectionTest(
                "missingExternalFilesPermission/manifestWithoutPermission/AndroidManifest.xml", warningExpected
            )
            doExternalFilesPermissionInspectionTest(
                "missingExternalFilesPermission/manifestWithPermission/AndroidManifest.xml", false
            )
        }
    }

    fun testStaticResourceInspection() {

        doInspectionsTest(JavaStaticResourceInspection(), "staticResources/Test1.java")
        doInspectionsTest(KotlinStaticResourceInspection(), "staticResources/Test1.kt")

    }


    fun testMissingFlushInspection() {

        doInspectionsTest(KotlinMissingFlushInspection(), "missingFlush/Test.kt")
        doInspectionsTest(JavaMissingFlushInspection(), "missingFlush/Test.java")

    }

    fun testProfilingCodeInspection() {

        doInspectionsTest(KotlinProfilingCodeInspection(), "profilingCode/Test.kt")
        doInspectionsTest(JavaProfilingCodeInspection(), "profilingCode/Test.java")

    }

    fun testLogLevelInspection() {

        doInspectionsTest(JavaLogLevelInspection(), "logLevel/Test.java")
        doInspectionsTest(KotlinLogLevelInspection(), "logLevel/Test.kt")

    }

    fun testFlushInsideLoopInspection() {

        doInspectionsTest(KotlinFlushInsideLoopInspection(), "flushInsideLoop/Test1.kt")
        doInspectionsTest(JavaFlushInsideLoopInspection(), "flushInsideLoop/Test2.kt", "flushInsideLoop/Test2.java")
        doInspectionsTest(KotlinFlushInsideLoopInspection(), "flushInsideLoop/Test3.java", "flushInsideLoop/Test3.kt")
        doInspectionsTest(KotlinFlushInsideLoopInspection(), "flushInsideLoop/Test4.kt")
        doInspectionsTest(JavaFlushInsideLoopInspection(), "flushInsideLoop/Test5.java")

    }

    fun testGDXAssetsAnnotationInspection() {

        addAnnotations()
        copyDirectoryToProject("assets", "/")

        doInspectionsTest(JavaGDXAssetsInspection(), "GDXAssetsAnnotation/Test.java")
        doInspectionsTest(KotlinGDXAssetsInspection(), "GDXAssetsAnnotation/Test.kt")

    }

    fun testNonExistingAssetInspection() {

        addAnnotations()
        addLibGDX113()
        copyDirectoryToProject("assets", "/")

        doInspectionsTest(JavaNonExistingAssetInspection(), "nonExistingAsset/Test.java")
        doInspectionsTest(KotlinNonExistingAssetInspection(), "nonExistingAsset/Test.kt")

    }

    fun testUnusedClassTagInspection() {

        addAnnotations()
        addLibGDX113()
        copyDirectoryToProject("assets", "/")

        doInspectionsTest(JavaUnusedClassTagInspection(), "unusedClassTag/Test.java")
        doInspectionsTest(KotlinUnusedClassTagInspection(), "unusedClassTag/Test.kt")

    }

    private fun doInspectionsTest(inspection: LocalInspectionTool, vararg fileNames: String) {
        myFixture.enableInspections(inspection::class.java)
        myFixture.testHighlightingAllFiles(true, false, false, *fileNames)
    }

    private fun doInspectionTestWithString(text: String, inspection: LocalInspectionTool, fileName: String) {

        configureByText(fileName, text)
        myFixture.enableInspections(inspection::class.java)
        myFixture.checkHighlighting(true, false, false)

    }

    private fun getHighLightsWithDescription(
        inspection: LocalInspectionTool, vararg fileNames: String, warningDescription: String
    ): List<HighlightInfo> {

        configureByFiles(*fileNames)
        myFixture.enableInspections(inspection::class.java)
        return myFixture.doHighlighting().filter { it.description?.contains(warningDescription) == true }

    }

    @Suppress("SameParameterValue")
    private fun getGlobalInspectionResults(
        testDir: String, inspection: GlobalInspectionTool
    ): Collection<CommonProblemDescriptor> {

        val toolWrapper = GlobalInspectionToolWrapper(inspection)

        val sourceDir = copyDirectoryToProject(File(testDir, "src").path, "src")
        val psiDirectory =
            myFixture.psiManager.findDirectory(sourceDir) ?: throw AssertionError("Could not find $sourceDir")

        val scope = AnalysisScope(psiDirectory)
        scope.invalidate()

        val globalContext = createGlobalContextForTool(scope, project, listOf(toolWrapper))

        InspectionTestUtil.runTool(toolWrapper, scope, globalContext)

        (myFixture.tempDirFixture as? LightTempDirTestFixtureImpl)?.deleteAll()

        return (globalContext as GlobalInspectionContextEx).getPresentation(toolWrapper).problemDescriptors
    }

    private fun doTestGlobalInspection(
        @Suppress("SameParameterValue") testDir: String, inspection: GlobalInspectionTool, warnings: List<String>
    ) {

        val expectedWarnings = warnings.toMutableList()
        val problemDescriptors = getGlobalInspectionResults(testDir, inspection)

        for (problem in problemDescriptors) {
            val msg = problem.descriptionTemplate
            if (!expectedWarnings.remove(msg)) {
                fail("Unexpected warning $msg in:\n" + ppDirContents(testDir))
            }
        }

        if (expectedWarnings.isNotEmpty()) {
            fail(
                "Expected warning(s) not found: " + expectedWarnings.joinToString("\n") + " in:\n" + ppDirContents(
                    testDir
                )
            )
        }

    }

    private fun ppDirContents(testDir: String): String {
        val dirName = testDataPath + testDir + "src/"
        val dir = File(dirName)
        val stringBuilder = StringBuilder()

        val files = dir.listFiles().flatMap {
            if (it.isDirectory) it.listFiles().toList() else listOf(it)
        }.sortedBy { it.absolutePath }

        for (file in files) {
            if (file.isFile) {
                val name = file.path.removePrefix(dirName)
                stringBuilder.append("\t[" + name + "]: \n\"" + file.readText() + "\"\n")
            }
        }

        return stringBuilder.toString()
    }

    private fun doUnsafeIteratorInspectionTest(name: String) {
        val lName = "unsafeIterators/$name"
        doInspectionsTest(JavaUnsafeIteratorInspection(), "$lName.java")
        doInspectionsTest(KotlinUnsafeIteratorInspection(), "$lName.kt")
    }

    private fun doOpenGLDirectiveInspectionTest(
        name: String, warningExpected: Boolean, problemElement: String? = "uses-feature"
    ) {
        val lName = "missingOpenGLDirective/$name"
        val hightLights = getHighLightsWithDescription(
            OpenGLESDirectiveInspection(), lName, warningDescription = message("no.opengl.directive.problem.descriptor")
        )

        for (hightLight in hightLights) {
            if (warningExpected) {
                if (problemElement != null && !hightLight.text.startsWith("<$problemElement")) {
                    fail(
                        "$name: Hightlight starts at wrong element: '" + hightLight.text.substring(
                            0, 30
                        ) + "'"
                    )
                    return
                } else {
                    return
                }
            } else {
                fail(
                    "$name: Unexpected highlight starting at '" + hightLight.text.substring(
                        0, 30
                    ) + "'"
                )
                return
            }
        }

        if (warningExpected) {
            fail("$name: Expected highlight not found")
        }

    }

    private fun cleanUp(dirName: String) {
        val dir = File(dirName)

        for (file in dir.listFiles().flatMap { if (it.isDirectory) it.listFiles().toList() else listOf(it) }) {
            if (file.isFile) {
                file.delete()
            }
        }
    }

    private fun doDesignedForTabletsTest(files: Map<String, String>, warnings: List<String>?) {

        val fakeProjectDir = "/designedForTablets/"
        val fullDir = testDataPath + fakeProjectDir + "src/"

        cleanUp(fullDir)

        for ((fileName, content) in files.entries) {
            val file = File(fullDir + fileName)
            file.writeText(content)
        }

        doTestGlobalInspection(
            fakeProjectDir,
            DesignedForTabletsInspection(),
            warnings?.map { message("designed.for.tablets.problem.descriptor.$it") } ?: listOf())

    }

    private fun doExternalFilesPermissionInspectionTest(manifestFileName: String, warningExpected: Boolean) {
        myFixture.enableInspections(MissingExternalFilesPermissionInspection::class.java)
        configureByFile(manifestFileName)
        val hasWarning =
            myFixture.doHighlighting().any { it.description == message("missing.files.permissions.problem.descriptor") }
        if (warningExpected && !hasWarning) {
            Assert.fail("Warning expected, but no warning found.")
        } else if (!warningExpected && hasWarning) {
            Assert.fail("Unexpected warning")
        }
    }

    override fun getBasePath() = "inspections"

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addKotlin()

        WriteCommandAction.runWriteCommandAction(project) {
            FileTypeManager.getInstance().associateExtension(GroovyFileType.GROOVY_FILE_TYPE, "gradle")
        }

        project.service<LibGDXPluginSettings>().enableColorAnnotations = false

    }

}
