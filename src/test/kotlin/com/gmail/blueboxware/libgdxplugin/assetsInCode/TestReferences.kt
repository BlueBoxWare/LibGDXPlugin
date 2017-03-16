package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinReference
import com.gmail.blueboxware.libgdxplugin.references.FileReference
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

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
class TestReferences : AssetsInCodeCodeInsightFixtureTestCase() {

  fun testJavaResourceReference() = doTest<SkinResource>(
          JavaFileType.INSTANCE,
          PsiLiteralExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin;

            class SkinTest {

                @GDXSkin(skinFiles = "src/assets/dir/holo.skin")
                static Skin staticSkin;

                void f() {
                    staticSkin.getDrawable("dialo<caret>gDim");
                }

            }
          """
  )

  fun testKotlinResourceReference() = doTest<SkinResource>(
          KotlinFileType.INSTANCE,
          KtStringTemplateExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin

            @GDXSkin(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets/dir/holo.skin"))
            val s: Skin = Skin()


            fun test() {
                s.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle::class.java)
            }
          """
  )

  fun testKotlinAndJavaResourceReference() = doTest<SkinResource>(
          KotlinFileType.INSTANCE,
          KtStringTemplateExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin

            fun f() {
              JavaSkinTest().skin.getColor("inv<caret>erse")
            }
          """
  )

  fun testJavaAndKotlinResourceReference() = doTest<SkinResource>(
          JavaFileType.INSTANCE,
          PsiLiteralExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin;

            class SkinTest {

                void f() {
                    KotlinSkinTestKt.skin.get("default<caret>-horizontal", "com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle::class.java");
                }

            }
          """
  )

  fun testKotlinSkinFileReferenceInAnnotation() = doTest<SkinFile>(
          KotlinFileType.INSTANCE,
          KtStringTemplateExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin

            @GDXSkin(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets\\dir/ho<caret>lo.skin"))
            val s: Skin = Skin()
          """
  )

  fun testJavaSkinFileReferenceInAnnotation() = doTest<SkinFile>(
          JavaFileType.INSTANCE,
          PsiLiteralExpression::class.java,
          """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin;

            class SkinTest {

                @GDXSkin(skinFiles = "src/assets\\dir/ho<caret>lo.skin")
                static Skin staticSkin;

            }
          """
  )

  inline fun <reified expectedReferentType: PsiElement>doTest(
          fileType: LanguageFileType,
          referencingElementType: Class<out PsiElement>,
          content: String
  ) {

    myFixture.configureByText(fileType, content)
    val referencingElement = myFixture.file.findElementAt(myFixture.caretOffset)?.let { elementAtCaret ->
      PsiTreeUtil.getParentOfType(elementAtCaret, referencingElementType)
    } ?: throw AssertionError()

    val referentElement = referencingElement.references.filter{ it is SkinReference<*> || it is FileReference }.firstOrNull()?.resolve() ?: throw AssertionError()

    assertTrue(referentElement is expectedReferentType)

    if (referentElement is SkinResource) {
      assertEquals(referentElement.name, StringUtil.stripQuotesAroundValue(referencingElement.text))
    }

  }

}