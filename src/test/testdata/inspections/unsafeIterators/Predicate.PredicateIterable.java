package main;

import com.badlogic.gdx.utils.Predicate;

import java.util.Iterator;

class MyPredicate implements Predicate {
  public boolean evaluate(Object arg0) {
    return true;
  }
}

class PredicateIterableTest {

  void test() {
    Predicate.PredicateIterable pi = new Predicate.PredicateIterable(new Iterable() {
      public Iterator iterator() {
        return null;
      }
    }, new MyPredicate());

    for (Object o: <warning>pi</warning>) {
      Iterator i = <warning>pi.iterator()</warning>;
    }
  }

}
