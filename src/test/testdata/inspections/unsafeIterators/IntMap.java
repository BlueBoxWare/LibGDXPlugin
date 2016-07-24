package main;

import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.IntMap;

class IntMapTest {

  private IntMap<String> map = new IntMap();

  void init() {
    map.put(1, "One");
    map.put(2, "Two");
    map.put(3, "Three");
  }

  void print() {
    for (IntMap.Entry e: <warning>map</warning>) {
      System.out.println(e.value);
    }
  }

  void print2() {
    for (IntMap.Entry<String> e: new IntMap.Entries<String>(map)) {
      System.out.println(e.value);
    }
  }

  void test() {
    int[] i = <warning>map.keys()</warning>.toArray().toArray();
    IntMap map2 = map;
    Object o = map2;
    <warning>((IntMap)o).entries()</warning>;
    <warning>((IntMap)o).iterator()</warning>;
    <warning>((IntMap)o).values()</warning>;
  }

}
