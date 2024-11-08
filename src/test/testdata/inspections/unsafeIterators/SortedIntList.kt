package main

import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.SortedIntList

class SortedIntListTestKotlin {

  fun test() {
    val list = SortedIntList<ObjectSet<String>>()

    for (n in <warning>list</warning>) {
      for (s in <warning>n.value</warning>) {

    }
    }

    <warning><warning>list.iterator()</warning>.next().value.iterator()</warning>.next()

    list.let {
      <warning>it.iterator()</warning>
    }
  }

}
