package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaClassReference
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

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

  fun testFieldReference1() {
    doTestFieldReference()
  }

  fun testFieldReference2() {
    doTestFieldReference()
  }

  fun testFieldReference3() {
    doTestFieldReference("com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::disabledFontColor")
  }

  fun testFieldReference4() {
    doTestFieldReference()
  }

  fun testFieldReference5() {
    doTestFieldReference("com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle::checked")
  }

  fun testResourceAliasReference1() {
    doTestResourceAliasReference()
  }

  fun testResourceAliasReference2() {
    doTestResourceAliasReference()
  }

  fun doTestFieldReference(expectedFieldName: String? = null) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
    val sourceElement = PsiTreeUtil.findFirstParent(elementAtCaret, { it is SkinPropertyName }) as? SkinPropertyName
    assertNotNull(sourceElement)
    val field = sourceElement?.reference?.resolve() as? PsiField
    assertNotNull(field)
    val expectedName = expectedFieldName ?: SkinClassSpecificationMixin.removeDollarFromClassName(sourceElement?.property?.containingClassSpecification?.classNameAsString!!) + "::" + field?.name
    assertEquals(expectedName, field!!.containingClass?.qualifiedName + "::" + field.name)
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

  fun doTestResourceAliasReference() {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent as? SkinStringLiteral
    assertNotNull(element)
    val resource = element?.reference?.resolve() as? SkinResource
    assertNotNull(resource)
    assertEquals(element!!.value, resource!!.name)
    assertEquals(PsiTreeUtil.findFirstParent(element, { it is SkinClassSpecification} ), resource.classSpecification)
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

    addLibGDX()

    myFixture.copyFileToProject("com/example/MyTestClass.java", "com/example/MyTestClass.java")
  }

  override fun getBasePath() = "/filetypes/skin/references/"
}