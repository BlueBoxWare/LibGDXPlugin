package com.example

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.List

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