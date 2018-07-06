package com.example

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

class KotlinClass {

  val buttonStyles = arrayOf<TextButton.TextButtonStyle>()
  val listStyles = arrayOf(arrayOf<List.ListStyle>())
  val colors = arrayOf<Color>()
  val moreColors = arrayOf(arrayOf<Color>())
  val bitmapFonts = arrayOf<BitmapFont>()
  val tds = arrayOf<Skin.TintedDrawable>()

  var textButtonStyle: TextButton.TextButtonStyle? = null
  var textButtonStyles: Array<TextButton.TextButtonStyle>? = null
  var buttonStyle: Button.ButtonStyle? = null

  val m = arrayOf<KotlinClass>()

  fun t() {
    com.badlogic.gdx.utils.Json().addClassTag("Tag1", com.example.KotlinClass::class.java)
    com.badlogic.gdx.utils.Json().addClassTag("Tag2", com.example.KotlinClass::class.java)
  }

}

@GDXTag("kotlinTag1", "kotlinTag2")
class SubClass: com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle()