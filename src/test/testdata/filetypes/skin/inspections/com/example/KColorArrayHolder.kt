package com.example

import com.badlogic.gdx.graphics.Color
import com.example.ColorArrayHolder
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("kotlinTag1", "kotlinTag2")
class KColorArrayHolder {

  val colors = arrayOf(Color.BLACK)
  val ccolors = arrayOf(arrayOf(Color.BLUE))
  val bool = true
  val i = 1
  val f = 1.0
  val byte: Byte = 1

  var nc: Array<Color>? = null
  var nnc: Array<Color?>? = null

  val m = arrayOf(arrayOf(ColorArrayHolder()))

  class StaticInner {}
  inner class NonStaticInner {}

  fun f() {
    com.badlogic.gdx.utils.Json().addClassTag("Tag2", com.example.ColorArrayHolder::class.java)
    com.badlogic.gdx.utils.Json().addClassTag("Tag3", com.example.ColorArrayHolder::class.java)
  }

}