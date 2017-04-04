package com.example;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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

    public class Inner {
        int innerField;
    }

    public class InnerClass {
        public class MyInnerStyle extends List.ListStyle {

        }
    }

}