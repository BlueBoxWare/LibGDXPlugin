package main;

import com.badlogic.gdx.utils.IntIntMap;

import java.util.Iterator;

class IntIntMapTest {

  private static IntIntMap map1 = new IntIntMap();

  private IntIntMap map2 = new IntIntMap(map1);

  static void test() {
    for (IntIntMap.Entry e: <warning>map1</warning>) {
      map1.remove(e.key, e.value);
    }
  }

  void foo() {


    IntIntMap.Entries e = <warning>map1.entries()</warning>;
    Iterator i = e.iterator();
    <warning>map2.keys()</warning>.toArray();
    IntIntMap.Values i2 = <warning>map2.values()</warning>;

    for (IntIntMap.Entry entry: <warning>map2.entries()</warning>) {
      for (int v: new IntIntMap.Keys(map1).toArray().toArray()) {
        new IntIntMap.Values(map2);
      }
    }

    new IntIntMap(map2).entries();
  }

}
