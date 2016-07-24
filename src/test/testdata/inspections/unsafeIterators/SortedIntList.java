package main;

import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.SortedIntList;

class SortedIntListTest {

  void test() {
    SortedIntList<ObjectSet<String>> list = new SortedIntList<ObjectSet<String>>();

    for (SortedIntList.Node<ObjectSet<String>> n: <warning>list</warning>) {
      for (String s: <warning>n.value</warning>) {

      }
    }

    <warning><warning>list.iterator()</warning>.next().value.iterator()</warning>.next();
  }

}
