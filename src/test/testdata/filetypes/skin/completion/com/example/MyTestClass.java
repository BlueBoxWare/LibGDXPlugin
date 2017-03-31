package com.example;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.graphics.Color;

class MyTestClass {

    int number;
    String name;

    Color[] colors;
    Color[][] moreColors;

    public class Inner {
        int innerField;
    }

    public class InnerClass {
        public class MyInnerStyle extends List.ListStyle {

        }
    }

}