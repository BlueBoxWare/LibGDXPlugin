package main

import com.badlogic.gdx.utils.ObjectIntMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class ObjectIntMapTest {

  var ofm2: ObjectIntMap<String>? = null

  fun test() {

    val ofm = ObjectIntMap<Any>()


    for (e in <warning>ofm</warning>) {
      println(e.key)
    }

    for (e in ObjectIntMap.Entries<Any>(ofm)) {
      println(e.key)
    }

    val i1 = <warning>ofm.keys()</warning>
    val i2 = <warning>ofm.entries()</warning>
    val i3 = <warning>ofm.values()</warning>
    val i4 = <warning>ofm.entries()</warning>

    <warning>ofm2?.keys()</warning>

    <warning>(ofm2 ?: ObjectIntMap()).values()</warning>

  }

}

