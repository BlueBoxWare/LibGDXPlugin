package main

import com.badlogic.gdx.utils.ObjectSet

@Suppress("UNUSED_PARAMETER")
class ObjectSetTest {

  var set = ObjectSet<ObjectSet<String>>()

  fun test() {
    for (e in <warning>set</warning>) {
      for (s in <warning>e</warning>) {

    }
    }

    <warning>set.iterator()</warning>
    <warning><warning>set.iterator()</warning>.next().iterator()</warning>
  }


}

