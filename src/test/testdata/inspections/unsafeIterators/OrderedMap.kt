package main

import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.OrderedMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class OrderedMapT {

  var map = OrderedMap<String, Any>()
  var map2 = OrderedMap<String, OrderedMap<String, Any>>()

  fun test() {
    map2.put("abc", map)

    for (e in <warning>map2</warning>) {
      for (e1 in <warning>e.value</warning>) {

    }
    }
  }

  var i1: Iterator<Any> = <warning>map.keys()</warning>
  var i2: Iterator<Any> = <warning>map.values()</warning>
  var i3: Iterator<Any> = <warning>map.iterator()</warning>
  var i4: Iterator<Any> = <warning>map.entries()</warning>

  var o: Any = <warning><warning>map2.values()</warning>.next().values()</warning>

}

