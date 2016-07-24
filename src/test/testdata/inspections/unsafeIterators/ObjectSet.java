package main;

import com.badlogic.gdx.utils.ObjectSet;

class ObjectSetTest {

  ObjectSet<ObjectSet<String>> set = new ObjectSet<ObjectSet<String>>();

  void test() {
    for (ObjectSet<String> e: <warning>set</warning>) {
      for (String s: <warning>e</warning>) {

      }
    }

    <warning>set.iterator()</warning>;
    <warning><warning>set.iterator()</warning>.next().iterator()</warning>;
  }


}