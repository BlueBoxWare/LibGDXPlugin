package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.*
import com.gmail.blueboxware.libgdxplugin.message

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
class TestSuppressQuickfixes : LibGDXCodeInsightFixtureTestCase() {

    fun testSuppressForObject1() = doTestQuickFix(
        SkinNonExistingFieldInspection::class, message("suppress.object"), "skin"
    )

    fun testSuppressForObject2() = doTestQuickFix(
        SkinMissingPropertyInspection::class, message("suppress.object"), "skin"
    )

    fun testSuppressForObject3() {
        copyFileToProject("ColorArrayHolder.java")
        doTestQuickFix(
            SkinTypeInspection::class, message("suppress.object"), "skin"
        )
    }

    fun testSuppressForObject4() = doTestQuickFix(
        SkinNonExistingFieldInspection::class, message("suppress.object"), "skin"
    )

    fun testSuppressForObject5() = doTestQuickFix(
        SkinNonExistingFieldInspection::class, message("suppress.object"), "skin"
    )


    fun testSuppressForClassSpec1() = doTestQuickFix(
        SkinDuplicateResourceNameInspection::class, message("suppress.object"), "skin"
    )

    fun testSuppressForClassSpec2() = doTestQuickFix(
        SkinDuplicateResourceNameInspection::class, message("suppress.object"), "skin"
    )

    fun testSuppressForClassSpec3() = doTestQuickFix(
        SkinDuplicateResourceNameInspection::class, message("suppress.object"), "skin"
    )


    fun testSuppressForClassSpec4() {
        addLibGDX113()
        doTestQuickFix(
            SkinAbbrClassInspection::class, message("suppress.object"), "skin"
        )
    }

    fun testSuppressForFile1() = doTestQuickFix(
        SkinNonExistingClassInspection::class, message("suppress.file"), "skin"
    )

    fun testSuppressForFile2() = doTestQuickFix(
        SkinDuplicateResourceNameInspection::class, message("suppress.file"), "skin"
    )


    fun testSuppressForFile3() {
        copyFileToProject("ColorArrayHolder.java")
        doTestQuickFix(
            SkinTypeInspection::class, message("suppress.file"), "skin"
        )
    }

    fun testSuppressNonexistingClass() = doTestQuickFix(
        SkinNonExistingClassInspection::class, message("suppress.object"), "skin"
    )


    override fun setUp() {
        super.setUp()
        addLibGDX()
    }

    override fun getBasePath() = "filetypes/skin/suppressQuickfixes"

}
