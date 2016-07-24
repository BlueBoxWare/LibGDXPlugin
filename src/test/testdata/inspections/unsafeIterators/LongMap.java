package main;

import com.badlogic.gdx.utils.LongMap;

class LongMapTest {

  private LongMap<String> map = new LongMap();

  void init() {
    map.put(1L, "One");
    map.put(2L, "Two");
    map.put(3L, "Three");
  }

  void print() {
    for (LongMap.Entry e: <warning>map</warning>) {
      System.out.println(e.value);
    }
  }

  void print2() {
    for (LongMap.Entry<String> e: new LongMap.Entries<String>(map)) {
      System.out.println(e.value);
    }
  }

  void test() {
    long[] i = <warning>map.keys()</warning>.toArray().toArray();
    LongMap map2 = map;
    Object o = map2;
    <warning>((LongMap)o).entries()</warning>;
    <warning>((LongMap)o).iterator()</warning>;
    <warning>((LongMap)o).values()</warning>;
  }

}