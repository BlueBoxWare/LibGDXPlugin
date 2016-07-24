package main;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

class ArrayMapTest {

  ArrayMap<String, Object> map = new ArrayMap();

  void test() {

    map.put("a", new ArrayMap<String, String>().values());
    map.put("b", new ArrayMap<String, String>());

    Iterator<String> iterator1 = <warning>map.keys()</warning>;
    Iterator<Object> iterator2 = <warning>map.values()</warning>;
    Iterator<ObjectMap.Entry<String, Object>> iterator3 = <warning>map.iterator()</warning>;
    Iterator iterator4 = <warning>map.entries()</warning>;
    Iterator iterator5 = <warning>((ArrayMap)map.get("b")).values()</warning>;

    for (ObjectMap.Entry e: <warning>map</warning>) {
      System.out.println(e.key);
    }

  }

}
