package main

import com.badlogic.gdx.utils.ObjectSet
import com.badlogic.gdx.utils.Predicate

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class MyPredicate : Predicate<Any> {
  override fun evaluate(arg0: Any): Boolean {
    return true
  }
}

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
class PredicateIterableTest {

  fun test() {
    val pi = Predicate.PredicateIterable(object : Iterable<Any> {
      override operator fun iterator(): Iterator<String> {
        return <warning>ObjectSet<String>().iterator()</warning>
      }
    }, MyPredicate())

    for (o in <warning>pi</warning>) {
      val i = <warning>pi.iterator()</warning>
    }

  }

}
