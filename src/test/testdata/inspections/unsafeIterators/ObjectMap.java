package main;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

class ObjectMapTest {

  void test() {
    ObjectMap<ObjectMap<String, ObjectMap<Object, Object>>, Object> map1 = new ObjectMap<ObjectMap<String, ObjectMap<Object, Object>>, Object>();
    ObjectMap<String, Object> map2 = new ObjectMap<String, Object>();

    for (ObjectMap.Entry<ObjectMap<String, ObjectMap<Object, Object>>, Object> m: <warning>map1</warning>) {
      for (ObjectMap.Entry<String, ObjectMap<Object, Object>> e: <warning>m.key</warning>) {
        for (Object p: <warning>e.value</warning>) {

        }
      }
    }

    Iterator i = <warning>map2.keys()</warning>;
    Iterator i2 = <warning>map2.values()</warning>;
    Iterator i3 = <warning>map2.entries()</warning>;
    Iterator i4 = <warning>map2.iterator()</warning>;
  }

}
