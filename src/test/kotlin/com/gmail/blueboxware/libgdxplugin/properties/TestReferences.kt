package com.gmail.blueboxware.libgdxplugin.properties

import com.gmail.blueboxware.libgdxplugin.filetypes.properties.GDXPropertyReference
import com.gmail.blueboxware.libgdxplugin.utils.innerText
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.properties.psi.Property
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.plainContent


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
class TestReferences: PropertiesCodeInsightFixtureTestCase() {

  val kotlinTests = listOf(
          """
            I18NBundle().get("<caret>noTranslation");
          """ to true,
          """
            I18NBundle().format("<caret>noTranslation", "germanTranslation");
          """ to true,
          """
            I18NBundle().format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            I18NBundle().get("<caret>french.Only");
          """ to true,

          """
            i18NBundle.get("<caret>noTranslation");
          """ to true,
          """
            i18NBundle.format("<caret>noTranslation", "germanTranslation");
          """ to true,
          """
            i18NBundle.format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            i18NBundle.get("<caret>french.Only");
          """ to true,

          """
            i18NBundle2.get("<caret>noTranslation");
          """ to false,
          """
            i18NBundle2.format("<caret>noTranslation", "germanTranslation");
          """ to false,
          """
            i18NBundle2.format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            i18NBundle2.get("<caret>french.Only");
          """ to false
  )

  val javaTests = listOf(
          """
            new I18NBundle().get("<caret>noTranslation");
          """ to true,
          """
            new I18NBundle().format("<caret>noTranslation", "germanTranslation");
          """ to true,
          """
            new I18NBundle().format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            new I18NBundle().get("<caret>french.Only");
          """ to true,

          """
            i18NBundle.get("<caret>noTranslation");
          """ to true,
          """
            i18NBundle.format("<caret>noTranslation", "germanTranslation");
          """ to true,
          """
            i18NBundle.format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            i18NBundle.get("<caret>french.Only");
          """ to true,

          """
            i18NBundle2.get("<caret>noTranslation");
          """ to false,
          """
            i18NBundle2.format("<caret>noTranslation", "germanTranslation");
          """ to false,
          """
            i18NBundle2.format("noTranslation", "<caret>noTranslation");
          """ to false,
          """
            i18NBundle2.get("<caret>french.Only");
          """ to false
  )

  fun testKotlinPropertiesReferences() {
    addKotlin()
    for ((test, shouldBeFound) in kotlinTests) {
      val content = """
        import com.badlogic.gdx.utils.I18NBundle
        import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

          @GDXAssets(propertiesFiles = "src/messages.properties")
          val i18NBundle = I18NBundle()
          @GDXAssets(propertiesFiles = "src/doesnotexist.properties")
          val i18NBundle2 = I18NBundle()

        fun f() {
          $test
        }
        """
      doTest(KotlinFileType.INSTANCE, content, shouldBeFound)
    }
  }

  fun testJavaPropertiesReferences() {
    for ((test, shouldBeFound) in javaTests) {
      val content = """
        import com.badlogic.gdx.utils.I18NBundle;
        import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

        class Test {

          @GDXAssets(propertiesFiles = "src/messages.properties")
          I18NBundle i18NBundle;
          @GDXAssets(propertiesFiles = "src/doesnotexist.properties")
          I18NBundle i18NBundle2;

          void m() {
            $test
          }
        }
      """
      doTest(JavaFileType.INSTANCE, content, shouldBeFound)
    }
  }

  fun doTest(fileType: LanguageFileType, content: String, shouldBeFound: Boolean = true) {

    myFixture.configureByText(fileType, content)

    val referencingElement = myFixture.file.findElementAt(myFixture.caretOffset)?.let { elementAtCaret ->
      PsiTreeUtil.findFirstParent(elementAtCaret) { it is KtStringTemplateExpression || it is PsiLiteralExpression }
    } ?: throw AssertionError("Referencing element not found")

    var found = false
    referencingElement.references.forEach { reference ->
      if (reference is GDXPropertyReference) {
        reference.multiResolve(true).forEach { resolveResult ->
          assertTrue(resolveResult.element is Property)
          val text =
                  if (referencingElement is PsiLiteralExpression)
                    referencingElement.innerText()
                  else if (referencingElement is KtStringTemplateExpression)
                    referencingElement.plainContent
                  else throw AssertionError()
          assertEquals((resolveResult.element as? Property)?.name, text)
          found = true
        }
      }
    }

    assertEquals(content, shouldBeFound, found)

  }

}