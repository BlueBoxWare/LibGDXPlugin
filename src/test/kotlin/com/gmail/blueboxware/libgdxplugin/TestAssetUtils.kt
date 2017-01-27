package com.gmail.blueboxware.libgdxplugin
import com.gmail.blueboxware.libgdxplugin.utils.AssetUtils
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

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
class TestAssetUtils : LibGDXCodeInsightFixtureTestCase() {

  var skinFile: VirtualFile? = null
  var atlasFile: VirtualFile? = null

  override fun setUp() {
    super.setUp()

    myFixture.copyDirectoryToProject("assets/", "assets/")

    FilenameIndex.getVirtualFilesByName(myFixture.project, "ui.json", GlobalSearchScope.allScope(myFixture.project))?.firstOrNull()?.let { file ->
      skinFile = file
    }

    FilenameIndex.getVirtualFilesByName(myFixture.project, "ui.atlas", GlobalSearchScope.allScope(myFixture.project))?.firstOrNull()?.let { file ->
      atlasFile = file
    }

    assertNotNull(skinFile)
    assertNotNull(atlasFile)
  }

  fun testgetAssociatedAtlas() {
    assertEquals(AssetUtils.getAssociatedAtlas(skinFile!!), atlasFile!!)
  }

  fun testgetAssociatedFiles() {
    assertTrue(AssetUtils.getAssociatedFiles(skinFile!!).map { it.name }.containsAll(listOf("somefile", "anotherfile", "ui.atlas")))
  }

  fun testReadImageNamesFromAtlas() {
    val imageNames = AssetUtils.readImageNamesFromAtlas(atlasFile!!)
    assertEquals(25, imageNames.size)
    assertTrue(imageNames.containsAll(listOf(
            "check-off",
            "textfield",
            "check-on",
            "cursor",
            "default",
            "default-pane",
            "default-rect-pad",
            "default-pane-noborder",
            "default-rect",
            "default-rect-down",
            "default-round",
            "default-round-down",
            "default-round-large",
            "default-scroll",
            "default-select",
            "default-select-selection",
            "default-slider",
            "default-slider-knob",
            "default-splitpane",
            "default-splitpane-vertical",
            "default-window",
            "selection",
            "tree-minus",
            "tree-plus",
            "white"
    )))
  }

}