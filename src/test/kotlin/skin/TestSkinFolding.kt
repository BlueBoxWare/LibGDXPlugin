package skin

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import getTestDataPathFromProperty

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
class TestSkinFolding: LightCodeInsightFixtureTestCase() {

  fun testFolding() {
//    myFixture.configureByFile("test.json")
//    markFileAsSkin(myFixture.project, myFixture.file.virtualFile)
    myFixture.testFolding(testDataPath + "test.skin")
  }

  override fun getTestDataPath() = getTestDataPathFromProperty() + "/filetypes/skin/folding/"
}