package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase

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
abstract class AssetsInCodeCodeInsightFixtureTestCase : LibGDXCodeInsightFixtureTestCase() {

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addAnnotations()
    addKotlin()

    myFixture.copyFileToProject("assets/libgdx.skin")
    myFixture.copyFileToProject("assets/dir/holo.json")
    myFixture.copyFileToProject("assets/dir/holo.skin")
    myFixture.copyFileToProject("assets/dir/skin.json")
    myFixture.copyFileToProject("assets/dir/test.pack")
    myFixture.copyFileToProject("assets/dir/something")
    myFixture.copyFileToProject("src/JavaSkinTest.java")
    myFixture.copyFileToProject("src/KotlinSkinTest.kt")

  }

  override fun getBasePath() = "/assetsInCode"

}