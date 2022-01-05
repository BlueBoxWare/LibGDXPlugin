package com.gmail.blueboxware.libgdxplugin.assetsInCode

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

    private val javaResourceCompletionTests = listOf(
        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1",
            "taggedColor2"
        ) to listOf(
            "c1",
            "medium",
            "default",
            "toggle",
            "green"
        )),

        """
            class Test {

                @GDXAssets(skinFiles = "src/assets/libgdx.skin")
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin", "src/assets/dir/skin.json"})
                public Skin skin1;

                void test() {
                    skin1.getColor("<caret>");
                }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "c1",
            "c2",
            "c3",
            "taggedColor1"
        ) to listOf(
            "medium",
            "default",
            "toggle",
            "green",
            "round-green"
        )),

        """
            class Test {

              void test() {
                KotlinSkinTestKt.getSkin().getColor("<caret>");
              }

            }
          """ to (listOf(
            "inverse",
            "ui",
            "default",
            "grey",
            "black",
            "taggedColor3"
        ) to listOf("c1", "c2", "dialogDim")),

        """
            class Test {

              void test() {
                new KotlinSkinTest().getSkin().getColor("<caret>");
              }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1"
        ) to listOf(
            "c1",
            "medium",
            "default",
            "toggle",
            "green"
        )),

        """
            class Test {

              void test() {
                KotlinSkinTest.Companion.getSkin().getColor("<caret>");
              }

            }
          """ to (listOf(
            "red",
            "white",
            "c1",
            "c2"
        ) to listOf(
            "medium",
            "default",
            "blue",
            "round-green"
        )),

        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>");
                }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "default",
            "medium",
            "green",
            "toggle"
        ) to listOf()),

        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", );
                }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "default",
            "medium",
            "green",
            "toggle"
        ) to listOf()),

        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", Color.class);
                }

            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1",
            "taggedColor2"
        ) to listOf("default", "medium", "green", "toggle")),

        """
            class Test {

                @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                public Skin skin1;

                void test() {
                    skin1.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """ to (listOf(
            "toggle",
            "default",
            "green",
            "taggedStyle1",
            "taggedStyle1"
        ) to listOf("medium", "red", "white", "yellow")),

        """
            class Test {

                void test() {

                    @GDXAssets(skinFiles = {"src/assets/libgdx.skin"})
                    Skin skin1;

                    skin1.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
                }

            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow")),

        """
            class Test {

                void test() {

                    @GDXAssets(skinFiles = {"src/assets/libgdx.skin", "src/assets/dir/holo.skin"})
                    Skin skin1;

                    skin1.getDrawable("<caret>");
                }

            }
          """ to (listOf(
            "user-red",
            "lightGrey",
            "button back",
            "button-back-disabled",
            "select-box-over",
            "taggedTinted1",
            "taggedTinted2"
        ) to listOf("medium", "red", "white", "yellow")),

        """
            class Test {

                void test() {

                    @GDXAssets(atlasFiles = {"src/assets/libgdx.atlas", "src/assets/dir/holo.atlas"})
                    Skin skin1;

                    skin1.getDrawable("<caret>");
                }

            }
          """ to (listOf(
            "button back",
            "button-back-disabled",
            "select-box-over"
        ) to listOf(
            "medium",
            "red",
            "white",
            "yellow",
            "user-red",
            "lightGrey"
        )),

        """
            import com.badlogic.gdx.graphics.g2d.TextureRegion;

            class Test {

                void test() {

                    @GDXAssets(atlasFiles = {"src/assets/libgdx.atlas", "src/assets/dir/holo.atlas"})
                    Skin skin1;

                    skin1.get("<caret>", TextureRegion.class);
                }

            }
          """ to (listOf(
            "slider-knob",
            "slider",
            "logo small",
            "progress-bar-horizontal-knob"
        ) to listOf("medium", "red", "white", "yellow", "user-red", "lightGrey")),

        """
            import com.badlogic.gdx.graphics.g2d.TextureRegion;

            class Test {

                void test() {

                    @GDXAssets(skinFiles = "src/assets/libgdx.skin")
                    Skin skin1;

                    skin1.get("<caret>", TextureRegion.class);
                }

            }
          """ to (listOf(
            "slider-knob",
            "slider",
            "scroll-background-v"
        ) to listOf(
            "medium",
            "red",
            "white",
            "yellow",
            "user-red",
            "lightGrey",
            "user-red"
        )),

        """
            import com.badlogic.gdx.graphics.g2d.TextureAtlas;

            class C {
              @GDXAssets(atlasFiles = {"src/assets/dir/test.pack"})
              TextureAtlas atlas;
            }

            class Test {

                void test() {
                    new C().atlas.createSprite("<caret>");
                }

            }
          """ to (listOf("abstractClass", "aspect", "compiledClassesFolder") to listOf()),

        """
            import com.badlogic.gdx.graphics.g2d.TextureAtlas;

            class C {
              @GDXAssets(atlasFiles = {"src/assets/dir/test.pack", "src/assets/libgdx.atlas"})
              static TextureAtlas atlas;
            }

            class Test {

                void test() {
                    C.atlas.findRegion("<caret>", 3);
                }

            }
          """ to (listOf(
            "abstractClass",
            "aspect",
            "compiledClassesFolder",
            "scroll-corner",
            "tooltip",
            "button back"
        ) to listOf())
    )

    private val kotlinResourceCompletionTests = listOf(

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1",
            "taggedColor2"
        ) to listOf("c1", "medium", "default", "toggle", "green")),

        """
            @GDXAssets(skinFiles = ["src/assets/libgdx.skin"])
            val s: Skin = Skin()

            fun f() {
            s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/skin.json"))
            val s: Skin = Skin()

            fun f() {
                s.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "c1",
            "c2",
            "c3",
            "taggedColor1",
            "taggedColor2"
        ) to listOf("medium", "default", "toggle", "green", "round-green")),

        """
            @GDXAssets(skinFiles = ["src/assets/libgdx.skin", "src/assets/dir/skin.json"])
            val s: Skin = Skin()

            fun f() {
                s.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "c1",
            "c2",
            "c3"
        ) to listOf(
            "medium",
            "default",
            "toggle",
            "green",
            "round-green"
        )),

        """
            class Test {
              @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
              val s: Skin = Skin()
            }

            fun f() {
                Test().s.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            class Test {
              companion object {
                @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
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
          """ to (listOf("inverse", "ui", "default", "grey", "black") to listOf("c1", "c2", "dialogDim")),

        """
            fun f() {
              JavaSkinTest.staticSkin.getColor("<caret>")
            }
          """ to (listOf("c1", "c2") to listOf("grey", "black")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "toggle",
            "default",
            "green",
            "medium",
            "taggedColor1",
            "taggedColor2"
        ) to listOf()),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", )
            }
          """ to (listOf("red", "white", "yellow", "toggle", "default", "green", "medium") to listOf()),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", Color::class.java)
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1",
            "taggedColor2"
        ) to listOf("toggle", "default", "green", "medium")),

        """
            @GDXAssets(skinFiles = ["src/assets/libgdx.skin"])
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", Color::class.java)
            }
          """ to (listOf("red", "white", "yellow") to listOf("toggle", "default", "green", "medium")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::class.java)
            }
          """ to (listOf(
            "toggle",
            "default",
            "green",
            "taggedStyle2",
            "taggedStyle2"
        ) to listOf("medium", "red", "white", "yellow")),

        """
            fun f() {
                @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s: Skin = Skin()

                s.get("<caret>", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle::class.java)
            }
          """ to (listOf("toggle", "default", "green") to listOf("medium", "red", "white", "yellow")),

        """
            fun f() {
                @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s: Skin = Skin()

                s.getFont("<caret>")
            }
          """ to (listOf("medium") to listOf("red", "white", "yellow", "toggle", "default", "green")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/holo.json"))
            val s: Skin = Skin()

            fun f() {
                s.getDrawable("<caret>")
            }
          """ to (listOf(
            "user-red",
            "lightGrey",
            "button back",
            "button-back-disabled",
            "select-box-over",
            "taggedTinted1",
            "taggedTinted2"
        ) to listOf("medium", "red", "white", "yellow")),

        """
            @GDXAssets(atlasFiles = arrayOf("src/assets/libgdx.atlas", "src/assets/dir/holo.atlas"))
            val s: Skin = Skin()

            fun f() {
                s.getDrawable("<caret>")
            }
          """ to (listOf(
            "button back",
            "button-back-disabled",
            "select-box-over"
        ) to listOf(
            "medium",
            "red",
            "white",
            "yellow",
            "user-red",
            "lightGrey"
        )),

        """
            @GDXAssets(atlasFiles = ["src/assets/libgdx.atlas", "src/assets/dir/holo.atlas"])
            val s: Skin = Skin()

            fun f() {
                s.getDrawable("<caret>")
            }
          """ to (listOf(
            "button back",
            "button-back-disabled",
            "select-box-over"
        ) to listOf(
            "medium",
            "red",
            "white",
            "yellow",
            "user-red",
            "lightGrey"
        )),


        """
            import com.badlogic.gdx.scenes.scene2d.utils.Drawable

            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/holo.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", Drawable::class.java)
            }
          """ to (listOf(
            "user-red",
            "lightGrey",
            "scroll-background-v",
            "logo small",
            "taggedTinted1",
            "taggedTinted2"
        ) to listOf()),

        """
            import com.badlogic.gdx.graphics.g2d.Sprite


            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/holo.skin"))
            val s: Skin = Skin()

            fun f() {
                s.get("<caret>", Sprite::class.java)
            }
          """ to (listOf(
            "scroll-background-v",
            "logo small"
        ) to listOf(
            "user-red",
            "lightGrey",
            "taggedTinted1",
            "taggedTinted2"
        )),

        """
            import com.badlogic.gdx.graphics.g2d.TextureAtlas

            object O {
              @GDXAssets(atlasFiles = arrayOf("src/assets/dir/test.pack"))
              val atlas = TextureAtlas()
            }

            fun f() {
              O.atlas.createPatch("<caret>")
            }
          """ to (listOf("abstractClass", "aspect", "compiledClassesFolder") to listOf()),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
            val s: Skin? = Skin()

            fun f() {
                s?.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "taggedColor1"
        ) to listOf("c1", "medium", "default", "toggle", "green")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin", "src/assets/dir/skin.json"))
            val s: Skin = Skin()

            fun f() {
                s?.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "c1",
            "c2",
            "c3"
        ) to listOf("medium", "default", "toggle", "green", "round-green")),

        """
            @GDXAssets(skinFiles = ["src/assets/libgdx.skin", "src/assets/dir/skin.json"])
            val s: Skin = Skin()

            fun f() {
                s?.getColor("<caret>")
            }
          """ to (listOf(
            "red",
            "white",
            "yellow",
            "c1",
            "c2",
            "c3"
        ) to listOf("medium", "default", "toggle", "green", "round-green")),

        """
            class Test {
              @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
              val s: Skin? = Skin()
            }

            fun f() {
                Test()?.s?.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            class Test {
              companion object {
                @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s: Skin? = Skin()
              }
            }

            fun f() {
                Test.s?.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            fun f() {
              JavaSkinTest()?.skin?.getColor("<caret>")
            }
          """ to (listOf("inverse", "ui", "default", "grey", "black") to listOf("c1", "c2", "dialogDim")),

        """
            class Test {
              companion object {
                @GDXAssets(skinFiles = arrayOf("src/assets/libgdx.skin"))
                val s = Skin()
              }
            }

            fun f() {
                Test.s?.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            class Test {
              companion object {
                @GDXAssets(skinFiles = ["src/assets/libgdx.skin"])
                val s = Skin()
              }
            }

            fun f() {
                Test.s?.getColor("<caret>")
            }
          """ to (listOf("red", "white", "yellow") to listOf("c1", "medium", "default", "toggle", "green")),

        """
            import com.badlogic.gdx.graphics.g2d.TextureAtlas

            object O {
              @GDXAssets(atlasFiles = arrayOf("src/assets/dir/test.pack"))
              val atlas: TextureAtlas? = TextureAtlas()
            }

            fun f() {
              O?.atlas?.createPatch("<caret>")
            }
          """ to (listOf("abstractClass", "aspect", "compiledClassesFolder") to listOf())

    )

    private val javaAssetFileNameCompletionTests = listOf(
        """
            class Test {

                @GDXAssets(skinFiles = "<caret>", atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf(
            "src/assets/libgdx.skin",
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf("src/assets/dir/something")),

        """
            class Test {

                @GDXAssets(propertiesFiles = "<caret>", atlasFiles = "")
                I18NBundle bundle;

            }
          """ to (listOf(
            "src/assets/test.properties",
            "src/assets/dir/foo.properties"
        ) to listOf(
            "src/assets/test_en.properties",
            "src/assets/test_en_GB.properties",
            "src/assets/dir/foo_es.properties"
        )),

        """
            class Test {

                @GDXAssets(skinFiles = "src/assets/d<caret>", atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf(
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf(
            "src/assets/dir/something",
            "src/assets/libgdx.skin",
            "src/assets/dir/test.pack"
        )),

        """
            class Test {

                @GDXAssets(skinFiles = {"something", "<caret>"}, atlasFiles = "")
                Skin skn1;

            }
          """ to (listOf(
            "src/assets/libgdx.skin",
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf(
            "src/assets/dir/something",
            "src/assets/dir/test.pack"
        )),

        """
            class Test {

                @GDXAssets(skinFiles = "", atlasFiles = {"<caret>"})
                Skin skn1;

            }
          """ to (listOf(
            "src/assets/dir/test.pack",
            "src/assets/libgdx.atlas",
            "src/assets/dir/holo.atlas"
        ) to listOf("src/assets/dir/something", "src/assets/libgdx.skin"))

    )

    private val kotlinAssetFileNameCompletionTests = listOf(
        """
            @GDXAssets(skinFiles = arrayOf("<caret>"))
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/libgdx.skin",
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf("src/assets/dir/something")),

        """
            @GDXAssets(skinFiles = ["<caret>"])
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/libgdx.skin",
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf("src/assets/dir/something")),

        """
            @GDXAssets(skinFiles = arrayOf("src/assets/d<caret>"))
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf(
            "src/assets/dir/something",
            "src/assets/libgdx.skin",
            "src/assets/dir/test.pack"
        )),

        """
            @GDXAssets(skinFiles = arrayOf("test", "<caret>"))
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/libgdx.skin",
            "src/assets/dir/holo.json",
            "src/assets/dir/skin.json"
        ) to listOf("src/assets/dir/something")),

        """
            @GDXAssets(skinFiles = arrayOf("test"), atlasFiles = arrayOf("", "<caret>"))
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/dir/test.pack",
            "src/assets/libgdx.atlas",
            "src/assets/dir/holo.atlas"
        ) to listOf("src/assets/dir/something", "src/assets/libgdx.skin")),

        """
            @GDXAssets(skinFiles = ["test"], atlasFiles = ["", "<caret>"])
            val s: Skin = Skin()
          """ to (listOf(
            "src/assets/dir/test.pack",
            "src/assets/libgdx.atlas",
            "src/assets/dir/holo.atlas"
        ) to listOf("src/assets/dir/something", "src/assets/libgdx.skin")),

        """
            @GDXAssets(skinFiles = arrayOf("test"), atlasFiles = arrayOf("", ""), propertiesFiles = arrayOf("<caret>"))
            val s = I18NBundle()
          """ to (listOf(
            "src/assets/test.properties",
            "src/assets/dir/foo.properties"
        ) to listOf(
            "src/assets/test_en.properties",
            "src/assets/test_en_GB.properties",
            "src/assets/dir/foo_es.properties"
        ))
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

    private fun doJavaTest(tests: List<Pair<String, Pair<List<String>, List<String>>>>) {
        for ((content, expected) in tests) {
            val source = """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin;
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;
            import com.badlogic.gdx.graphics.Color;
            import com.badlogic.gdx.utils.I18NBundle;

            $content
      """

            doTestCompletion("Test.java", source, expected.first, expected.second)
        }
    }

    private fun doKotlinTest(tests: List<Pair<String, Pair<List<String>, List<String>>>>) {
        for ((content, expected) in tests) {
            val source = """
            import com.badlogic.gdx.scenes.scene2d.ui.Skin
            import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets
            import com.badlogic.gdx.graphics.Color
            import com.badlogic.gdx.utils.I18NBundle

            $content
      """

            doTestCompletion("Test.kt", source, expected.first, expected.second)
        }
    }

    override fun setUp() {
        super.setUp()

        addDummyLibGDX199()
    }
}
