package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.testname
import com.gmail.blueboxware.libgdxplugin.utils.DRAWABLE_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.psi.PsiClass
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.psi.KtClass

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
class TestFindUsages : LibGDXCodeInsightFixtureTestCase() {

    fun testFindUsages1() {
        addFreeType()
        doTest(9)
    }

    fun testFindUsages2() {
        doTest(77)
    }

    fun testFindUsages3() {
        doTest(4)
    }

    fun testFindUsages4() {
        doTest(4)
    }

    fun testFindUsages5() {
        doTest(11)
    }

    fun testFindUsages6() {
        doTest(6)
    }

    fun testFindUsages7() {
        doTest(6)
    }

    fun testFindUsagesWithTaggedClasses1() {
        doTest(6)
    }

    fun testFindUsagesWithTaggedClasses2() {
        doTest(5)
    }

    fun testFindUsagesWithTaggedClasses3() {
        doTest(5)
    }

    fun testFindUsagesAsParentProperty() {
        doTest(2)
    }

    fun testFindDrawableUsages() {
        copyFileToProject("drawableUsages.skin")
        val usagesInfos = myFixture.testFindUsages("drawableUsages.atlas")
        val origin = file.findElementAt(myFixture.caretOffset)?.firstParent<Atlas2Region>()
        assertEquals(10, usagesInfos.size)
        usagesInfos.forEach { usagesInfo ->
            assertNotNull(usagesInfo.element)
            assertTrue(usagesInfo.element is SkinStringLiteral)
            assertEquals(origin, usagesInfo.element?.reference?.resolve())
        }
    }

    fun testFindJavaClassUsagesWithTags() {
        copyFileToProject("findJavaClassUsagesWithTags.skin")
        configureByFile("FindJavaClassUsagesWithTags.java")
        val usagesInfos = myFixture.findUsages(myFixture.elementAtCaret as PsiClass)
        assertEquals(4, usagesInfos.size)
        usagesInfos.forEach { usageInfo ->
            assertEquals(myFixture.elementAtCaret, (usageInfo.element as SkinClassName).resolve())
        }
    }

    fun testFindKotlinClassUsagesWithTags() {
        copyFileToProject("findKotlinClassUsagesWithTags.skin")
        configureByFile("FindKotlinClassUsagesWithTags.kt")
        val usagesInfos = myFixture.findUsages(myFixture.elementAtCaret as KtClass)
        assertEquals(4, usagesInfos.size)
        usagesInfos.forEach { usageInfo ->
            assertEquals(
                (myFixture.elementAtCaret as KtClass).toLightClass(),
                (usageInfo.element as SkinClassName).resolve()
            )
        }
    }

    fun doTest(nrOfUsages: Int) {
        val usagesInfos = myFixture.testFindUsages(testname() + ".skin")
        assertEquals(nrOfUsages, usagesInfos.size)

        val classType =
            (file.findElementAt(myFixture.caretOffset)
                ?.parent
                ?.parent
                    as? SkinResourceName)
                ?.resource
                ?.classSpecification
                ?.classNameAsString

        assertNotNull(classType)
        for (usageInfo in usagesInfos) {
            assertTrue(usageInfo.element is SkinStringLiteral)
            (usageInfo.element as? SkinPropertyValue)?.let { propertyValue ->
                val type = propertyValue.property?.resolveToTypeString()
                assertNotNull(type)
                assertTrue(
                    classType?.dollarName == type
                            || (classType?.dollarName == "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable" && type == DRAWABLE_CLASS_NAME)
                )
            }
            (usageInfo.element as? SkinStringLiteral)?.let { stringLiteral ->
                assertNotNull((usageInfo.element as? SkinStringLiteral)?.value)
                assertEquals(
                    (stringLiteral.reference?.resolve() as? SkinResource)?.name,
                    (usageInfo.element as? SkinStringLiteral)?.value
                )
            }
        }
    }

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addDummyLibGDX199()
        addKotlin()
        addAnnotations()

        copyFileToProject("KotlinClass.kt", "/KotlinClass.kt")
    }

    override fun getBasePath() = "/filetypes/skin/findUsages/"

}
