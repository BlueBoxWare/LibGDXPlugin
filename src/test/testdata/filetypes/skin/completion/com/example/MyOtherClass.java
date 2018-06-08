package com.example;

class MyOtherClass {

  void t() {
    new com.badlogic.gdx.utils.Json().addClassTag("tag2", com.example.KotlinClass.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tinted", com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable.class)
    new com.badlogic.gdx.utils.Json().addClassTag("BM", com.badlogic.gdx.graphics.g2d.BitmapFont.class)
    new com.badlogic.gdx.utils.Json().addClassTag("TagInner", com.example.MyTestClass.Inner.class)
  }

}