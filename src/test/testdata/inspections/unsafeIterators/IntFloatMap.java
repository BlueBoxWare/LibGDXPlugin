package main;

import com.badlogic.gdx.utils.IntFloatMap;

import java.util.Iterator;

class IntFloatMapTest {

  private IntFloatMap map = new IntFloatMap();

  void test() {

    <warning>map.keys()</warning>.hasNext();
    <warning>map.entries()</warning>.iterator();

    IntFloatMap map2 = map;

    Iterator iterator1 = <warning>map.iterator()</warning>;
    IntFloatMap.Keys keys = <warning>map2.keys()</warning>;
    IntFloatMap.Entries entries = <warning>map2.entries()</warning>;
    IntFloatMap.Values values = <warning>map2.values()</warning>;

    IntFloatMap.Keys keys2 = new IntFloatMap.Keys(map2);

  }

}
