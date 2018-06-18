package com.example

class KotlinClass {

  val bool = true

  class Inner {
    class Inner {}
  }

  private class Private

  inner class NonStatic

}

class SecondClass {

  class Inner {}

  fun f() {
    com.badlogic.gdx.utils.Json().addClassTag("SubStyle", com.example.SubClass::class.java)
  }

}



class SubClass: com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle()