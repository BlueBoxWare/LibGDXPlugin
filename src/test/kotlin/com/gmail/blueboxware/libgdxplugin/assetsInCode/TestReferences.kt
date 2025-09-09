package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.gmail.blueboxware.libgdxplugin.references.FileReference
import com.gmail.blueboxware.libgdxplugin.testname
import com.gmail.blueboxware.libgdxplugin.utils.BITMAPFONT_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl
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
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                @GDXAssets(skinFiles = "src/assets/dir/holo.skin")
                static Skin staticSkin;

                void f() {
                    staticSkin.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle.class);
                }

            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle"
    )

    fun testJavaResourceReferenceWithoutAnnotation() = doTest<SkinResource>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                static Skin staticSkin;

                void f() {
                    staticSkin.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle.class);
                }

            }
          """,
        shouldBeFound = false
    )

    fun testJavaResourceReferenceWithTag1() = doTest<SkinResource>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                @GDXAssets(skinFiles = "src/assets/libgdx.skin")
                static Skin staticSkin;

                void f() {
                    staticSkin.get("taggedS<caret>tyle1", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle"
    )

    fun testJavaResourceReferenceWithTag2() = doTest<SkinResource>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                @GDXAssets(skinFiles = "src/assets/libgdx.skin")
                static Skin staticSkin;

                void f() {
                    staticSkin.get("tagged<caret>Style2", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle"
    )

    fun testKotlinResourceReferenceWithoutAnnotation() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            val s: Skin = Skin()


            fun test() {
                s.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle::class.java)
            }
          """,
        shouldBeFound = false
    )

    fun testKotlinResourceReference() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets/dir/holo.skin"))
            val s: Skin = Skin()


            fun test() {
                s.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle::class.java)
            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle"
    )

    fun testKotlinResourceReference2() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(atlasFiles = [""], skinFiles = ["src/assets/dir/holo.skin"])
            val s: Skin = Skin()


            fun test() {
                s.get("sw<caret>itch", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle::class.java)
            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle"
    )

    fun testKotlinResourceReferenceWithTag() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(atlasFiles = [""], skinFiles = ["src/assets/libgdx.skin"])
            val s: Skin = Skin()

            fun test() {
                s.get("tagged<caret>Style2", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::class.java)
            }
          """,
        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle"
    )

    fun testKotlinAndJavaResourceReference() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            fun f() {
              JavaSkinTest().skin.getColor("inv<caret>erse")
            }
          """,
        expectedType = COLOR_CLASS_NAME
    )

    fun testKotlinAndJavaResourceReferenceWithTags() = doTest<SkinResource>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            fun f() {
              JavaSkinTest().skin.getColor("tagged<caret>Color3")
            }
          """,
        expectedType = COLOR_CLASS_NAME
    )

// TODO
//    fun testJavaAndKotlinResourceReference() = doTest<SkinResource>(
//        JavaFileType.INSTANCE,
//        PsiLiteralExpression::class.java,
//        """
//            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;
//
//            class SkinTest {
//
//                void f() {
//                    KotlinSkinTestKt.skin.get("default<caret>-horizontal", com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle.class);
//                }
//
//            }
//          """,
//        expectedType = "com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle"
//    )

    fun testJavaAndKotlinResourceReferenceWithTags() = doTest<SkinResource>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                void f() {
                    new KotlinSkinTest().getSkin().get("mediu<caret>m", com.badlogic.gdx.graphics.g2d.BitmapFont.class);
                }

            }
          """,
        expectedType = BITMAPFONT_CLASS_NAME
    )

    fun testKotlinSkinFileReferenceInAnnotation() = doTest<SkinFile>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets\\dir/ho<caret>lo.skin"))
            val s: Skin = Skin()
          """
    )

    fun testKotlinSkinFileReferenceInAnnotation2() = doTest<SkinFile>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(atlasFiles = arrayOf(""), skinFiles = ["src/assets\\dir/ho<caret>lo.skin"])
            val s: Skin = Skin()
          """
    )

    fun testJavaSkinFileReferenceInAnnotation() = doTest<SkinFile>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                @GDXAssets(skinFiles = "src/assets\\dir/ho<caret>lo.skin")
                static Skin staticSkin;

            }
          """
    )

    fun testKotlinPropertiesFileReferenceInAnnotation() = doTest<PropertiesFileImpl>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(propertiesFiles = arrayOf("src/assets\\test<caret>.properties"))
            val s: Skin = Skin()
          """
    )

    fun testKotlinPropertiesFileReferenceInAnnotation2() = doTest<PropertiesFileImpl>(
        KotlinFileType.INSTANCE,
        KtStringTemplateExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

            @GDXAssets(propertiesFiles = ["src/assets\\test<caret>.properties"])
            val s: Skin = Skin()
          """
    )

    fun testJavaPropertiesFileReferenceInAnnotation() = doTest<PropertiesFileImpl>(
        JavaFileType.INSTANCE,
        PsiLiteralExpression::class.java,
        """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

            class SkinTest {

                @GDXAssets(propertiesFiles = "src/assets\\test.<caret>properties")
                static Skin staticSkin;

            }
          """
    )

    private inline fun <reified expectedReferentType : PsiElement> doTest(
        fileType: LanguageFileType,
        referencingElementType: Class<out PsiElement>,
        content: String,
        expectedType: String? = null,
        shouldBeFound: Boolean = true
    ) {

        configureByText(fileType, content)
        val referencingElement = file.findElementAt(myFixture.caretOffset)?.let { elementAtCaret ->
            PsiTreeUtil.getParentOfType(elementAtCaret, referencingElementType)
        } ?: throw AssertionError("Referencing element not found")

        val referentElement =
            referencingElement.references.firstOrNull { it is AssetReference || it is FileReference }?.let {
                if (!shouldBeFound) {
                    throw AssertionError("Unexpected reference found")
                }
                it.resolve()
            }

        if (!shouldBeFound && referentElement == null) {
            return
        }

        assertTrue(referentElement is expectedReferentType)

        if (referentElement is SkinResource) {
            assertEquals(referentElement.name, StringUtil.stripQuotesAroundValue(referencingElement.text))
            assertEquals(expectedType, referentElement.classSpecification?.resolveClass()?.qualifiedName)
        }

    }

    override fun setUp() {
        super.setUp()

        if (testname().contains("tag", ignoreCase = true)) {
            addLibGDX113()
            copyFileToProject("src/JavaSkinTest.java")
            copyFileToProject("src/KotlinSkinTest.kt")
        }

    }
}
