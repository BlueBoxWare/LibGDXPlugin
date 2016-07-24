package main;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.PerformanceCounter;

class Test {

  PerformanceCounter pc = new PerformanceCounter("yes");

  void test1() {

    <warning>new FPSLogger().log()</warning>;

    FPSLogger p = new FPSLogger();

    <warning>p.log()</warning>;

    class Clazz {
      FPSLogger p = new FPSLogger();

      Clazz() {
        <warning>GLProfiler.enable()</warning>;
      }
    }

    <warning>new Clazz().p.log()</warning>;

  }

  void test2() {

    <warning>new PerformanceCounter("no").start()</warning>;

    <warning>pc.reset()</warning>;

    PerformanceCounter pc2 = pc;

    <warning>pc.stop()</warning>;

  }

  void test3(PerformanceCounter p) {
    <warning>p.tick()</warning>;

    <warning>GLProfiler.enable()</warning>;
  }


}
