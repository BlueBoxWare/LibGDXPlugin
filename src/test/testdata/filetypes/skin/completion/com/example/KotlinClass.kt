package com.example

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("kotlinTag1", "kotlinTag2", "kotlinTag3", "kotlinTag4")
class KotlinClass {

  var m: Array<KotlinClass>? = null

  val buttonStyles = arrayOf<TextButton.TextButtonStyle>()
  val listStyles = arrayOf(arrayOf<List.ListStyle>())

  val bools = arrayOf(true)

  class StaticInner {
    class InnerInner
  }
  private class PrivateInner
  inner class NonStaticInner
  object InnerObject

}

fun t() {
  com.badlogic.gdx.utils.Json().addClassTag("Tag1", com.example.MyTestClass::class.java)
}