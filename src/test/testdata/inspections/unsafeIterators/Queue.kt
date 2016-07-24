package main

import com.badlogic.gdx.utils.Queue

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class QueueTest {

  var q = Queue<String>()

  fun f(s: Queue<String>) {
    <warning>s.iterator()</warning>
  }

  fun g(i: Iterator<Any>) {
    i.next()
  }

  fun test() {

    for (s in <warning>q</warning>) {

    }

    f(q)
    g(<warning>q.iterator()</warning>)
  }

}

