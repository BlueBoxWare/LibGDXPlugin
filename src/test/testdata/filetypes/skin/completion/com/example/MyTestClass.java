package com.example;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag;


@GDXTag({"javaTag1", "javaTag2"})
class MyTestClass {

    int number;
    boolean bool;
    boolean[] bools;
    String name;

    Color[] colors;
    Color[][] moreColors;

    MyTestClass[] m;

    TextButton.TextButtonStyle textButtonStyle;
    TextButton.TextButtonStyle[] textButtonStyles;

    public static class Inner {
        int innerField;
    }

    private static class InnerClass {
        static class MyInnerStyle extends List.ListStyle {

        }
    }

    class NonStatic {}

}