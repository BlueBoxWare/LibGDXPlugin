package com.gmail.blueboxware.libgdxplugin.bitmapFont

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasValue
import com.gmail.blueboxware.libgdxplugin.utils.getParentOfType
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiFile

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
class TestReferences : LibGDXCodeInsightFixtureTestCase() {

  fun testImageFileReference() {
    myFixture.configureByFile("assets/test1.atlas")

    val atlasValue = myFixture.file.findElementAt(myFixture.caretOffset)?.getParentOfType<AtlasValue>()
    assertNotNull(atlasValue)
    val reference = atlasValue!!.reference
    assertNotNull(reference)
    val psiFile = reference?.resolve() as PsiFile
    assertNotNull(psiFile)
    assertEquals("/src/assets/images/test.png", VfsUtilCore.getRelativeLocation(psiFile.virtualFile, project.baseDir))

  }

  override fun setUp() {
    super.setUp()

    myFixture.copyFileToProject("assets/images/test.png", "assets/images/test.png")
  }

  override fun getBasePath() = "/filetypes/bitmapFont/references/"

}