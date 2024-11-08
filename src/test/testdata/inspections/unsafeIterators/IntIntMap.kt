package main

import com.badlogic.gdx.utils.IntIntMap


@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST")
internal class IntIntMapTestKotlin {

  private val map2 = IntIntMap(map1)

  fun foo() {


    val e = <warning>map1.entries()</warning>
    val i = e.iterator()
    <warning>map2.keys()</warning>.toArray()
    val i2 = <warning>map2.values()</warning>

    for (entry in <warning>map2.entries()</warning>) {
      for (v in IntIntMap.Keys(map1).toArray().toArray()) {
        IntIntMap.Values(map2)
      }
    }

    <warning>IntIntMap(map2).entries()</warning>

    val a = <warning>(if (map2.size > 1) map1 else IntIntMap()).keys()</warning>
    val b = IntIntMap.Entries(map1)
  }

  companion object {

    private val map1 = IntIntMap()

    fun test() {
      for (e in <warning>map1</warning>) {
        map1.remove(e.key, e.value)
      }
    }
  }



}
