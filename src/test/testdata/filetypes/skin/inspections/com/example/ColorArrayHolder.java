package com.example;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ColorArrayHolder {

  public Color[] colors;
  public Color[][] moreColors;

  public TextButton.TextButtonStyle textButtonStyle;
  TextButton.TextButtonStyle[] textButtonStyles;

  boolean c;
  byte aByte;

  public int i;
  public float f;

  ColorArrayHolder[] m;

  public Skin.TintedDrawable[] tds;

  public int[] ints;

  class NonStaticInner { }

  static class StaticInner {}

}
