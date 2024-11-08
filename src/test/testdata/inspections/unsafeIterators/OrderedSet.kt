package main

import com.badlogic.gdx.utils.OrderedSet

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class OrderedSetTestKotlin {

  fun test() {
    val set = OrderedSet<OrderedSet<String>>()

    for (s in <warning>set</warning>) {
      for (str in <warning>s</warning>) {

    }
    }

    <warning><warning><warning>set.iterator()</warning>.next().iterator()</warning>.toArray().iterator()</warning>

    <warning>set.orderedItems().iterator()</warning>
  }


}

