package main;

import com.badlogic.gdx.utils.OrderedSet;

import java.util.Iterator;

class OrderedSetTest {

  void test() {
    OrderedSet<OrderedSet<String>> set = new OrderedSet<OrderedSet<String>>();

    for (OrderedSet<String> s: <warning>set</warning>) {
      for (String str: <warning>s</warning>) {

      }
    }

    <warning><warning><warning>set.iterator()</warning>.next().iterator()</warning>.toArray().iterator()</warning>;


  }



}
