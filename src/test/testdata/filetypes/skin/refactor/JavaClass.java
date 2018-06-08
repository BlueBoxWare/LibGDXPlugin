package com.example;

private class JavaC<caret>lass {
  public boolean bool;

  static class InnerClass {
    static class InnerInnerClass
  }

  private static class Private {

  }

  class NonStatic {}

  void m() {
    new com.badlogic.gdx.utils.Json().addClassTag("JC", com.example.JavaClass.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Inner", com.example.JavaClass.class)
  }

}