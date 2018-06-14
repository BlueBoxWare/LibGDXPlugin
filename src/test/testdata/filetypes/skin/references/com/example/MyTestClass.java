package com.example;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


class MyTestClass {

    int number;
    String name;
    com.badlogic.gdx.graphics.Color color;
    com.example.MyTestClass testClass;

    Color[] colors;
    Color[][] moreColors;

    MyTestClass[] m;

    TextButton.TextButtonStyle[] textButtonStyle;

    public class Inner {
        int innerField;

        void t() {
            new com.badlogic.gdx.utils.Json().addClassTag("tag2", com.example.KTestClass.class);
            new com.badlogic.gdx.utils.Json().addClassTag("tag4", com.example.MyTestClass.class);
            new com.badlogic.gdx.utils.Json().addClassTag("tag4", com.example.KTestClass.class);
            new com.badlogic.gdx.utils.Json().addClassTag("Tinted", com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable.class);
            new com.badlogic.gdx.utils.Json().addClassTag("BM", com.badlogic.gdx.graphics.g2d.BitmapFont.class);
            new com.badlogic.gdx.utils.Json().addClassTag("CBS", com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle.class);
        }

    }

}