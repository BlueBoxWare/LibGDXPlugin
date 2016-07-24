package main;

import com.badlogic.gdx.utils.Queue;

import java.util.Iterator;

class QueueTest {

  Queue<String> q = new Queue<String>();

  void f(Queue s) {
    <warning>s.iterator()</warning>;
  }

  void g(Iterator i) {
    i.next();
  }

  void test() {

    for (String s: <warning>q</warning>) {

    }

    f(q);
    g(<warning>q.iterator()</warning>);
  }

}
