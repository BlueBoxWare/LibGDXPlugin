package main

import com.badlogic.gdx.utils.LongMap

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
internal class LongMapTestKotlin {

  private val map = LongMap<String>()

  fun init() {
    map.put(1L, "One")
    map.put(2L, "Two")
    map.put(3L, "Three")
  }

  fun print() {
    for (e in <warning>map</warning>) {
      println(e.value)
    }
  }

  fun print2() {
    for (e in LongMap.Entries<String>(map)) {
      println(e.value)
    }
  }

  fun test() {
    val i = <warning>map.keys()</warning>.toArray().toArray()
    val map2 = map
    val o: Any = map2
    <warning>(o as LongMap<String>).entries()</warning>
    <warning>(o as LongMap<String>).iterator()</warning>
    <warning>(o as LongMap<String>).values()</warning>

    val map3 = if (true==false) map2 else null

    map3?.let {
      <warning>it.keys()</warning>
      it.let { bla ->
        <warning>bla.values()</warning>
      }
    }
  }

}
