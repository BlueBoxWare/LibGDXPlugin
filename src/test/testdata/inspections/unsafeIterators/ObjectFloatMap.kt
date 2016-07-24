package main

import com.badlogic.gdx.utils.ObjectFloatMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class ObjectFloatMapTest {

  var ofm2: ObjectFloatMap<String>? = null

  fun test() {

    val ofm = ObjectFloatMap<Any>()


    for (e in <warning>ofm</warning>) {
      println(e.key)
    }

    for (e in ObjectFloatMap.Entries<Any>(ofm)) {
      println(e.key)
    }

    val i1 = <warning>ofm.keys()</warning>
    val i2 = <warning>ofm.entries()</warning>
    val i3 = <warning>ofm.values()</warning>
    val i4 = <warning>ofm.entries()</warning>

    <warning>ofm2?.keys()</warning>

    <warning>(ofm2 ?: ObjectFloatMap()).values()</warning>

  }

}

