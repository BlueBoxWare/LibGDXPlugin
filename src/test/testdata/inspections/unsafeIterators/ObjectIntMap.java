package main;

import com.badlogic.gdx.utils.ObjectIntMap;

import java.util.Iterator;

class ObjectIntMapTest {

  void test() {

    ObjectIntMap<String> ofm = new ObjectIntMap();

    for (ObjectIntMap.Entry<String> e: <warning>ofm</warning>) {
      System.out.println(e.key);
    }

    for (ObjectIntMap.Entry<String> e: new ObjectIntMap.Entries<String>(ofm)) {
      System.out.println(e.key);
    }

    Iterator i1 = <warning>ofm.keys()</warning>;
    Iterator i2 = <warning>ofm.entries()</warning>;
    ObjectIntMap.Values i3 = <warning>ofm.values()</warning>;
    Iterator i4 = <warning>ofm.entries()</warning>;

  }

}
