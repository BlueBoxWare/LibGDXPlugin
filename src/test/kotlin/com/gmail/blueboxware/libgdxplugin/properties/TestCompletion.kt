package com.gmail.blueboxware.libgdxplugin.properties


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
class TestCompletion : PropertiesCodeInsightFixtureTestCase() {

    private val javaTests = listOf(
        """
            new I18NBundle().get("<caret>");
          """ to (listOf(
            "noTranslation",
            "germanTranslation",
            "french.Only",
            "default",
            "spain",
            "something"
        ) to listOf()),
        """
            new I18NBundle().get("s<caret>");
          """ to (listOf(
            "spain",
            "something",
            "noTranslation",
            "germanTranslation"
        ) to listOf("french.Only", "default")),
        """
            new I18NBundle().get("so<caret>");
          """ to (listOf("something") to listOf(
            "spain",
            "noTranslation",
            "germanTranslation",
            "french.Only",
            "default"
        )),

        """
            i18NBundle.format("<caret>");
          """ to (listOf("noTranslation", "french.Only") to listOf("something", "default", "spain")),
        """
            i18NBundle.format("no<caret>", "2", "3");
          """ to (listOf("noTranslation") to listOf("something", "default", "spain", "french.Only")),
        """
            i18NBundle.get("foo<caret>");
          """ to (listOf<String>() to listOf("something", "default", "spain", "french.Only", "noTranslation")),

        """
            i18NBundle2.get("<caret>");
          """ to (listOf<String>() to listOf("something", "default", "spain", "french.Only", "noTranslation")),
        """
            i18NBundle3.get("<caret>");
          """ to (listOf("spain", "default", "something", "extra", "also") to listOf("french.Only", "noTranslation"))
    )

    private val kotlinTests = listOf(
        """
            I18NBundle().get("<caret>");
          """ to (listOf(
            "noTranslation",
            "germanTranslation",
            "french.Only",
            "default",
            "spain",
            "something"
        ) to listOf()),
        """
            I18NBundle().get("s<caret>");
          """ to (listOf(
            "spain",
            "something",
            "noTranslation",
            "germanTranslation"
        ) to listOf("french.Only", "default")),
        """
            I18NBundle().get("so<caret>");
          """ to (listOf("something") to listOf(
            "spain",
            "noTranslation",
            "germanTranslation",
            "french.Only",
            "default"
        )),

        """
            i18NBundle.format("<caret>");
          """ to (listOf("noTranslation", "french.Only") to listOf("something", "default", "spain")),
        """
            i18NBundle.format("no<caret>", "2", "3");
          """ to (listOf("noTranslation") to listOf("something", "default", "spain", "french.Only")),
        """
            i18NBundle.get("foo<caret>");
          """ to (listOf<String>() to listOf("something", "default", "spain", "french.Only", "noTranslation")),

        """
            i18NBundle2.get("<caret>");
          """ to (listOf<String>() to listOf("something", "default", "spain", "french.Only", "noTranslation")),
        """
            i18NBundle3.get("<caret>");
          """ to (listOf("spain", "default", "something", "extra", "also") to listOf("french.Only", "noTranslation"))
    )

    fun testKotlinCompletion() {
        for ((text, expectedResults) in kotlinTests) {
            val content = """
        import com.badlogic.gdx.utils.I18NBundle
        import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

          @GDXAssets(propertiesFiles = ["src/messages.properties"])
          val i18NBundle = I18NBundle()
          @GDXAssets(propertiesFiles = arrayOf("src/doesnotexist.properties"))
          val i18NBundle2 = I18NBundle()
          @GDXAssets(propertiesFiles = arrayOf("src/test.properties", "src/extra.properties"))
          val i18NBundle3 = I18NBundle();

        fun f() {
          $text
        }
        """
            doTestCompletion("Test.kt", content, expectedResults.first, expectedResults.second)
        }
    }

    fun testJavaCompletion() {
        for ((text, expectedResults) in javaTests) {
            val content = """
        import com.badlogic.gdx.utils.I18NBundle;
        import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

        class Test {

          @GDXAssets(propertiesFiles = {"src/messages.properties"})
          I18NBundle i18NBundle;
          @GDXAssets(propertiesFiles = {"src/doesnotexist.properties"})
          I18NBundle i18NBundle2;
          @GDXAssets(propertiesFiles = {"src/test.properties", "src/extra.properties"})
          I18NBundle i18NBundle3;

          void m() {
            $text
          }
        }
      """
            doTestCompletion("Test.java", content, expectedResults.first, expectedResults.second)
        }
    }

}
