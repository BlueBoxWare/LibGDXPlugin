package main

import com.badlogic.gdx.utils.ArrayMap
import com.badlogic.gdx.utils.ObjectMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST")
class ArrayMapTest {

  var map: ArrayMap<String, Any> = ArrayMap()

  fun test() {

    map.put("a", <warning>ArrayMap<String, String>().values()</warning>)
    map.put("b", ArrayMap<String, String>())

    val iterator1 = <warning>map.keys()</warning>
    val iterator2 = <warning>map.values()</warning>
    val iterator3 = <warning>map.iterator()</warning>
    val iterator4 = <warning>map.entries()</warning>
    val iterator5 = <warning>(map.get("b") as ArrayMap<Any, Any>).values()</warning>

    for (e in <warning>map</warning>) {
      println(e.key)
    }

  }

}
