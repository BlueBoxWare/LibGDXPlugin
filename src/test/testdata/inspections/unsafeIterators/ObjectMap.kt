package main

import com.badlogic.gdx.utils.ObjectMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class ObjectMapTestKotlin {

  fun test() {
    val map1 = ObjectMap<ObjectMap<String, ObjectMap<Any, Any>>, Any>()
    val map2 = ObjectMap<String, Any>()

    for (m in <warning>map1</warning>) {
      for (e in <warning>m.key</warning>) {
      for (p in <warning>e.value</warning>) {

    }
    }
    }

    val i = <warning>map2.keys()</warning>
    val i2 = <warning>map2.values()</warning>
    val i3 = <warning>map2.entries()</warning>
    val i4 = <warning>map2.iterator()</warning>
  }

}

