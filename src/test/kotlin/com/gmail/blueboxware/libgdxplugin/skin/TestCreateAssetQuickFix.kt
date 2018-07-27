package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinNonExistingResourceAliasInspection
import com.gmail.blueboxware.libgdxplugin.testname


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

  fun test1() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                default: foo<caret>
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                foo: {<caret> }
                default: foo
              }
            }
          """.trimIndent()
  )

  fun test2() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                bar: { }
                default: foo<caret>
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                bar: { }
                foo: {<caret> }
                default: foo
              }
            }
          """.trimIndent()
  )

  fun test3() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                bar: { }
                default: foo<caret>
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                bar: { }
                foo: {<caret> }
                default: foo
              }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
              }
            }
          """.trimIndent()
  )

  fun test4() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Button${"$"}ButtonStyle: {
                f: { checked: fo<caret>o }
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  color: { hex: "#ffffff" }
                  name:${" "}<caret>
                }
              }
              com.badlogic.gdx.scenes.scene2d.ui.Button${"$"}ButtonStyle: {
                f: { checked: foo }
              }
            }
          """.trimIndent()
  )

  fun test5() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  name: dwd<caret>qwd
                }
              }
            }
          """.trimIndent(),
          "",
          intentionShouldBeAvailable = false
  )

  fun test6() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  color: foo<caret>
                }
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.graphics.Color: {
                foo: {
                  hex: "#<caret>"
                }
              }
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  color: foo
                }
              }
            }
          """.trimIndent()
  )

  fun test7() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  color: foo<caret>
                }
              }
              com.badlogic.gdx.graphics.Color: {
                c1: {
                  hex: "#ffffff"
                }
                c2: {}
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: {
                  color: foo
                }
              }
              com.badlogic.gdx.graphics.Color: {
                c1: {
                  hex: "#ffffff"
                }
                c2: {}
                foo: {
                  hex: "#<caret>"
                }
              }
            }
          """.trimIndent()
  )

  fun test8() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${"$"}TintedDrawable: {
                foo: bar<caret>
              }
            }
          """.trimIndent(),
          "",
          intentionShouldBeAvailable = false
  )

  fun test991() = doTest(
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                default: { parent: foo<caret> }
              }
            }
          """.trimIndent(),
          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${"$"}TextButtonStyle: {
                foo: {<caret> }
                default: { parent: foo }
              }
            }
          """.trimIndent()
  )

  fun test992() = doTest(
          """
            {
              TintedDrawable: {
                foo: {
                  color: foo<caret>
                }
              }
            }
          """.trimIndent(),
          """
            {
              Color: {
                foo: {
                  hex: "#<caret>"
                }
              }
              TintedDrawable: {
                foo: {
                  color: foo
                }
              }
            }
          """.trimIndent()
  )

  fun doTest(
          test: String,
          expectedResult: String,
          intentionShouldBeAvailable: Boolean = true
  ) {

    myFixture.configureByText("skin.skin", test)

    for (intention in myFixture.availableIntentions) {
      if (intention.familyName.startsWith("Create resource")) {
        if (!intentionShouldBeAvailable) {
          throw AssertionError("Unexpected intention found")
        }
        myFixture.launchAction(intention)
        myFixture.checkResult(expectedResult)
        return
      }
    }

    if (intentionShouldBeAvailable) {
      throw AssertionError("Intention not found")
    }

  }

  override fun setUp() {
    super.setUp()

    addLibGDX()

    if (testname().contains("99")) {
      addDummyLibGDX199()
    } else {
      removeDummyLibGDX199()
    }

    myFixture.enableInspections(SkinNonExistingResourceAliasInspection())

  }

}