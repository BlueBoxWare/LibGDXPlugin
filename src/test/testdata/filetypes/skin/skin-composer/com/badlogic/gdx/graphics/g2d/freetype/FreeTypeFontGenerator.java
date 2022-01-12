package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;

/*
 * Copyright 2022 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FreeTypeFontGenerator {
    public static class FreeTypeFontParameter {
        /** The size in pixels */
        public int size = 16;
        /** If true, font smoothing is disabled. */
        public boolean mono;
        /** Foreground color (required for non-black borders) */
        public Color color = Color.WHITE;
        /** Glyph gamma. Values > 1 reduce antialiasing. */
        public float gamma = 1.8f;
        /** Number of times to render the glyph. Useful with a shadow or border, so it doesn't show through the glyph. */
        public int renderCount = 2;
        /** Border width in pixels, 0 to disable */
        public float borderWidth = 0;
        /** Border color; only used if borderWidth > 0 */
        public Color borderColor = Color.BLACK;
        /** true for straight (mitered), false for rounded borders */
        public boolean borderStraight = false;
        /** Values < 1 increase the border size. */
        public float borderGamma = 1.8f;
        /** Offset of text shadow on X axis in pixels, 0 to disable */
        public int shadowOffsetX = 0;
        /** Offset of text shadow on Y axis in pixels, 0 to disable */
        public int shadowOffsetY = 0;
        /** Shadow color; only used if shadowOffset > 0. If alpha component is 0, no shadow is drawn but characters are still offset
         * by shadowOffset. */
        public Color shadowColor = new Color(0, 0, 0, 0.75f);
        /** Pixels to add to glyph spacing when text is rendered. Can be negative. */
        public int spaceX, spaceY;
        /** Pixels to add to the glyph in the texture. Cannot be negative. */
        public int padTop, padLeft, padBottom, padRight;
        /** The characters the font should contain. If '\0' is not included then {@link BitmapFont.BitmapFontData#missingGlyph} is not set. */
        public String characters = "";
        /** Whether the font should include kerning */
        public boolean kerning = true;
        /** The optional PixmapPacker to use for packing multiple fonts into a single texture.
         * @see FreeTypeFontParameter */
        public PixmapPacker packer = null;
        /** Whether to flip the font vertically */
        public boolean flip = false;
        /** Whether to generate mip maps for the resulting texture */
        public boolean genMipMaps = false;
        /** Minification filter */
        public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
        /** Magnification filter */
        public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;

        public boolean incremental;
    }
}
