package main

import com.badlogic.gdx.utils.IntSet

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class IntSetTest {

  fun test() {
    <warning>IntSet().iterator()</warning>

    val set = IntSet()

    val i = <warning>set.iterator()</warning>

    for (x in <warning>set.iterator()</warning>.toArray().toArray()) {

    }


  }

}

