package main;

import com.badlogic.gdx.Preferences;

class Test {

  Preferences preferences;

  void fun1() {
    preferences.putBoolean("fsd", true);
    for (int i: new int[]{1,2,3}) {
      preferences.putInteger("fsd", i);
    }
    if (true) {
      preferences.flush();
    }
  }

  void fun2() {
    for (int i: new int[]{1,2,3}) {
      preferences.putInteger("fsd", i);
    }
    preferences.flush();
  }

  void fun3() {
    preferences.remove("a");
    <warning>preferences.remove("b")</warning>;
  }

  void fun4() {
    class C {
      void fun4() {
        preferences.remove("a");
        <warning>preferences.remove("b")</warning>;
      }
    }

    preferences.flush();
  }



}
