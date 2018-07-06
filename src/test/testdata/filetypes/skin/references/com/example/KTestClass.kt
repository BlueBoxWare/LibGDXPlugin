package com.example

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("kotlinTag1", "kotlinTag2")
@GDXTag(value = ["kotlinTag4", "kotlinTag5"])
class KTestClass {

  val labelStyles = arrayOf<Label.LabelStyle>()
  val m = arrayOf<MyTestClass>()
  val tc = KTestClass()

  fun t() {
    com.badlogic.gdx.utils.Json().addClassTag("Tag1", com.example.MyTestClass::class.java)
    com.badlogic.gdx.utils.Json().addClassTag("Tag3", KTestClass::class.java)
    com.badlogic.gdx.utils.Json().addClassTag("ColorTag", com.badlogic.gdx.graphics.Color::class.java)
    com.badlogic.gdx.utils.Json().addClassTag("SubStyle", com.example.SubStyle::class.java)
  }

}

@GDXTag(value = ["kotlinTag3"])
class SubStyle: com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle()