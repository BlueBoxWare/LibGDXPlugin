package main;

import com.badlogic.gdx.utils.ObjectFloatMap;

import java.util.Iterator;

class ObjectFloatMapTest {

  void test() {

    ObjectFloatMap<String> ofm = new ObjectFloatMap();

    for (ObjectFloatMap.Entry<String> e: <warning>ofm</warning>) {
      System.out.println(e.key);
    }

    for (ObjectFloatMap.Entry<String> e: new ObjectFloatMap.Entries<String>(ofm)) {
      System.out.println(e.key);
    }

    Iterator i1 = <warning>ofm.keys()</warning>;
    Iterator i2 = <warning>ofm.entries()</warning>;
    ObjectFloatMap.Values i3 = <warning>ofm.values()</warning>;
    Iterator i4 = <warning>ofm.entries()</warning>;

  }

}
