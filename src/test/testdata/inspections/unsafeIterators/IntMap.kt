package main

import com.badlogic.gdx.utils.IntIntMap
import com.badlogic.gdx.utils.IntMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
internal class IntMapTestKotlin {

  private val map = IntMap<String>()

  fun init() {
    map.put(1, "One")
    map.put(2, "Two")
    map.put(3, "Three")
  }

  fun print() {
    for (e in <warning>map</warning>) {
      println(e.value)
    }
  }

  fun print2() {
    for (e in IntMap.Entries<String>(map)) {
      println(e.value)
    }
  }

  fun test() {
    val i = <warning>map.keys()</warning>.toArray().toArray()
    val map2 = map
    val o: Any = map2
    <warning>(o as IntMap<String>).entries()</warning>
    <warning>(o as IntMap<String>).iterator()</warning>
    <warning>(o as IntMap<String>).values()</warning>

    val map3 = if (true==false) map2 else null

    map3?.let {
      <warning>it.keys()</warning>
      it.let { bla ->
        <warning>bla.values()</warning>
      }
    }
  }

}
