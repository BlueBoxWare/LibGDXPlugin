package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
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
class TestCompletion : LibGDXCodeInsightFixtureTestCase() {

  private val EMPTY = listOf<String>()

  private val tests = listOf(
          "{ <caret> }" to (listOf(
                  "com",
                  "java",
                  "com.badlogic.gdx.graphics.g2d.BitmapFont",
                  "com.example.MyTestClass",
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle",
                  "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle",
                  "com.example.KotlinClass\$StaticInner",
                  "com.example.KotlinClass\$StaticInner\$InnerInner",
                  "com.example.KotlinClass\$PrivateInner",
                  "Tag1",
                  "tag2",
                  "BitmapFont",
                  "TreeStyle",
                  "BM"
          ) to listOf(
                  "com.example.MyTestClass\$NonStatic",
                  "com.example.KotlinClass\$NonStaticInner",
                  "com.example.KotlinClass\$InnerObject"
          )),

          "{ \"<caret>\" }" to (listOf(
                  "com",
                  "java",
                  "com.badlogic.gdx.graphics.g2d.BitmapFont",
                  "com.example.MyTestClass",
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle",
                  "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle",
                  "com.example.KotlinClass\$StaticInner",
                  "com.example.KotlinClass\$StaticInner\$InnerInner",
                  "com.example.KotlinClass\$PrivateInner",
                  "Tag1",
                  "tag2",
                  "BitmapFont",
                  "TreeStyle",
                  "BM"
          ) to listOf(
                  "com.example.MyTestClass\$NonStatic",
                  "com.example.KotlinClass\$NonStaticInner",
                  "com.example.KotlinClass\$InnerObject"
          )),

          "{ \"Ko<caret>\" }" to (listOf(
                  "com.example.KotlinClass\$StaticInner",
                  "com.example.KotlinClass\$StaticInner\$InnerInner",
                  "com.example.KotlinClass\$PrivateInner"
          ) to listOf(
                  "com.example.KotlinClass\$NonStaticInner",
                  "com.example.KotlinClass\$InnerObject"
          )),

          "{ '<caret>' }" to (EMPTY to listOf(
                  "com",
                  "java",
                  "com.badlogic.gdx.graphics.g2d.BitmapFont"
          )),

          "{ co<caret> }" to (listOf("com") to listOf()),

          "{ \"co<caret>\" }" to (listOf("com") to listOf()),

          "{ My<caret> }" to (listOf(
                  "com.example.MyTestClass",
                  "com.example.MyOtherClass",
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf(
                  "com.example.MyTestClass\$NonStatic",
                  "com.badlogic.gdx.scenes.scene2d.ui.TextButton\$TextButtonStyle"
          )),

          "{ \"My<caret>\" }" to (listOf(
                  "com.example.MyTestClass",
                  "com.example.MyOtherClass"
          ) to listOf(
                  "com.example.MyTestClass\$NonStatic"
          )),

          "{ 'My<caret>' }" to (EMPTY to listOf(
                  "com.example.MyTestClass",
                  "com.example.MyOtherClass"
          )),

          "{ MyT<caret> }" to (listOf("com.example.MyTestClass") to listOf()),

          "{ com.example.MyT<caret> }" to (listOf(
                  "com.example.MyTestClass",
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          "{ com.example.MyTestClass\$<caret> }" to (listOf(
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          "{ com.example.MyTestClass\$Inn<caret> }" to (listOf(
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          "{ Skin\$<caret> }" to (listOf(
                  "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"
          ) to listOf()),

          "{ Skin\$Tin<caret> }" to (listOf(
                  "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"
          ) to listOf()),

          "{ \"com.example.MyTestClass\$<caret> }" to (listOf(
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          "{ \"com.example.MyTestClass\$Inn<caret>\" }" to (listOf(
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          "{ \"Skin\$<caret>\" }" to (listOf(
                  "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"
          ) to listOf()),

          "{ \"Skin\$Tin<caret> }" to (listOf(
                  "com.badlogic.gdx.scenes.scene2d.ui.Skin\$TintedDrawable"
          ) to listOf()),

          "{ \"com.example.MyT<caret>\" }" to (listOf(
                  "com.example.MyTestClass",
                  "com.example.MyTestClass\$Inner",
                  "com.example.MyTestClass\$InnerClass\$MyInnerStyle"
          ) to listOf()),

          """{ com.example.MyTestClass: {
            default: { <caret> }
            }
            }""" to (listOf("number", "name") to EMPTY),

          """{ com.example.MyTestClass: {
            default: { '<caret>' }
            }
            }""" to (EMPTY to listOf("number", "name")),

          """{ com.example.MyTestClass: {
            default: { "<caret>" }
            }
            }""" to (listOf("number", "name") to EMPTY),

          """{ com.example.MyTestClass: {
            default: { "<caret> }
            }
            }""" to (listOf("number", "name") to EMPTY),

          """{ com.example.MyTestClass: {
            default: { "<caret>": }
            }
            }""" to (listOf("number", "name") to EMPTY),

          """
            { com.example.MyTestClass: {
                default: {
                  number: 3
                  <caret>
                }
            } }""" to (listOf("name") to listOf()),

          """
            { com.example.MyTestClass${'$'}Inner: {
              default: {
              i<caret>
            }
            } }
          """ to (listOf("innerField") to listOf()),

          """
            { TagInner: {
              default: {
              i<caret>
            }
            } }
          """ to (listOf("innerField") to listOf()),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { <caret>
            } } }
          """ to (listOf("r", "g", "b", "a") to listOf()),

          """
            { Color: {
              default: { <caret>
            } } }
          """ to (listOf("r", "g", "b", "a") to listOf()),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { <caret>: 1
            } } }
          """ to (listOf("r", "g", "b", "a") to listOf()),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { }, color: {}, green: {}
            }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                b: { fontColor: <caret> }
            } }
          """ to (listOf("default", "color", "green") to listOf()),

          """
            { Color: {
              default: { }, color: {}, green: {}
            }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                b: { fontColor: <caret> }
            } }
          """ to (listOf("default", "color", "green") to listOf()),

          """
            { Color: {
              default: { }, color: {}, green: {}
            }
              TextButtonStyle: {
                b: { fontColor: <caret> }
            } }
          """ to (listOf("default", "color", "green") to listOf()),

          """
            { Color: {
              default: { },
              }
              com.badlogic.gdx.graphics.Color: {
              color: {}, green: {}
              }

              TextButtonStyle: {
                b: { fontColor: <caret> }
            } }
          """ to (listOf("default", "color", "green") to listOf()),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { }, color: {}, green: {}
            }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                b: { fontColor: '<caret>' }
            } }
          """ to (EMPTY to listOf("default", "color", "green")),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { }, color: {}, green: {}
            }
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                b: { fontColor: "<caret> }
            } }
          """ to (listOf("default", "color", "green") to listOf()),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { r: 1, <caret>: 1
            } } }
          """ to (listOf("g", "b", "a") to listOf("r")),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { r: 1, '<caret>': 1
            } } }
          """ to (EMPTY to listOf("r", "g", "b", "a")),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { r: 1, "<caret>": 1
            } } }
          """ to (listOf("g", "b", "a") to listOf("r")),

          """
            { Color: {
              default: { r: 1, "<caret>": 1
            } } }
          """ to (listOf("g", "b", "a") to listOf("r")),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { hex: 1, <caret>
            } } }
          """ to (EMPTY to listOf("r", "g", "b", "a", "hex")),

          """
            { com.badlogic.gdx.graphics.Color: {
              default: { r: 1, <caret>: 1, g: 0.0
            } } }
          """ to (listOf("b", "a") to listOf("r", "g", "hex")),

          """
            {com.badlogic.gdx.graphics.Color: {
                default: {
                  <caret>  : 3
            }}}
          """ to (listOf("r", "g", "b", "a", "hex") to listOf()),

          """
            {com.badlogic.gdx.graphics.Color: {
                default: {
                  r: 3
                  <caret>  : 3
            }}}
          """ to (listOf("g", "b", "a") to listOf("r", "hex")),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    default: {
                        down: <caret>,
                    }
                }
            }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to listOf()),

          """
            {
                TextButtonStyle: {
                    default: {
                        down: <caret>,
                    }
                }
            }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to listOf()),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    default: {
                        down: "<caret>",
                    }
                }
            }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to listOf()),

          """
            { com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                    default: {
                        color: skyblue,
                        name: <caret>
            } } }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to EMPTY),

          """
            { Tinted: {
                    default: {
                        color: skyblue,
                        name: <caret>
            } } }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to EMPTY),

          """
            { com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                    default: {
                        color: skyblue,
                        name: "<caret>
            } } }
          """ to (listOf("tree-minus", "selection", "textfield", "cursor") to EMPTY),

          """
            { com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                    tintedDrawable: {
                        color: skyblue,
                        name: check
                    },
              },
               com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    default: {
                      checked: <caret>
                    }
                }
            }
          """ to (listOf("tintedDrawable") to listOf()),

          """
            { TintedDrawable: {
                    tintedDrawable: {
                        color: skyblue,
                        name: check
                    },
              },
               TextButtonStyle: {
                    default: {
                      checked: <caret>
                    }
                }
            }
          """ to (listOf("tintedDrawable") to listOf()),

          """{ com.badlogic.gdx.graphics.g2d.BitmapFont: { font1: { <caret> } } }""" to (listOf("file", "scaledSize", "flip", "markupEnabled") to listOf("integer")),

          """
            {
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                font1: {
                  file: <caret>
                }
              }
            }
          """ to (listOf("font1.fnt", "assets/font2.fnt") to listOf("ui.atlas", "assets/somefile")),

          """
            {
              BitmapFont: {
                font1: {
                  file: <caret>
                }
              }
            }
          """ to (listOf("font1.fnt", "assets/font2.fnt") to listOf("ui.atlas", "assets/somefile")),

          """
            {
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                font1: {
                  file: "<caret>"
                }
              }
            }
          """ to (listOf("font1.fnt", "assets/font2.fnt") to listOf("ui.atlas", "assets/somefile")),

          """
            {
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                font1: {
                  markupEnabled: <caret>
                }
              }
            }
          """ to (listOf("true", "false") to listOf()),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {

                    green: {
                        down: round-down, up: round,
                        font: medium, fontColor: { a: 1, <caret>  }
                    }
                }
            }
          """ to (listOf("r", "g", "b") to listOf("a", "checkedFontColor", "hex")),

          """
            {
                TextButtonStyle: {

                    green: {
                        down: round-down, up: round,
                        font: medium, fontColor: { a: 1, <caret>  }
                    }
                }
            }
          """ to (listOf("r", "g", "b") to listOf("a", "checkedFontColor", "hex")),

          """
            {
               com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    green: {
                        font: { flip: true, <caret> }
                    }
                }
            }
          """ to (listOf("file", "scaledSize",  "markupEnabled") to listOf("flip")),

          """
            {
               TextButtonStyle: {
                    green: {
                        font: { flip: true, <caret> }
                    }
                }
            }
          """ to (listOf("file", "scaledSize",  "markupEnabled") to listOf("flip")),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    green: {
                        fontColor: {  <caret> }
                    }
                }
            }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    green: {
                        fontColor: {  "<caret>" }
                    }
                }
            }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            {
                com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                    green: {
                        fontColor: {  hex: 1, <caret> }
                    }
                }
            }
          """ to (EMPTY to listOf("r", "g", "b", "a", "hex")),

          """
            {
              com.example.MyTestClass: {
                testClass: { }
              }
              com.example.MyOtherClass: {
                otherClass: { }
              }
              com.example.AThirdClass: {
                aThirdClass: {
                  myTestClass: <caret>
                }
              }
            }

          """ to (listOf("testClass") to listOf("otherClass")),

          """
            {
              Tag1: {
                testClass: { }
              }
              com.example.MyOtherClass: {
                otherClass: { }
              }
              com.example.AThirdClass: {
                aThirdClass: {
                  myTestClass: <caret>
                }
              }
            }

          """ to (listOf("testClass") to listOf("otherClass")),

          """
            {
              com.example.MyTestClass: {
                testClass: { }
              }
              com.example.MyOtherClass: {
                otherClass: { }
              }
              com.example.AThirdClass: {
                aThirdClass: {
                  myTestClass: "<caret>"
                }
              }
            }

          """ to (listOf("testClass") to listOf("otherClass")),


          """
            {
              com.example.MyTestClass: {
                testClass: { }
              }
              com.example.MyOtherClass: {
                otherClass: { }
              }
              com.example.AThirdClass: {
                aThirdClass: {
                  myTestClass: testClass,
                  myOtherClass: <caret>
                }
              }
            }

          """ to (listOf("otherClass") to listOf("testClass")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                <caret>
              }
            }
          """ to (listOf("default") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                '<caret>'
              }
            }
          """ to (EMPTY to listOf("default")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                "<caret>":
              }
            }
          """ to (listOf("default") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                red: { }
                <caret>
              }
            }
          """ to (listOf("default") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                default: { }
                <caret>
              }
            }
          """ to (EMPTY to listOf("default")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                name: <caret>
              }
            }
          """ to (listOf("color") to listOf("name")),

          """
            {
              Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                name: <caret>
              }
            }
          """ to (listOf("color") to listOf("name")),


          """
            {
              com.badlogic.gdx.graphics.Color: {
                "\u0064\nd\"d": { r: 1, g: 1, b: 1, a: 1 },
                name: <caret>
              }
            }
          """ to (listOf("d\\nd\\\"d") to listOf("name")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                \u0064\nd\"d: { r: 1, g: 1, b: 1, a: 1 },
                \u0064\nd\"d\"'"\/: {}
                "())([}{}": {}
                name: <caret>
              }
            }
          """ to (listOf("d\\nd\\\"d", "d\\nd\\\"d\\\"'\\\"/", "())([}{}\"") to listOf("name")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                name: "<caret>"
              }
            }
          """ to (listOf("color") to listOf("name")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                name: "col<caret>"
              }
            }
          """ to (listOf("color") to listOf("name")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                "name1": color
                'name2': bla
                test: <caret>
              }
            }
          """ to (listOf("color", "name1", "'name2'") to listOf("test")),

          """
            {
              com.badlogic.gdx.graphics.Color: {
                color: { r: 1, g: 1, b: 1, a: 1 },
                "name1": color
                'name2': bla
                test: "<caret>"
              }
            }
          """ to (listOf("color", "name1", "'name2'") to listOf("test")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                tbs: { checked: test }
              },
              com.badlogic.gdx.scenes.scene2d.ui.TextField${'$'}TextFieldStyle: {
                def: { cursor: bla },
                test: <caret>
              }
            }
          """ to (listOf("def") to listOf("test", "tbs")),

          """
            {
              TextButtonStyle: {
                tbs: { checked: test }
              },
              TextFieldStyle: {
                def: { cursor: bla },
                test: <caret>
              }
            }
          """ to (listOf("def") to listOf("test", "tbs")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                tbs: { checked: test }
              },
              com.badlogic.gdx.scenes.scene2d.ui.TextField${'$'}TextFieldStyle: {
                def: { cursor: bla },
                test: "<caret>"
              }
            }
          """ to (listOf("def") to listOf("test", "tbs")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                tbs: { checked: test }
              },
              com.badlogic.gdx.scenes.scene2d.ui.TextField${'$'}TextFieldStyle: {
                def: { cursor: bla },
                s: {}
                test: "<caret>
              }
            }
          """ to (listOf("def", "s") to listOf("test", "tbs")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                tbs: { checked: test }
              },
              com.badlogic.gdx.scenes.scene2d.ui.TextField${'$'}TextFieldStyle: {
                def: { cursor: bla },
                s: {}
                test: "<caret>"
              }
            }
          """ to (listOf("def", "s") to listOf("test", "tbs")),

          """
            {
              com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
              com.example.MyTestClass: {
                default: {
                  colors: [green, <caret>]
                }
              }
            }
          """ to (listOf("red", "green", "blue") to EMPTY),

          """
            {
              com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
              com.example.MyTestClass: {
                default: {
                  moreColors: [[green], [<caret>]]
                }
              }
            }
          """ to (listOf("red", "green", "blue") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
              com.example.MyTestClass: {
                default: {
                  moreColors: [[green], ["<caret>"]]
                }
              }
            }
          """ to (listOf("red", "green", "blue") to listOf()),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                t1: {}
                t2: {
                  fontColor: { hex: "ff0000" }
                }
              },
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
              com.example.KotlinClass: {
                default: {
                  buttonStyles: [ <caret> ]
                }
              }
            }
          """ to (listOf("t1", "t2") to listOf("t3", "default")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                t1: {}
                t2: {
                  fontColor: { hex: "ff0000" }
                }
              },
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
              com.example.KotlinClass: {
                default: {
                  buttonStyles: [ "<caret> ]
                }
              }
            }
          """ to (listOf("t1", "t2") to listOf("t3", "default")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                t1: {}
                t2: {
                  fontColor: { hex: "ff0000" }
                }
              },
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
              com.example.KotlinClass: {
                default: {
                  listStyles: [[ <caret> ]]
                }
              }
            }
          """ to (listOf("t3", "default") to listOf("t1", "t2")),

          """
            { com.example.MyTestClass: {
              default: {
              bool: <caret>
            }
            } }
          """ to (listOf("true", "false") to listOf()),

          """
            { com.example.MyTestClass: {
              default: {
              bools: [true, <caret>]
            }
            } }
          """ to (listOf("true", "false") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
            com.example.MyTestClass: {
              default: {
              textButtonStyle: {
                fontColor: <caret>
              }
            }
            } }
          """ to (listOf("red", "green", "blue") to listOf()),

          """
            {
              com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
            com.example.MyTestClass: {
              default: {
              textButtonStyles: [{
                fontColor: <caret>
              }]
            }
            } }
          """ to (listOf("red", "green", "blue") to listOf()),

          """
            {
                com.badlogic.gdx.graphics.g2d.BitmapFont: {
                  a: {}
                  b: {}
                }
              com.example.KotlinClass: {
                default: {
                  buttonStyles: [ { font: <caret> } ]
                }
              }
            }
          """ to (listOf("a", "b") to listOf("default")),

          """
            { com.example.MyTestClass: {
              default: {
              m: [{m: [{ <caret> }]}]
            }
            } }
          """ to (listOf("m", "colors", "textButtonStyle", "moreColors") to listOf()),

          """
            { com.example.MyTestClass: {
              default: {
              m: [{m: [{ "<caret>" }]}]
            }
            } }
          """ to (listOf("m", "colors", "textButtonStyle", "moreColors") to listOf()),

          """
            { com.example.MyTestClass: {
              default: {
              m: [{m: [{ m: [{  moreColors: [[{<caret>}]]}] }]}]
            }
            } }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            { com.example.MyTestClass: {
              default: {
              m: [{m: [{ m: [{  moreColors: [[{"<caret>"}]]}] }]}]
            }
            } }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            { com.example.MyTestClass: {
              default: {
              m: [{m: [{ m: [{  moreColors: [[{"<caret>}]]}] }]}]
            }
            } }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            {
                com.badlogic.gdx.graphics.Color: {

                red: {hex: "ff0000"},
                green: {hex: "00ff00"},
                blue: {r: 0, g: 0, b: 1}

              },
            com.example.MyTestClass: {
              default: {
              m: [{m: [{ m: [{  moreColors: [[<caret>]]}] }]}]
            }
            } }
          """ to (listOf("red", "green", "blue") to listOf("r", "g", "b", "hex")),

          """
            {
            com.example.MyTestClass: {
              x: {}
              n: {},
              default: {
              m: [{m: [{ m: [<caret>] }]}]
            }
            } }
          """ to (listOf("n", "x") to listOf()),


          """
            {
            com.example.KotlinClass: {
              x: {}
              n: {},
              default: {
              m: [{m: [{ m: [<caret>] }]}]
            }
            } }
          """ to (listOf("n", "x") to listOf()),

          """
            {
            com.example.KotlinClass: {
              x: {}
              n: {},
              default: {
              m: [{m: [{ m: ["<caret>"] }]}]
            }
            } }
          """ to (listOf("n", "x") to listOf()),

          """
            {
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", <caret> }] }]}]
            }
            } }
          """ to (listOf("buttonStyles", "listStyles") to listOf("m")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", listStyles: [[<caret>]] }] }]}]
            }
            } }
          """ to (listOf("default", "t3") to listOf("m")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", listStyles: [[{<caret>}]] }] }]}]
            }
            } }
          """ to (listOf("font", "selection", "fontColorSelected") to listOf("m")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", listStyles: [[{"<caret>"}]] }] }]}]
            }
            } }
          """ to (listOf("font", "selection", "fontColorSelected") to listOf("m")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", listStyles: [[{fontColorSelected: {<caret>}]] }] }]}]
            }
            } }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                t3: {},
                default: {}
              }
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ m: "", listStyles: [[{fontColorSelected: {"<caret>"}]] }] }]}]
            }
            } }
          """ to (listOf("r", "g", "b", "hex") to listOf()),

          """
            {
            com.example.KotlinClass: {
              default: {
              m: [{m: [{ m: [{ bools: [<caret>] }] }]}]
            }
            } }
          """ to (listOf("true", "false") to EMPTY),

          """
            {
            com.badlogic.gdx.graphics.g2d.BitmapFont: {
              z: {},
              k: {}
            },

              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                l: {}
                x: <caret>
              }
            }
          """ to (listOf("z", "k", "l") to listOf("x")),

          """
            {
            BitmapFont: {
              z: {},
              k: {}
            },

              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                l: {}
                x: <caret>
              }
            }
          """ to (listOf("z", "k", "l") to listOf("x")),

          """
            {
            com.badlogic.gdx.graphics.g2d.BitmapFont: {
              z: {},
              k: {}
            },

              BitmapFont: {
                l: {}
                x: <caret>
              }
            }
          """ to (listOf("z", "k", "l") to listOf("x")),

          """
            {,

              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                l: {}
                x: <caret>
              }

            com.badlogic.gdx.graphics.g2d.BitmapFont: {
              z: {},
              k: {},
              x: {}
            }}
          """ to (listOf("l") to listOf("z", "k", "x")),

          """
            {,
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                z: {},
                k: {},
                x: {}
              }
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                l: {}
                x: <caret>
              }

         }
          """ to (listOf("l", "z", "k", "x") to listOf()),

          """
            {

              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                zz: {
                  checked: start-pressed,
                  disabled: start-pressed
                },
                xx: {}
              },

              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                drawable: {
                  name: start-pressed,
                  color: start-pressed
                },
                something: {}
              },

              com.badlogic.gdx.graphics.Color: {
                color1: {}
                color2: {}
              }

              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                zz: {}
                <caret>
              }

              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                color1: {}
              }
            }
          """ to (listOf("default", "xx", "drawable", "something", "color2") to listOf("zz", "color1")),

          """
            {

              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                zz: {
                  checked: start-pressed,
                  disabled: start-pressed
                },
                xx: {}
              },

              TintedDrawable: {
                drawable: {
                  name: start-pressed,
                  color: start-pressed
                },
                something: {}
              },

              Color: {
                color1: {}
                color2: {}
              }

              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                zz: {}
                <caret>
              }

              ListStyle: {
                color1: {}
              }
            }
          """ to (listOf("default", "xx", "drawable", "something", "color2") to listOf("zz", "color1")),

          """
            {

              com.badlogic.gdx.scenes.scene2d.ui.TextButton${'$'}TextButtonStyle: {
                zz: {
                  checked: start-pressed,
                  disabled: start-pressed
                },
                xx: {}
              },

              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                drawable: {
                  name: start-pressed,
                  color: start-pressed
                },
                something: {}
              },

              com.badlogic.gdx.graphics.Color: {
                color1: {}
                color2: {}
              }

              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                zz: {}
                "<caret>"
              }

              com.badlogic.gdx.scenes.scene2d.ui.List${'$'}ListStyle: {
                color1: {}
              }
            }
          """ to (listOf("default", "xx", "drawable", "something", "color2") to listOf("zz", "color1")),

          """
            {
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                default: { file: <caret> }
              }
            }
          """ to (listOf("assets/font2.fnt", "assets/somedir/font.fnt") to listOf("assets/ui.atlas", "assets/somedir/anotherfile")),

          """
            {
              com.badlogic.gdx.graphics.g2d.BitmapFont: {
                default: { file: assets/some<caret> }
              }
            }
          """ to (listOf("assets/somedir/font.fnt") to listOf("assets/font2.fnt", "assets/ui.atlas", "assets/somedir/anotherfile")),

          """
            {
              com.example.MyTestClass: {
                default: {
                  textButtonStyles: [
                    {
                      font: { file: assets<caret> }
                    }
                  ]
                }
              }
            }
          """ to (listOf("assets/somedir/font.fnt", "assets/font2.fnt") to listOf("assets/ui.atlas", "assets/somedir/anotherfile")),

          """
            {
              // @<caret>
            }
          """ to (listOf("Suppress") to listOf()),

          """
              /*
                 @<caret>
              */

          """ to (listOf("Suppress") to listOf()),


          """
            {
              // @<caret> abc
            }
          """ to (listOf("Suppress") to listOf()),

          """
            {
              // @SU<caret> abc
            }
          """ to (listOf("Suppress") to listOf()),

          """
            {
              // @SUPPRESS ( "  <caret>
            }
          """ to (listOf(
                  "DuplicateProperty",
                  "DuplicateResource",
                  "MalformedColorString",
                  "MissingProperty",
                  "NonExistingClass",
                  "NonExistingField",
                  "NonExistingFile",
                  "NonExistingResourceInAlias",
                  "TypeError"
          ) to listOf()),

          """
            {
              // @Suppress(  Non<caret>
            }
          """ to (listOf(
                  "NonExistingClass",
                  "NonExistingField",
                  "NonExistingFile",
                  "NonExistingResourceInAlias"
          ) to listOf(
                  "DuplicateProperty",
                  "DuplicateResource",
                  "MalformedColorString",
                  "MissingProperty"
          )),

          """
            {
              // @Suppress("Non<caret>
            }
          """ to (listOf(
                  "NonExistingClass",
                  "NonExistingField",
                  "NonExistingFile",
                  "NonExistingResourceInAlias"
          ) to listOf(
                  "DuplicateProperty",
                  "DuplicateResource",
                  "MalformedColorString",
                  "MissingProperty"
          )),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                a: {}
              }
              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                x: {}
                y: {}
                u: <caret>
              }
            }
          """ to (EMPTY to listOf("a", "x", "y")),

          """
            {
              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                a: {}
              }
              TintedDrawable: {
                x: {}
                y: {}
                u: <caret>
              }
            }
          """ to (EMPTY to listOf("a", "x", "y")),

          """
            {
              TintedDrawable: {
                a: {}
              }
              com.badlogic.gdx.scenes.scene2d.ui.Skin${'$'}TintedDrawable: {
                x: {}
                y: {}
                u: <caret>
              }
            }
          """ to (EMPTY to listOf("a", "x", "y"))
  )

  fun testCompletions() {
    for ((content, expected) in tests) {
      doTest(content, expected.first, expected.second)
    }
  }

  fun doTest(content: String, expectedCompletionStrings: List<String>, notExpectedCompletionStrings: List<String> = listOf()) {
    myFixture.configureByText("ui.skin", content)
    val result = myFixture.complete(CompletionType.BASIC, 1)
    if (result == null) {
      // the only item was auto-completed?
      assertEquals("Got only 1 result. Expected results: $expectedCompletionStrings. Content: \n'$content'", expectedCompletionStrings.size, 1)
      val text = myFixture.editor.document.text
      val expectedString = expectedCompletionStrings.first()
      val msg = "Expected string '$expectedString' not found. Content: \n'$content'"
      assertTrue(msg, text.contains(expectedString))
    } else {
      val strings = myFixture.lookupElementStrings?.map { if (listOf('"').contains(it.firstOrNull())) it.substring(1) else it }
      assertNotNull(strings)
      strings?.let { results ->
        for (expected in expectedCompletionStrings) {
          assertTrue("'$expected' expected but not found, Content:\n'$content'", results.contains(expected))
        }
        for (notExpectedCompletionString in notExpectedCompletionStrings) {
          val msg2 = "Not expected to find '$notExpectedCompletionString'. Content:\n '$content'"
          assertFalse(msg2, strings.contains(notExpectedCompletionString))
        }
      }
    }
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addKotlin()

    addDummyLibGDX199()

    myFixture.copyFileToProject("filetypes/skin/completion/com/example/MyTestClass.java", "com/example/MyTestClass.java")
    myFixture.copyFileToProject("filetypes/skin/completion/com/example/MyOtherClass.java", "com/example/MyOtherClass.java")
    myFixture.copyFileToProject("filetypes/skin/completion/com/example/AThirdClass.java", "com/example/AThirdClass.java")
    myFixture.copyFileToProject("filetypes/skin/completion/com/example/KotlinClass.kt", "com/example/KotlinClass.kt")
    myFixture.copyFileToProject("ui.atlas")
    myFixture.copyFileToProject("font1.fnt")
    myFixture.copyDirectoryToProject("assets", "assets")
  }

}