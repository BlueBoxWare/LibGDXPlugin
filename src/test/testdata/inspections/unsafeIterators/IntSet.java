package main;

import com.badlogic.gdx.utils.IntSet;

class IntSetTest {

  void test() {
    new IntSet().iterator();

    IntSet set = new IntSet();

    IntSet.IntSetIterator i =  <warning>set.iterator()</warning>;

    for (int x: <warning>set.iterator()</warning>.toArray().toArray()) {

    }


  }

}
