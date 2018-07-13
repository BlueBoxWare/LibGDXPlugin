package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasRegion
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaClassReference
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.removeDollarFromClassName
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

  fun testResourceReference5() {
    doTestResourceReference("blue", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceReference6() {
    doTestResourceReference("ddd", "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle")
  }

  fun testResourceReference7() {
    doTestResourceReference(null, "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testResourceReference8() {
    doTestResourceReference("d1", "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle")
  }

  fun testResourceReferenceWithTaggedClasses1() {
    doTestResourceReference("bar", "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testResourceReferenceWithTaggedClasses2() {
    doTestResourceReference("foo", "com.example.MyTestClass")
  }

  fun testResourceReferenceWithTaggedClasses3() {
    doTestResourceReference("bar", "com.example.MyTestClass")
  }

  fun testResourceReferenceWithTaggedClasses4() {
    doTestResourceReference("bar", "com.example.MyTestClass")
  }

  fun testResourceReferenceWithTaggedClasses5() {
    doTestResourceReference("foo", "com.example.MyTestClass")
  }

  fun testResourceReferenceWithTaggedClasses6() {
    doTestResourceReference("bar", "com.example.KTestClass")
  }

  fun testResourceReferenceWithTaggedClasses7() {
    doTestResourceReference("foo", "com.example.KTestClass")
  }

  fun testParentResourceReferenceFromSuperClass1() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.Button\$ButtonStyle")
  }

  fun testParentResourceReferenceFromSuperClassWithKotlin1() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.Button\$ButtonStyle")
  }

  fun testParentResourceReferenceFromSuperClassWithKotlin2() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.Button\$ButtonStyle")
  }

  fun testParentResourceReferenceFromSuperClassWithKotlin3() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle")
  }

  fun testParentResourceReferenceFromSuperClassWithKotlin4() {
    doTestResourceReference(null, null)
  }

  fun testParentResourceReferenceFromSuperClassWithKotlin5() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle")
  }

  fun testResourceAliasReference1() {
    doTestResourceReference("yellow", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceAliasReference2() {
    doTestResourceReference("yellow", "com.badlogic.gdx.scenes.scene2d.ui.TextField\$TextFieldStyle")
  }

  fun testResourceAliasReference3() {
    doTestResourceReference("dark-gray", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceAliasReferenceWithTaggedClasses1() {
    doTestResourceReference("foo", "com.example.MyTestClass")
  }

  fun testResourceAliasReferenceWithTaggedClasses2() {
    doTestResourceReference("foo", "com.example.KTestClass")
  }

  fun testResourceAliasReferenceWithTaggedClasses3() {
    doTestResourceReference("foo", "com.example.KTestClass")
  }

  fun testResourceAliasReferenceWithTaggedClasses4() {
    doTestResourceReference("foo", "com.example.KTestClass")
  }

  fun testResourceAliasReferenceWithTaggedClasses5() {
    doTestResourceReference("red", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceAliasReferenceWithTaggedClasses6() {
    doTestResourceReference("red", "com.badlogic.gdx.graphics.Color")
  }

  fun testResourceAliasReferenceWithTaggedClasses7() {
    doTestResourceReference("foo", "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testResourceAliasReferenceWithTaggedClasses8() {
    doTestResourceReference("foo", "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testResourceReferenceTintedDrawable() {
    doTestResourceReference("round-down", "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testResourceReferenceTintedDrawableWithTaggedClasses() {
    doTestResourceReference("round-down", "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable")
  }

  fun testParentReference() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.CheckBox\$CheckBoxStyle")
  }

  fun testParentReferencePre199() {
    removeDummyLibGDX199()
    doTestResourceReference(null, null)
  }

  fun testParentReferenceTagged() {
    doTestResourceReference("main", "com.badlogic.gdx.scenes.scene2d.ui.CheckBox\$CheckBoxStyle")
  }

  fun testJavaClassReference1() {
    doTestJavaClassReference("com.example.MyTestClass")
  }

  fun testJavaClassReference2() {
    doTestJavaClassReference("com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle")
  }

  fun testJavaClassReference3() {
    myFixture.configureByFile("javaClassReference3.skin")
    val element: SkinClassName? = myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent as? SkinClassName
    assertNotNull(element)
    assertTrue(element!!.multiResolve().isEmpty())
  }

  fun testTaggedClassReference1() {
    doTestJavaClassReference("com.example.MyTestClass")
  }

  fun testTaggedClassReference2() {
    doTestJavaClassReference("com.example.KTestClass")
  }

  fun testBitmapFontReference() {
    myFixture.copyFileToProject("bitmap.fnt")
    doTestFileReference(SkinStringLiteral::class.java, "bitmap.fnt")
  }

  fun testBitmapFontReferenceWithTaggedClasses1() {
    myFixture.copyFileToProject("bitmap.fnt")
    doTestFileReference(SkinStringLiteral::class.java, "bitmap.fnt")
  }

  fun testBitmapFontReferenceWithTaggedClasses2() {
    myFixture.copyFileToProject("bitmap.fnt")
    doTestFileReference(SkinStringLiteral::class.java, "bitmap.fnt")
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

  fun testFieldReference6() {
    doTestFieldReference("com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle::checkedOffsetX")
  }

  fun testTaggedClassFieldReference1() {
    doTestFieldReference("com.example.MyTestClass::name")
  }

  fun testTaggedClassFieldReference2() {
    doTestFieldReference("com.example.MyTestClass::testClass")
  }

  fun testFieldReferenceKotlin1() {
    doTestFieldReference("com.example.KTestClass::labelStyles")
  }

  fun testFieldReferenceKotlin2() {
    doTestFieldReference("com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle::background")
  }

  fun testFieldReferenceKotlin3() {
    doTestFieldReference("com.badlogic.gdx.graphics.Color::a")
  }

  fun testDrawableReference1() {
    doTestDrawableReference()
  }

  fun testDrawableReference2() {
    doTestDrawableReference()
  }

  fun testDrawableReference3() {
    doTestDrawableReference()
  }

  fun testDrawableReferenceWithTaggedClasses1() {
    doTestDrawableReference()
  }

  fun testDrawableReferenceWithTaggedClasses2() {
    doTestDrawableReference()
  }

  private fun doTestFieldReference(expectedFieldName: String? = null) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
    val sourceElement = PsiTreeUtil.findFirstParent(elementAtCaret) { it is SkinPropertyName } as? SkinPropertyName
    assertNotNull(sourceElement)
    val field = sourceElement?.reference?.resolve() as? PsiField
    assertNotNull(field)
    val expectedName = expectedFieldName ?: sourceElement?.property?.containingObject?.resolveToTypeString()!!.removeDollarFromClassName() + "::" + field?.name
    assertEquals(expectedName, field!!.containingClass?.qualifiedName + "::" + field.name)
  }

  private fun doTestFileReference(sourceElementClass: Class<*>, expectedFileName: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
    val sourceElement = PsiTreeUtil.findFirstParent(elementAtCaret) { sourceElementClass.isInstance(it) }
    assertNotNull(sourceElement)
    val file = sourceElement?.reference?.resolve() as? PsiFile
    assertNotNull(file)
    assertEquals(expectedFileName, file?.name)
  }

  private fun doTestResourceReference(resourceName: String?, resourceType: String?) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
    assertNotNull(element)
    val resource = element?.reference?.resolve() as? SkinResource
    if (resourceName != null) {
      assertNotNull(resource)
      assertEquals(resourceName, resource?.name)
      assertTrue(resource?.classSpecification?.getRealClassNamesAsString()?.contains(resourceType?.removeDollarFromClassName()) == true)
    } else {
      assertNull(resource)
    }
  }

  private fun doTestJavaClassReference(className: String) {
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element: SkinClassName? = myFixture.file.findElementAt(myFixture.caretOffset)?.parent?.parent as? SkinClassName
    assertNotNull(element)
    val clazz = (element?.reference as? SkinJavaClassReference)?.multiResolve(false)?.firstOrNull()?.element as? PsiClass
    assertNotNull(clazz)
    assertEquals(className, clazz?.qualifiedName)
  }

  private fun doTestDrawableReference() {
    myFixture.copyFileToProject(getTestName(true) + ".atlas")
    myFixture.configureByFile(getTestName(true) + ".skin")
    val element = PsiTreeUtil.findFirstParent(myFixture.file.findElementAt(myFixture.caretOffset)) { it is SkinStringLiteral } as SkinStringLiteral
    val reference = element.reference ?: throw AssertionError()
    val target = reference.resolve() as AtlasRegion
    assertEquals(element.value, target.name)
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addAnnotations()
    addDummyLibGDX199()

    myFixture.copyFileToProject("com/example/MyTestClass.java", "com/example/MyTestClass.java")

    val testName = getTestName(true)

    if (testName.contains("Kotlin", ignoreCase = true) || testName.contains("tagged", ignoreCase = true)) {
      addKotlin()
      myFixture.copyFileToProject("com/example/KTestClass.kt", "com/example/KTestClass.kt")
    }

  }

  override fun getBasePath() = "/filetypes/skin/references/"
}