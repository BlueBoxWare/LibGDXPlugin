package com.gmail.blueboxware.libgdxplugin.assetsInCode

import com.intellij.codeInsight.completion.CompletionType

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
class TestCompletion : AssetsInCodeCodeInsightFixtureTestCase() {

  val javaResourceCompletionTests = listOf(
          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            class Test {

                @GDXSkin(skinFiles = "src/assets/libgdx.skin")
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin", "src/assets/dir/skin.json"})
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf("red", "white", "yellow", "c1", "c2", "c3") to listOf("medium", "default", "toggle", "green", "round-green")),

          """
            class Test {

              void test() {
                KotlinSkinTestKt.getSkin().getColor("<caret>");
              }

            }
          """ to (listOf("inverse", "ui", "default", "grey", "black") to listOf("c1", "c2", "dialogDim")),

          """
            class Test {

              void test() {
                new KotlinSkinTest().getSkin().getColor("<caret>");
              }

            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            class Test {

              void test() {
                KotlinSkinTest.Companion.getSkin().getColor("<caret>");
              }

            }
          """ to (listOf("red", "white", "c1", "c2") to listOf("medium", "default", "blue", "round-green")),

          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>");
                }

            }
          """ to (listOf("red", "white", "yellow", "default", "medium", "green", "toggle") to listOf()),

          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", );
                }

            }
          """ to (listOf("red", "white", "yellow", "default", "medium", "green", "toggle") to listOf()),

          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", Color.class);
                }

            }
          """ to (listOf("red", "white", "yellow") to listOf("default", "medium", "green", "toggle")),

          """
            class Test {

                @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow")),

          """
            class Test {

                void test() {

                    @GDXSkin(skinFiles = {"src/assets/libgdx.skin"})
                    Skin skin1;

                    skin1.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow"))
  )

  val kotlinResourceCompletionTests = listOf(

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/skin.json"))
            val s: Skin = Skin()

            fun f() {
                s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow", "c1", "c2", "c3") to listOf("medium", "default", "toggle", "green", "round-green")),

          """
            class Test {
              @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
              val s: Skin = Skin()
            }

            fun f() {
                Test().s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            class Test {
              companion object {
                @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s: Skin = Skin()
              }
            }

            fun f() {
                Test.s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

          """
            fun f() {
              JavaSkinTest().skin.getColor("<caret>")
            }
          """  to (listOf("inverse", "ui", "default", "grey", "black") to listOf("c1", "c2", "dialogDim")),

          """
            fun f() {
              JavaSkinTest.staticSkin.getColor("<caret>")
            }
          """  to (listOf("c1", "c2") to listOf("grey", "black")),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>")
            }
          """ to (listOf("red", "white", "yellow", "toggle", "default", "green", "medium") to listOf()),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", )
            }
          """ to (listOf("red", "white", "yellow", "toggle", "default", "green", "medium") to listOf()),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", Color::class.java)
            }
          """ to (listOf("red", "white", "yellow") to listOf("toggle", "default", "green", "medium")),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::class.java)
            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow")),

          """
            fun f() {
                @GDXSkin(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s: Skin = Skin()

                s.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::class.java)
            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow"))

  )

  val javaAssetFileNameCompletionTests = listOf(
          """
            class Test {

                @GDXSkin(skinFiles = "<caret>", atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf("src/assets/libgdx.skin", "src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something")),

          """
            class Test {

                @GDXSkin(skinFiles = "src/assets/d<caret>", atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf("src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something", "src/assets/libgdx.skin", "src/assets/dir/test.pack")),

          """
            class Test {

                @GDXSkin(skinFiles = {"something", "<caret>"}, atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf("src/assets/libgdx.skin", "src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something", "src/assets/dir/test.pack")),

          """
            class Test {

                @GDXSkin(skinFiles = "", atlasFiles = {"<caret>"})
                Skin skn1;

            }
          """ to (listOf("src/assets/dir/test.pack") to listOf("src/assets/dir/something", "src/assets/libgdx.skin"))
  )

  val kotlinAssetFileNameCompletionTests = listOf(
          """
            @GDXSkin(skinFiles = arrayOf("<caret>"))
            val s: Skin = Skin()
          """ to (listOf("src/assets/libgdx.skin", "src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something")),

          """
            @GDXSkin(skinFiles = arrayOf("src/assets/d<caret>"))
            val s: Skin = Skin()
          """ to (listOf("src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something", "src/assets/libgdx.skin", "src/assets/dir/test.pack")),

          """
            @GDXSkin(skinFiles = arrayOf("test", "<caret>"))
            val s: Skin = Skin()
          """ to (listOf("src/assets/libgdx.skin", "src/assets/dir/holo.json", "src/assets/dir/skin.json") to listOf("src/assets/dir/something")),

          """
            @GDXSkin(skinFiles = arrayOf("test"), atlasFiles = arrayOf("", "<caret>"))
            val s: Skin = Skin()
          """ to (listOf("src/assets/dir/test.pack") to listOf("src/assets/dir/something", "src/assets/libgdx.skin"))
  )

  fun testJavaAssetFileNameCompletion() {
    doJavaTest(javaAssetFileNameCompletionTests)
  }

  fun testKotlinAssetFileNameCompletion() {
    doKotlinTest(kotlinAssetFileNameCompletionTests)
  }

  fun testJavaResourceCompletion() {
    doJavaTest(javaResourceCompletionTests)
  }

  fun testKotlinResourceCompletion() {
    doKotlinTest(kotlinResourceCompletionTests)
  }

  fun doJavaTest(tests: List<Pair<String, Pair<List<String>, List<String>>>>) {
    for ((content, expected) in tests) {
      val source = """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin;
            import com.badlogic.gdx.graphics.Color;

            $content
      """

      doTest("Test.java", source, expected.first, expected.second)
    }
  }

  fun doKotlinTest(tests: List<Pair<String, Pair<List<String>, List<String>>>>) {
    for ((content, expected) in tests) {
      val source = """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin
            import com.badlogic.gdx.graphics.Color

            $content
      """

      doTest("Test.kt", source, expected.first, expected.second)
    }
  }

  fun doTest(testFileName: String, content: String, expectedCompletionStrings: List<String>, notExpectedCompletionStrings: List<String>) {
    myFixture.configureByText(testFileName, content)
    val result = myFixture.complete(CompletionType.BASIC, 1)
    if (result == null) {
      // the only item was auto-completed?
      assertEquals(expectedCompletionStrings.size, 1)
      val text = myFixture.editor.document.text
      val expectedString = expectedCompletionStrings.first()
      val msg = "\nExpected string '$expectedString' not found. Content: '$content'"
      assertTrue(msg, text.contains(expectedString))
    } else {
      val strings = myFixture.lookupElementStrings
      assertNotNull(strings)
      strings?.let { results ->
        val msg = "\nExpected results: $expectedCompletionStrings, \nFound: $strings, \nContent: '$content'"
        assertTrue(msg, results.containsAll(expectedCompletionStrings))
        for (notExpectedCompletionString in notExpectedCompletionStrings) {
          val msg2 = "Not expected to find '$notExpectedCompletionString'. Content: '$content'"
          assertFalse(msg2, strings.contains(notExpectedCompletionString))
        }
      }
    }
  }

}