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
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradlePropertiesTestIdsInspection
import com.gmail.blueboxware.libgdxplugin.inspections.java.*
import com.gmail.blueboxware.libgdxplugin.inspections.kotlin.*
import com.gmail.blueboxware.libgdxplugin.inspections.xml.MissingExternalFilesPermissionInspection
import com.gmail.blueboxware.libgdxplugin.inspections.xml.OpenGLESDirectiveInspection
import com.gmail.blueboxware.libgdxplugin.inspections.xml.XmlTestIdsInspection
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_MAP
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_PREFIXES_MAP
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.openapi.components.service
import org.junit.Assert

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
        val stringBuilderGradleProperties = StringBuilder()

        for ((idCount, testId) in (TEST_ID_MAP.keys + TEST_ID_PREFIXES_MAP.keys).withIndex()) {

            assertFalse(testId.isEmpty())
            stringBuilderKotlin.append("val id$idCount = \"<warning>$testId</warning>\"\n")
            stringBuilderJava.append("String id$idCount = <warning>\"$testId\"</warning>;\n")
            stringBuilderXml.append("<warning><string name=\"id$idCount\">$testId</string></warning>\n")
            stringBuilderGradleProperties.append("<warning>id$idCount=$testId</warning>\n")
            stringBuilderGradleProperties.append("<warning>idQuoted$idCount=\"$testId\"</warning>\n")
            stringBuilderGradleProperties.append("# suppress inspection \"GDXGradlePropertiesTestId\"\n")
            stringBuilderGradleProperties.append("suppressed$idCount=$testId\n")

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
            stringBuilderGradleProperties.toString(), GradlePropertiesTestIdsInspection(), "gradle.properties"
        )

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

        project.service<LibGDXPluginSettings>().enableColorAnnotations = false

    }

}
