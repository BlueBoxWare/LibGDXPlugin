import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Predicate;

class ArrayTest {

  class Pred implements Predicate<String> {
    public boolean evaluate(String arg0) {
      return false;
    }
  }

  void test() {
    Array<String> strings = new Array<String>();

    Iterator iterator1 = <warning>strings.iterator()</warning>;
    Iterable iterator2 = <warning>strings.select(new Pred())</warning>;

    for (String s: <warning>strings</warning>) {
      System.out.println(s);
    }
  }

}