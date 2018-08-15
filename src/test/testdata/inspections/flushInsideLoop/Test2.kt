package flushInsideLoop

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class Clazz {

  var i: Int = 0
    get() {
      PolygonSpriteBatch().flush()
      return 1
    }

  var j: Int = 0
    set(value) {
      PolygonSpriteBatch().flush()
      j = value
    }

  fun bla() {
    PolygonSpriteBatch().flush()
  }


}

class Clazz1 {
  init {
    SpriteBatch().transformMatrix = null
  }
}

class Clazz2 {
  constructor() {

  }

  @Suppress("UNUSED_PARAMETER")
  constructor(str: String) {
    SpriteBatch().transformMatrix = null
  }
}