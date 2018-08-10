package com.example

import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("ktag")
class Kotli<caret>nClass {

  val bool = true

  @GDXTag("innerkotlin")
  class Inner {
    class Inner {}
  }

  private class Private

  inner class NonStatic

}

class SecondClass {

  class Inner {}

}