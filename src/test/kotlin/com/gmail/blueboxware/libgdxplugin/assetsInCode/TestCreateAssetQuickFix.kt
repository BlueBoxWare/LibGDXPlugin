package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.inspections.java.JavaNonExistingAssetInspection
import com.gmail.blueboxware.libgdxplugin.inspections.kotlin.KotlinNonExistingAssetInspection
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.psi.codeStyle.CodeStyleSettingsManager


/*
 * Copyright 2018 Blue Box Ware
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
class TestCreateAssetQuickFix: LibGDXCodeInsightFixtureTestCase() {

  fun testCreateColor1() = doJavaTest(
          "",
          """
            skin.getColor("<caret>yellow");
          """,
          """
            {
              com.badlogic.gdx.graphics.Color: {
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateColor2() = doJavaTest(
          "{}",
          """
            skin.getColor("<caret>yellow");
          """,
          """
            {
              com.badlogic.gdx.graphics.Color: {
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateColor3() = doJavaTest(
          """
             {
              com.badlogic.gdx.graphics.Color: {
                red: {
                  hex: "#123456"
                }
              }
            }
          """.trimIndent(),
          """
            skin.getColor("<caret>yellow");
          """,
          """
           {
            com.badlogic.gdx.graphics.Color: {
              red: {
                hex: "#123456"
              }
              yellow: {
                hex: "#<caret>"
              }
            }
          }
          """.trimIndent()
  )

  fun testCreateColor4() = doJavaTest(
          """
            // Comments

            /*
             * Comments
            */
            {
              // Comments

              /*
               * Comments
              */
            }
          """.trimIndent(),
          """
            skin.getColor("<caret>yellow");
          """,
          """
            // Comments

            /*
             * Comments
            */
            {
              // Comments

              /*
               * Comments
              */
              com.badlogic.gdx.graphics.Color: {
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateColor5() = doJavaTest(
          """
           // Comments
           {
            // Comments
            com.badlogic.gdx.graphics.Color: {
              // Comments
              red: {
                hex: "#123456"
              }
              /*
               * Comments
               */
            }
           }
          """.trimIndent(),
          """
            skin.getColor("<caret>yellow");
          """,
          """
            // Comments
            {
             // Comments
             com.badlogic.gdx.graphics.Color: {
               // Comments
               red: {
                 hex: "#123456"
               }
               yellow: {
                 hex: "#"
               }

               /*
                * Comments
                */
             }
            }
          """.trimIndent()
  )

  fun testCreateColor991() = doJavaTest(
          "",
          """
            skin.getColor("<caret>yellow");
          """,
          """
            {
              Color: {
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateColor992() = doJavaTest(
          """
             {
              Color: {
                red: {
                  hex: "#123456"
                }
              }
            }
          """.trimIndent(),
          """
            skin.getColor("<caret>yellow");
          """,
          """
           {
            Color: {
              red: {
                hex: "#123456"
              }
              yellow: {
                hex: "#<caret>"
              }
            }
          }
          """.trimIndent()
  )

  fun testCreateColor993() = doJavaTest(
          """
            {
              Color: {
                red: {
                  hex: "#123456"
                }
              }
              com.badlogic.gdx.graphics.Color: {
                blue: {}
              }
            }
          """.trimIndent(),
          """
            skin.getColor("<caret>yellow");
          """,
          """
            {
              Color: {
                red: {
                  hex: "#123456"
                }
              }
              com.badlogic.gdx.graphics.Color: {
                blue: {}
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateColorKotlin1() = doKotlinTest(
          "",
          """
            skin.getColor("<caret>yellow")
          """,
          """
            {
              com.badlogic.gdx.graphics.Color: {
                yellow: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateTintedDrawable1() = doJavaTest(
          "",
          """
            skin.getDrawable("tinted<caret>");
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                tinted: {
                  color: { hex: "#ffffff" }
                  name: <caret>
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateTintedDrawable2() {
    @Suppress("DEPRECATION")
    // COMPAT: CodeStyle#getCustomSettings() introduced in 181
    CodeStyleSettingsManager.getSettings(myFixture.project).getCustomSettings(SkinCodeStyleSettings::class.java).SPACE_AFTER_COLON = false
    doJavaTest(
            "",
            """
            skin.getDrawable("tinted<caret>");
            """.trimIndent(),
            """
              {
                com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable:{
                  tinted:{
                    color:{ hex:"#ffffff" }
                    name:<caret>
                  }
                }
              }
            """.trimIndent()
    )
  }

  fun testCreateTintedDrawableKotlin() = doKotlinTest(
          "",
          """
            skin.newDrawable("tinted<caret>")
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                tinted: {
                  color: { hex: "#ffffff" }
                  name: <caret>
                }
              }
            }
          """.trimIndent()
  )

  fun testCreateResource1() = doJavaTest(
          "",
          """
            skin.get("myButto<caret>nStyle", TextButton.TextButtonStyle.class);
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                myButtonStyle: { <caret>  }
              }
            }
          """.trimIndent()
  )

  fun testCreateResource991() = doJavaTest(
          """
            {
              TextButtonStyle {
                f: {}
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle {
                g: {}
              }
            }
          """.trimIndent(),
          """
            skin.get("myButto<caret>nStyle", TextButton.TextButtonStyle.class);
          """.trimIndent(),
          """
            {
              TextButtonStyle {
                f: {}
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle {
                g: {}
                myButtonStyle: { <caret>  }
              }
            }
          """.trimIndent()
  )

  fun testCreateResource992() = doJavaTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle {
                f: {}
              }
              TextButtonStyle {
                g: {}
              }
            }
          """.trimIndent(),
          """
            skin.get("myButto<caret>nStyle", TextButton.TextButtonStyle.class);
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle {
                f: {}
              }
              TextButtonStyle {
                g: {}
                myButtonStyle: { <caret>  }
              }
            }
          """.trimIndent()
  )

  private val javaContent = """
      import com.badlogic.gdx.scenes.scene2d.ui.Button;
      import com.badlogic.gdx.scenes.scene2d.ui.Skin;
      import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
      import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

      public class Test {

          @GDXAssets(skinFiles = {"src/skin.skin", "src/atlas.atlas"})
          Skin skin = new Skin();

          void m() {
            <content>
          }
      }
  """.trimIndent()

  private val kotlinContent = """
    import com.badlogic.gdx.scenes.scene2d.ui.Button
    import com.badlogic.gdx.scenes.scene2d.ui.Skin
    import com.badlogic.gdx.scenes.scene2d.ui.TextButton
    import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

    @GDXAssets(skinFiles = arrayOf("src/skin.skin", "src/atlas.atlas"))
    val skin = Skin()

    fun f() {
      <content>
    }
  """.trimIndent()

  private fun doJavaTest(skinFileContent: String, codeFileContent: String, expectedSkinContent: String) {
    myFixture.enableInspections(JavaNonExistingAssetInspection())
    doTest(skinFileContent, javaContent.replace("<content>", codeFileContent), ".java", expectedSkinContent)
  }

  private fun doKotlinTest(skinFileContent: String, codeFileContent: String, expectedSkinContent: String) {
    addKotlin()
    myFixture.enableInspections(KotlinNonExistingAssetInspection())
    doTest(skinFileContent, kotlinContent.replace("<content>", codeFileContent), ".kt", expectedSkinContent)
  }

  fun doTest(
          skinFileContent: String,
          codeFileContent: String,
          extension: String,
          expectedSkinContent: String

  ) {

    val skinFile = myFixture.configureByText("skin.skin", skinFileContent)
    myFixture.configureByText("Test.$extension", codeFileContent)

    for (intention in myFixture.availableIntentions) {
      if (intention.familyName.startsWith("Create resource")) {
        myFixture.launchAction(intention)
        myFixture.openFileInEditor(skinFile.virtualFile)
        myFixture.checkResult(expectedSkinContent, true)
        return
      }
    }

    throw AssertionError("Intention not found")

  }

  override fun setUp() {
    super.setUp()

    addAnnotations()
    addLibGDX()

    if (testname().contains("99")) {
      addDummyLibGDX199()
    } else {
      removeDummyLibGDX199()
    }

  }

}