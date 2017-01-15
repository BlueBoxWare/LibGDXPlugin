package skin

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinPropertyValue
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaClassReference
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import getTestDataPathFromProperty

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
class TestReferences : LightCodeInsightFixtureTestCase() {

  fun testResourceReference1() {
    doTestResourceReference("white", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceReference2() {
    doTestResourceReference("white", "com.badlogic.gdx.graphics.g2d.BitmapFont")
  }
  fun testResourceReference3() {
    doTestResourceReference("blue", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceReference4() {
    doTestResourceReference("blue", "com.example.MyTestClass")
  }

  fun testJavaClassReference1() {
    doTestJavaClassReference("com.example.MyTestClass")
  }

  fun testJavaClassReference2() {
    doTestJavaClassReference("com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle")
  }

  fun testBitmapFontReference() {
    myFixture.copyFileToProject("bitmap.fnt")
    doTestFileReference(SkinPropertyValue::class.java, "bitmap.fnt")
  }

  fun doTestFileReference(sourceElementClass: Class<*>, expectedFileName: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
    val sourceElement = PsiTreeUtil.findFirstParent(elementAtCaret, { sourceElementClass.isInstance(it) })
    assertNotNull(sourceElement)
    val file = sourceElement?.reference?.resolve() as? PsiFile
    assertNotNull(file)
    assertEquals(expectedFileName, file?.name)
  }

  fun doTestResourceReference(resourceName: String, resourceType: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent
    assertNotNull(element)
    val resource = element?.reference?.resolve() as? SkinResource
    assertNotNull(resource)
    assertEquals(resourceName, resource?.name)
    assertEquals(resourceType, resource?.classSpecification?.classNameAsString)
  }

  fun doTestJavaClassReference(className: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element: SkinClassName? = myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent as? SkinClassName
    assertNotNull(element)
    val clazz = (element?.reference as? SkinJavaClassReference)?.multiResolve(false)?.firstOrNull()?.element as? PsiClass
    assertNotNull(clazz)
    assertEquals(className, clazz?.qualifiedName)
  }

  override fun setUp() {
    super.setUp()

    PsiTestUtil.addLibrary(myFixture.module, getTestDataPathFromProperty() + "/lib/gdx.jar")

    myFixture.copyFileToProject("com/example/MyTestClass.java", "com/example/MyTestClass.java")
  }

  override fun getTestDataPath() = getTestDataPathFromProperty() + "/filetypes/skin/references/"
}