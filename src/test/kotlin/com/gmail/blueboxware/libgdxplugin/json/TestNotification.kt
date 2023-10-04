package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.editor.showNotification
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.intellij.lang.LanguageUtil


/*
 * Copyright 2019 Blue Box Ware
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
class TestNotification : LibGDXCodeInsightFixtureTestCase() {

    fun test1() = doTest(
        "test.json",
        true,
        SKIN
    )

    fun test2() = doTest(
        "test.txt",
        false,
        SKIN
    )

    fun test3() = doTest(
        "test.json",
        false,
        "{}"
    )

    fun test4() = doTest(
        "test.json",
        true,
        """
            {
              foo: "foo",
              foo: "foo",
              foo: "foo",
              foo: "foo",
              foo: "foo",
              foo: "foo",
            }
          """.trimIndent()
    )

    fun test5() = doTest(
        "test.json",
        true,
        """
            {
              "foo": foo,
              "foo": foo,
              "foo": foo,
              "foo": foo,
              "foo": foo,
              "foo": foo,
            }
          """.trimIndent()
    )

    fun test6() = doTest(
        "test.json",
        false,
        """
            {
              "foo": "foo",
              "foo": foo,
              "foo": foo,
              "foo": foo,
              "foo": foo,
              "foo": foo,
            }
          """.trimIndent()
    )

    fun test7() = doTest(
        "test.json",
        true,
        """
            {
              "foo": {
                "bar": [
                  {
                    "f": [a, b, c, d, e, f]
                  }
                ]
              }
            }
          """.trimIndent()
    )

    fun test8() = doTest(
        "test.json",
        false,
        """
            {
              "foo": {
                "bar": [
                  {
                    "f": ["a", "b", c, d, e, f]
                  }
                ]
              }
            }
          """.trimIndent()
    )


    fun doTest(fileName: String, expectedResult: Boolean, content: String) {
        configureByText(fileName, content)
        assertEquals(
            expectedResult,
            showNotification(
                project,
                LanguageUtil.getLanguageForPsi(project, file.virtualFile),
                file.virtualFile,
                project.getService(LibGDXPluginSettings::class.java)
            )
        )
    }

    companion object {

        val SKIN = """
              {
                com.badlogic.gdx.graphics.Color: {
                  white: { r: 1, g: 1, b: 1, a: 1 },
                  red: { r: 1, g: 0, b: 0, a: 1 },
                  yellow: { r: 0.5, g: 0.5, b: 0, a: 1 }
                },
                com.badlogic.gdx.graphics.g2d.BitmapFont: {
                  medium: { file: medium.fnt }
                },
                com.badlogic.gdx.scenes.scene2d.ui.TextButton: {
                  default: {
                    down: round-down, up: round,
                    font: medium, fontColor: white
                  },
                  toggle: {
                    down: round-down, up: round, checked: round-down,
                    font: medium, fontColor: white, checkedFontColor: red
                  },
                  green: {
                    down: round-down, up: round,
                    font: medium, fontColor: { r: 0, g: 1, b: 0, a: 1 }
                  }
                }
              }
    """.trimIndent()

    }

}
