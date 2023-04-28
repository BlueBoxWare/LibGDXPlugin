package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.ui.ImagePreviewPsiDocumentationTargetProvider
import java.awt.Color
import java.net.URL
import javax.imageio.ImageIO

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
class TestTextTextureRegionPreview : LibGDXCodeInsightFixtureTestCase() {

    fun testPreviewInAtlas() {
        doTest("test.atlas", false, 48, 40)
    }

    fun testPreviewInSkin() {
        doTest("test.skin")
    }

    fun testTintedDrawable() {
        doTest("test2.skin")
    }

    fun testPreviewInJavaWithSkin() {
        doTest("Test1.java")
    }

    fun testPreviewInJavaWithTextureAtlas() {
        doTest("Test2.java", false)
    }

    fun doTest(fileName: String, tinted: Boolean = true, width: Int = 50, height: Int = 50) {
        configureByFile(fileName)
        val element = file.findElementAt(myFixture.caretOffset) ?: throw AssertionError()

        @Suppress("OverrideOnly") val preview: String =
            ImagePreviewPsiDocumentationTargetProvider().documentationTarget(element, element)
                ?.computeDocumentationHint() ?: throw AssertionError()

        val imageFile = Regex("""src="([^"]+)"""").find(preview)?.groupValues?.get(1) ?: throw AssertionError()
        val image = ImageIO.read(URL(imageFile))
        assertEquals(width, image.width)
        assertEquals(height, image.height)
        if (tinted) {
            @Suppress("UseJBColor") assertEquals(Color.YELLOW, Color(image.getRGB(1, 1)))
        }
    }

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addAnnotations()

        copyDirectoryToProject("", "")

    }

    override fun getBasePath() = "textureRegionPreview/"

}
