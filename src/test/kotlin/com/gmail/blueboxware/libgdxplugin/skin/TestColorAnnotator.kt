package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent

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
class TestColorAnnotator : LibGDXCodeInsightFixtureTestCase() {

  fun testHighlighting() {
    myFixture.configureByFile("1.skin")
    val d= myFixture.doHighlighting()
    myFixture.checkHighlighting(false, false, true)
  }

  override fun setUp() {
    super.setUp()

    project.getComponent(LibGDXProjectComponent::class.java).isTesting = true
  }

  override fun getBasePath() = "filetypes/skin/colorAnnotator/"
}