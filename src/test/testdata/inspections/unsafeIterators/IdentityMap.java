package main;

import com.badlogic.gdx.utils.IdentityMap;

import java.util.Iterator;

class IdentityMapTest {

  private IdentityMap<String, Object> map1 = new IdentityMap<String, Object>();

  void test() {

    for (String s: <warning>map1.keys()</warning>) {
      System.out.println(s);
    }

    <warning>map1.keys()</warning>.iterator();

    Iterator iterator1 = <warning>map1.iterator()</warning>;
    Iterator iterator2 = <warning>map1.values()</warning>;
    IdentityMap.Keys<String> iterator3 = <warning>map1.keys()</warning>;
    Iterator iterator4 = <warning>map1.entries()</warning>;

    for (IdentityMap.Entry e: <warning>map1</warning>) {
      System.out.println(e.key);
    }

  }

}
