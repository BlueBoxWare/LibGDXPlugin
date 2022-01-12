/*
 * The MIT License
 *
 * Copyright (c) 2021 Raymond Buckley.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ray3k.skincomposer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Raymond
 */
public class NinePatchWidget extends Stack {

    public static class NinePatchWidgetStyle {
        public Drawable lightTile;
        public Drawable darkTile;
        public Drawable paddingHandle;
        public Drawable paddingHandleOver;
        public Drawable paddingHandlePressed;
        public Drawable contentHandle;
        public Drawable contentHandleOver;
        public Drawable contentHandlePressed;
        public Drawable border;
        public Drawable switchOn;
        public Drawable switchOnOver;
        public Drawable switchOff;
        public Drawable switchOffOver;
        public Drawable black;
        public Drawable gridLight;
        public Drawable gridDark;
        public Drawable zoomedOutPaddingHandle;
        public Drawable zoomedOutContentHandle;
    }
    
}

