package com.example;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag;

@GDXTag({"javaTag1", "javaTag2"})
public class ColorArrayHolder {

  public Color[] colors;
  public Color[][] moreColors;

  public TextButton.TextButtonStyle textButtonStyle;
  TextButton.TextButtonStyle[] textButtonStyles;
  Button.ButtonStyle buttonStyle;

  boolean c;
  byte aByte;

  public int i;
  public float f;

  ColorArrayHolder[] m;

  public Skin.TintedDrawable[] tds;

  public int[] ints;

  class NonStaticInner { }

  static class StaticInner {}

  void t() {
    new com.badlogic.gdx.utils.Json().addClassTag("Tag1", com.badlogic.gdx.graphics.Color.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tag2", com.example.KColorArrayHolder.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tag4", com.example.ColorArrayHolder.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tag5", com.example.KColorArrayHolder.class)

    new com.badlogic.gdx.utils.Json().addClassTag("Tag6", java.lang.Integer.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tag7", java.lang.Boolean.class)
  }

}
