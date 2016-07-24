package main;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.Iterator;

class OrderedMapTest {

  OrderedMap<String, Object> map = new OrderedMap<String, Object>();
  OrderedMap<String, OrderedMap<String, Object>> map2 = new OrderedMap<String, OrderedMap<String, Object>>();

  void test() {
    map2.put("abc", map);

    for (ObjectMap.Entry<String, OrderedMap<String, Object>> e: <warning>map2</warning>) {
      for (ObjectMap.Entry<String, Object> e1: <warning>e.value</warning>) {

      }
    }
  }

  Iterator i1 = <warning>map.keys()</warning>;
  Iterator i2 = <warning>map.values()</warning>;
  Iterator i3 = <warning>map.iterator()</warning>;
  Iterator i4 = <warning>map.entries()</warning>;

  Object o = <warning><warning>map2.values()</warning>.next().values()</warning>;

}
