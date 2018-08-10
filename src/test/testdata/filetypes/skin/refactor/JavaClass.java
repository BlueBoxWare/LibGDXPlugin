package com.example;

import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag;

@GDXTag({"tag1", "tag2"})
private class JavaC<caret>lass {
  public boolean bool;

  @GDXTag({"javainner"})
  static class InnerClass {
    static class InnerInnerClass
  }

  private static class Private {

  }

  class NonStatic {}

  void m() {
    new com.badlogic.gdx.utils.Json().addClassTag("JC", com.example.JavaClass.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Inner", com.example.JavaClass.InnerClass.class)
  }

}