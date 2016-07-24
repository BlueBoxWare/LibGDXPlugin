package main

import com.badlogic.gdx.utils.IdentityMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST")
class IdentityMapTest {

  var map1 = IdentityMap<String, Any>()

  fun test() {

    for (s in <warning>map1.keys()</warning>) {
      println(s)
    }

    <warning>map1.keys()</warning>.iterator()

    val iterator1 = <warning>map1.iterator()</warning>
    val iterator2 = <warning>map1.values()</warning>
    val iterator3 = <warning>map1.keys()</warning>
    val iterator4 = <warning>map1.entries()</warning>

    for (e in <warning>map1</warning>) {
      println(e.key)
    }

    <warning>(if (true) IdentityMap<String, String>() else IdentityMap<String, Any>()).keys()</warning>

  }

}
