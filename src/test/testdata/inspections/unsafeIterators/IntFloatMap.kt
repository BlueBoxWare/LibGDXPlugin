package main

import com.badlogic.gdx.utils.IntFloatMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST")
class IntFloatMapTestKotlin {

  private val map = IntFloatMap()

  companion object {
    var map2: IntFloatMap? = null
  }

  internal fun test() {

    <warning>map.keys()</warning>.hasNext()
    <warning>map.entries()</warning>.iterator()

    map2 = map

    val iterator1 = <warning>map.iterator()</warning>
    val keys = <warning>map2?.keys()</warning>
    val entries = <warning>map2?.entries()</warning>
    val values = <warning>map2?.values()</warning>

    val keys2 = IntFloatMap.Keys(map2)



  }

}

