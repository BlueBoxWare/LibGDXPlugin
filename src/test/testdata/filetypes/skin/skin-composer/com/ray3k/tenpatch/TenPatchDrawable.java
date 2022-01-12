/*
 * The MIT License
 *
 * Copyright 2019 Raymond Buckley.
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
package com.ray3k.tenpatch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;

/**
 * TenPatchDrawable is an alternative to libGDX's implementation of 9-patch. The
 * NinePatch class is limited in functionality: it can only define one
 * stretchable area per axis. Real 9-patches allow for multiple stretch areas as
 * seen in the Android OS. TenPatch addresses this issue by allowing users to
 * pass in multiple values for stretch areas. Stretching is then divided among
 * the multiple patches based on their ratio. It also adds a tiling option,
 * increasing functionality in UI across the board.
 * 
 * Unfortunately, the NinePatch class is deep-rooted in libGDX. Full replacement
 * would require modifications to TextureAtlas, TexturePacker, and more. Thus,
 * TenPatchDrawable will not be directly loaded from a TextureAtlas or from a
 * 9-patch image file. Typical use would require loading from a Skin JSON.
 * Specifying stretch regions and options will be made simple by using the
 * associated editor in Skin Composer.
 * 
 * @author Raymond Buckley
 * @see <a href="https://github.com/raeleus/skin-composer">Skin Composer</a>
 */
public class TenPatchDrawable extends TextureRegionDrawable {
    static private final Color temp = new Color();
    private Color color = new Color(1, 1, 1, 1);
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    public int[] horizontalStretchAreas;
    public int[] verticalStretchAreas;
    public boolean tiling;
    public float offsetX;
    public float offsetY;
    public float offsetXspeed;
    public float offsetYspeed;
    public float time;
    private final float[] verts = new float[20];
    private Array<TextureRegion> regions;
    private float frameDuration;
    private boolean autoUpdate = true;
    public int playMode = PlayMode.LOOP;
    public float scaleX = 1f, scaleY = 1f;
    public int crushMode = CrushMode.SHRINK;
    
    /**
     * The strategies used when the drawable is forced to scale below the minimum size.
     */
    public static class CrushMode {
        /**
         * When the drawable is forced to scale below the minimum
         * size, it will shrink everything to accommodate the new
         * bounds.
         */
        public static final int SHRINK = 0;
        
        /**
         * When the drawable is forced to scale below the minimum
         * size, it will not shrink. Anything drawn beyond the
         * bounds will be cropped on the right and top.
         */
        public static final int CROP = 1;
    
        /**
         * When the drawable is forced to scale below the minimum
         * size, it will not shrink. Anything drawn beyond the
         * bound will be cropped on the left and bottom.
         */
        public static final int CROP_REVERSED = 2;
        
        /**
         * When the drawable is forced to scale below the minimum
         * size, it will overflow it's bounds.
         */
        public static final int NONE = 3;
    }
    
    public static class PlayMode {
        public static final int NORMAL = 0, REVERSED = 1, LOOP = 2, LOOP_REVERSED = 3, LOOP_PINGPONG = 4, LOOP_RANDOM = 5;
    }
    public static RandomXS128 randomXS128 = new RandomXS128();
    public transient int seed = MathUtils.random(100);
    
    /**
     * No-argument constructor necessary for loading via JSON.
     * horizontalStretchAreas and verticalStretchAreas must be set before this
     * drawable is drawn.
     * @see TenPatchDrawable#setHorizontalStretchAreas(int[])
     * @see TenPatchDrawable#setVerticalStretchAreas(int[])
     */
    public TenPatchDrawable() {
    
    }
    
    /**
     * Create a duplicate TenPatchDrawable.
     * @param other 
     */
    public TenPatchDrawable(TenPatchDrawable other) {
        set(other);
    }
    
    /**
     * Creates a TenPatchDrawable. All stretch values must be defined in pairs.
     * @param horizontalStretchAreas Pairs of values defining stretch areas from
     * the left of the graphic in ascending order in pixels. All values are
     * inclusive with 0 being the left-most pixel (i.e. (0,2) defines a 3 pixel
     * wide stretch area on the left of the graphic).
     * @param verticalStretchAreas Pairs of values defining stretch areas from
     * the bottom of the graphic in ascending order in pixels. All values are
     * inclusive with 0 being the bottom-most pixel (i.e. (0,2) defines a 3
     * pixel high stretch area on the bottom of the graphic).
     * @param tiling
     * @param region 
     */
    public TenPatchDrawable(int[] horizontalStretchAreas, int[] verticalStretchAreas, boolean tiling, TextureRegion region) {
        this.horizontalStretchAreas = horizontalStretchAreas;
        this.verticalStretchAreas = verticalStretchAreas;
        this.tiling = tiling;
        setRegion(region);
    }

    public void set(TenPatchDrawable other) {
        color = other.color;
        color1 = other.color1;
        color2 = other.color2;
        color3 = other.color3;
        color4 = other.color4;
        horizontalStretchAreas = other.horizontalStretchAreas == null ? null : Arrays.copyOf(other.horizontalStretchAreas, other.horizontalStretchAreas.length);
        verticalStretchAreas = other.verticalStretchAreas == null ? null : Arrays.copyOf(other.verticalStretchAreas, other.verticalStretchAreas.length);
        tiling = other.tiling;
        offsetX = other.offsetX;
        offsetY = other.offsetY;
        offsetXspeed = other.offsetXspeed;
        offsetYspeed = other.offsetYspeed;
        time = other.time;
        regions = regions == null ? null : new Array<TextureRegion>(other.regions);
        frameDuration = other.frameDuration;
        autoUpdate = other.autoUpdate;
        playMode = other.playMode;
        seed = other.seed;
        setMinWidth(other.getMinWidth());
        setMinHeight(other.getMinHeight());
        scaleX = other.scaleX;
        scaleY = other.scaleY;
        crushMode = other.crushMode;
        setRegion(other.getRegion());
    }

    public static class InvalidPatchException extends RuntimeException {
        
    }
    
    /**
     * Draws the TenPatch to the specified batch. The defined stretch areas
     * will adapt to the width and height by either tiling or stretching/shrinking.
     * Minimum width is defined as the total width of all the non-stretching
     * areas. If width is brought below this value, the non-stretching areas
     * will shrink to accommodate. The same applies to height.
     * @param batch
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        if (autoUpdate) {
            update(Gdx.graphics.getDeltaTime(), true);
        }
        
        float previousValue = 0;
        for (float value : horizontalStretchAreas) {
            if (value < previousValue || value >= getRegion().getRegionWidth()) {
                throw new InvalidPatchException();
            }
        }

        //properties from the texture region
        TextureRegion region = getRegion();
        Texture texture = region.getTexture();
        float w = region.getRegionWidth() * scaleX;
        float h = region.getRegionHeight() * scaleY;
        float u = region.getU();
        float u2 = region.getU2();
        float v = region.getV2();
        float v2 = region.getV();

        //values to pass to batch.draw()
        float drawWidth;
        float drawHeight;
        float drawU;
        float drawV;
        float drawU2;
        float drawV2;

        //calculated values
        float extraWidth = MathUtils.floor(width) - w;
        float extraHeight = MathUtils.floor(height) - h;
        float originX = 0f;
        float originY = 0f;

        float totalWidthStretch = 0f;
        for (int i = 0; i < horizontalStretchAreas.length; i += 2) {
            totalWidthStretch += horizontalStretchAreas[i + 1] - horizontalStretchAreas[i] + 1;
        }
        totalWidthStretch *= scaleX;
    
        //totalHeightStretch is the sum of all the vertical stretchable areas defined
        float totalHeightStretch = 0f;
        for (int i = 0; i < verticalStretchAreas.length; i += 2) {
            totalHeightStretch += verticalStretchAreas[i + 1] - verticalStretchAreas[i] + 1;
        }
        totalHeightStretch *= scaleY;

        int yIndex = 0;
        float texY1 = 0f;
        float cropAccumulatorY = 0;
        while (yIndex <= verticalStretchAreas.length) {
            float texY2 = yIndex < verticalStretchAreas.length ? verticalStretchAreas[yIndex] * scaleY : h;

            originX = 0;
            int xIndex = 0;
            float texX1 = 0;
            //row of vertically non-stretching pixels
            if (height > h - totalHeightStretch || crushMode == CrushMode.NONE) {
                drawHeight = texY2 - texY1;
            } else if (crushMode == CrushMode.CROP) {
                if (height > h - totalHeightStretch) {
                    drawHeight = texY2 - texY1;
                } else {
                    drawHeight = Math.min(texY2 - texY1, height - cropAccumulatorY);
                    cropAccumulatorY += drawHeight;
                }
            }  else if (crushMode == CrushMode.CROP_REVERSED) {
                if (height > h - totalHeightStretch) {
                    drawHeight = texY2 - texY1;
                } else {
                    float heightToMakeUp = h - totalHeightStretch - height - cropAccumulatorY;
                    drawHeight = texY2 - texY1 - heightToMakeUp;
                    drawHeight = MathUtils.clamp(drawHeight, 0, texY2 - texY1);
                    cropAccumulatorY += MathUtils.clamp(heightToMakeUp, 0, texY2 - texY1);
                }
            } else {
                drawHeight = (texY2 - texY1) * height / (h - totalHeightStretch);
            }
            drawHeight = Math.max(drawHeight, 0);
            float cropAccumulatorX = 0;
            while (xIndex <= horizontalStretchAreas.length) {
                //cell of horizontally non-stretching pixels
                float texX2 = xIndex < horizontalStretchAreas.length ? horizontalStretchAreas[xIndex] * scaleX : w;

                if (width > w - totalWidthStretch || crushMode == CrushMode.NONE) {
                    drawWidth = texX2 - texX1;
                } else if (crushMode == CrushMode.CROP) {
                    if (width > w - totalWidthStretch) {
                        drawWidth = texX2 - texX1;
                    } else {
                        drawWidth = Math.min(texX2 - texX1, width - cropAccumulatorX);
                        cropAccumulatorX += drawWidth;
                    }
                }  else if (crushMode == CrushMode.CROP_REVERSED) {
                    if (width > w - totalWidthStretch) {
                        drawWidth = texX2 - texX1;
                    } else {
                        float widthToMakeUp = w - totalWidthStretch - width - cropAccumulatorX;
                        drawWidth = texX2 - texX1 - widthToMakeUp;
                        drawWidth = MathUtils.clamp(drawWidth, 0, texX2 - texX1);
                        cropAccumulatorX += MathUtils.clamp(widthToMakeUp, 0, texX2 - texX1);
                    }
                } else {
                    drawWidth = (texX2 - texX1) * width / (w - totalWidthStretch);
                }
                drawWidth = Math.max(drawWidth, 0);
                if (crushMode == CrushMode.CROP) {
                    drawU = u + (u2 - u) * texX1 / w;
                    drawV = v + (v2 - v) * texY1 / h;
                    drawU2 = u + (u2 - u) * Math.min(texX1 + drawWidth, texX2) / w;
                    drawV2 = v + (v2 - v) * Math.min(texY1 + drawHeight, texY2) / h;
                } else if (crushMode == CrushMode.CROP_REVERSED) {
                    drawU = u + (u2 - u) * Math.max(texX2 - drawWidth, texX1) / w;
                    drawV = v + (v2 - v) * Math.max(texY2 - drawHeight, texY1) / h;
                    drawU2 = u + (u2 - u) * texX2 / w;
                    drawV2 = v + (v2 - v) * texY2 / h;
                } else {
                    drawU = u + (u2 - u) * texX1 / w;
                    drawV = v + (v2 - v) * texY1 / h;
                    drawU2 = u + (u2 - u) * texX2 / w;
                    drawV2 = v + (v2 - v) * texY2 / h;
                }
                drawPatches(batch, texture, x, y, originX, originY, drawWidth, drawHeight, drawU, drawV, drawU2, drawV2, texX1, texX2, texY1, texY2, true, true, false, false);

                originX += drawWidth;
                xIndex++;

                //cell of horizontally stretching pixels
                if (xIndex < horizontalStretchAreas.length) {
                    texX1 = texX2;
                    texX2 = (horizontalStretchAreas[xIndex] + 1) * scaleX;

                    drawWidth = texX2 - texX1 + extraWidth * (texX2 - texX1) / totalWidthStretch;
                    drawWidth = Math.max(drawWidth, 0);
                    drawU = u + (u2 - u) * texX1 / w;
                    drawU2 = u + (u2 - u) * texX2 / w;
                    if (crushMode == CrushMode.CROP_REVERSED) {
                        drawV = v + (v2 - v) * Math.max(texY1, texY2 - drawHeight) / h;
                        drawV2 = v + (v2 - v) * texY2 / h;
                    } else {
                        drawV = v + (v2 - v) * texY1 / h;
                        drawV2 = v + (v2 - v) * Math.min(texY1 + drawHeight, texY2) / h;
                    }
                    
                    if (texture.getMagFilter() == Texture.TextureFilter.Linear || texture.getMinFilter() == Texture.TextureFilter.Linear) {
                        drawU += .5f /texture.getWidth();
                        drawU2 -= .5f /texture.getWidth();
                    }
                    
                    drawPatches(batch, texture, x, y, originX, originY, drawWidth, drawHeight, drawU, drawV, drawU2, drawV2, texX1, texX2, texY1, texY2, false, true, true, false);

                    originX += drawWidth;
                    xIndex++;
                }
                texX1 = texX2;
            }
            originY += drawHeight;
            yIndex++;

            if (yIndex < verticalStretchAreas.length) {
                texY1 = texY2;
                texY2 = (verticalStretchAreas[yIndex] + 1) * scaleY;
                xIndex = 0;
                texX1 = 0;
                originX = 0;
                //row of vertically stretching cells
                drawHeight = texY2 - texY1 + extraHeight * (texY2 - texY1) / totalHeightStretch;
                drawHeight = Math.max(drawHeight, 0);
                cropAccumulatorX = 0;
                while (xIndex <= horizontalStretchAreas.length) {
                    float texX2 = xIndex < horizontalStretchAreas.length ? horizontalStretchAreas[xIndex] * scaleX : w;

                    //cell of horizontally non-stretching pixels
                    if (width > w -totalWidthStretch || crushMode == CrushMode.NONE) {
                        drawWidth = texX2 - texX1;
                    } else if (crushMode == CrushMode.CROP) {
                        if (width > w - totalWidthStretch) {
                            drawWidth = texX2 - texX1;
                        } else {
                            drawWidth = Math.min(texX2 - texX1, width - cropAccumulatorX);
                            cropAccumulatorX += drawWidth;
                        }
                    }  else if (crushMode == CrushMode.CROP_REVERSED) {
                        if (width > w - totalWidthStretch) {
                            drawWidth = texX2 - texX1;
                        } else {
                            float widthToMakeUp = w - totalWidthStretch - width - cropAccumulatorX;
                            drawWidth = texX2 - texX1 - widthToMakeUp;
                            drawWidth = MathUtils.clamp(drawWidth, 0, texX2 - texX1);
                            cropAccumulatorX += MathUtils.clamp(widthToMakeUp, 0, texX2 - texX1);
                        }
                    } else {
                        drawWidth = (texX2 - texX1) * width / (w - totalWidthStretch);
                    }
                    drawWidth = Math.max(drawWidth, 0);
                    if (crushMode == CrushMode.CROP) {
                        drawU = u + (u2 - u) * texX1 / w;
                        drawV = v + (v2 - v) * texY1 / h;
                        drawU2 = u + (u2 - u) * Math.min(texX1 + drawWidth, texX2) / w;
                        drawV2 = v + (v2 - v) * texY2 / h;
                    } else if (crushMode == CrushMode.CROP_REVERSED) {
                        drawU = u + (u2 - u) * Math.max(texX2 - drawWidth, texX1) / w;
                        drawV = v + (v2 - v) * texY1 / h;
                        drawU2 = u + (u2 - u) * texX2 / w;
                        drawV2 = v + (v2 - v) * texY2 / h;
                    } else {
                        drawU = u + (u2 - u) * texX1 / w;
                        drawV = v + (v2 - v) * texY1 / h;
                        drawU2 = u + (u2 - u) * texX2 / w;
                        drawV2 = v + (v2 - v) * texY2 / h;
                    }
    
                    if (texture.getMagFilter() == Texture.TextureFilter.Linear || texture.getMinFilter() == Texture.TextureFilter.Linear) {
                        drawV -= .5f /texture.getHeight();
                        drawV2 += .5f /texture.getHeight();
                    }
                    
                    drawPatches(batch, texture, x, y, originX, originY, drawWidth, drawHeight, drawU, drawV, drawU2, drawV2, texX1, texX2, texY1, texY2, true, false, false, true);

                    originX += drawWidth;
                    xIndex++;

                    //cell of horizontally stretching cells
                    if (xIndex < horizontalStretchAreas.length) {
                        texX1 = texX2;
                        texX2 = (horizontalStretchAreas[xIndex] + 1) * scaleX;

                        drawWidth = texX2 - texX1 + extraWidth * (texX2 - texX1) / totalWidthStretch;
                        drawWidth = Math.max(drawWidth, 0);
                        drawU = u + (u2 - u) * texX1 / w;
                        drawV = v + (v2 - v) * texY1 / h;
                        drawU2 = u + (u2 - u) * texX2 / w;
                        drawV2 = v + (v2 - v) * texY2 / h;
    
                        if (texture.getMagFilter() == Texture.TextureFilter.Linear || texture.getMinFilter() == Texture.TextureFilter.Linear) {
                            drawU += .5f /texture.getWidth();
                            drawU2 -= .5f /texture.getWidth();
                            drawV -= .5f /texture.getHeight();
                            drawV2 += .5f /texture.getHeight();
                        }
                        
                        drawPatches(batch, texture, x, y, originX, originY, drawWidth, drawHeight, drawU, drawV, drawU2, drawV2, texX1, texX2, texY1, texY2, false, false, true, true);

                        originX += drawWidth;
                        xIndex++;
                    }
                    texX1 = texX2;
                }
                originY += drawHeight;
                yIndex++;
            }
            texY1 = texY2;
        }
    }
    
    /**
     * Simplifies drawing calls in draw method.
     * @see TenPatchDrawable#draw(Batch, float, float, float, float)
     * @param batch
     * @param texture
     * @param x
     * @param y
     * @param originX
     * @param originY
     * @param drawWidth
     * @param drawHeight
     * @param drawU
     * @param drawV
     * @param drawU2
     * @param drawV2
     * @param texX1
     * @param texX2
     * @param texY1
     * @param texY2
     * @param squeezeX
     * @param squeezeY 
     */
    private void drawPatches(Batch batch, Texture texture, float x, float y, float originX, float originY, float drawWidth, float drawHeight, float drawU, float drawV, float drawU2, float drawV2, float texX1, float texX2, float texY1, float texY2, boolean squeezeX, boolean squeezeY, boolean tilingX, boolean tilingY) {
        if (!tilingX && !tilingY || !this.tiling) {
            drawToBatch(batch, texture, x + originX, y + originY, drawWidth, drawHeight, drawU, drawV, drawU2, drawV2);
        } else {
            float offsetXadjusted = offsetX % (texX2 - texX1);
            if (offsetXadjusted < 0) offsetXadjusted = (texX2 - texX1) + offsetXadjusted;
            float offsetYadjusted = offsetY % (texY2 - texY1);
            if (offsetYadjusted < 0) offsetYadjusted = (texY2 - texY1) + offsetYadjusted;
            
            float i, j;
            
            //partial row as a result of offsetY
            if (tilingY) {
                //partial cell as result of offsetX
                if (tilingX && offsetXadjusted > 0) {
                    float xValue = x + originX;
                    float yValue = y + originY;
                    float width = Math.min(offsetXadjusted, drawWidth);
                    float height = Math.min(offsetYadjusted, drawHeight);
                    float u = drawU2 - (drawU2 - drawU) * offsetXadjusted / (texX2 - texX1);
                    float v = drawV2 - (drawV - drawV2) * offsetYadjusted / (texY1 - texY2);
                    float u2 = Math.min(drawU2, drawU2 - (drawU2 - drawU) * (offsetXadjusted - drawWidth) / (texX2 - texX1));
                    float v2 = Math.max(drawV2, drawV2 - (drawV - drawV2) * (offsetYadjusted - drawHeight) / (texY1 - texY2));
                    drawToBatch(batch, texture, xValue, yValue, width, height, u, v, u2, v2);
                }
    
                //repeating horizontal cells
                for (i = tilingX ? offsetXadjusted : 0; i < drawWidth && texX2 - texX1 > 0; i += texX2 - texX1) {
                    float xValue = x + originX + i;
                    float yValue = y + originY;
                    float width = Math.min(texX2 - texX1, drawWidth - i);
                    float height = Math.min(offsetYadjusted, drawHeight);
                    float u = drawU;
                    float v = drawV2 - (drawV - drawV2) * offsetYadjusted / (texY1 - texY2);
                    float u2 = Math.min(drawU2, squeezeX ? drawU2 : drawU + (drawU2 - drawU) * (drawWidth - i) / (texX2 - texX1));
                    float v2 = Math.max(drawV2, drawV2 - (drawV - drawV2) * (offsetYadjusted - drawHeight) / (texY1 - texY2));
                    drawToBatch(batch, texture, xValue, yValue, width, height, u, v, u2, v2);
                }
            }
            
            //repeating vertical rows
            for (j = tilingY ? offsetYadjusted : 0; j < drawHeight && texY2 - texY1 > 0; j += texY2 - texY1) {
                //partial cell as result of offsetX
                if (tilingX && offsetXadjusted > 0) {
                    float xValue = x + originX;
                    float yValue = y + originY + j;
                    float width = Math.min(offsetXadjusted, drawWidth);
                    float height = Math.min(texY2 - texY1, drawHeight - j);
                    float u = drawU2 - (drawU2 - drawU) * offsetXadjusted / (texX2 - texX1);
                    float v = drawV;
                    float u2 = Math.min(drawU2, drawU2 - (drawU2 - drawU) * (offsetXadjusted - drawWidth) / (texX2 - texX1));
                    float v2 = Math.max(drawV2, squeezeY ? drawV2 : drawV + (drawV2 - drawV) * (drawHeight - j) / (texY2 - texY1));
                    drawToBatch(batch, texture, xValue, yValue, width, height, u, v, u2, v2);
                }
                
                //repeating horizontal cells
                for (i = tilingX ? offsetXadjusted : 0; i < drawWidth && texX2 - texX1 > 0; i += texX2 - texX1) {
                    float xValue = x + originX + i;
                    float yValue = y + originY + j;
                    float width = Math.min(texX2 - texX1, drawWidth - i);
                    float height = Math.min(texY2 - texY1, drawHeight - j);
                    float u = drawU;
                    float v = drawV;
                    float u2 = Math.min(drawU2, squeezeX ? drawU2 : drawU + (drawU2 - drawU) * (drawWidth - i) / (texX2 - texX1));
                    float v2 = Math.max(drawV2, squeezeY ? drawV2 : drawV + (drawV2 - drawV) * (drawHeight - j) / (texY2 - texY1));
                    drawToBatch(batch, texture, xValue, yValue, width, height, u, v, u2, v2);
                }
            }
        }
    }
    
    private void drawToBatch(Batch batch, Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        int i = 0;
        verts[i++] = x;
        verts[i++] = y;
        temp.set(color1 != null ? color1 : color);
        verts[i++] = temp.mul(batch.getColor()).toFloatBits();
        verts[i++] = u;
        verts[i++] = v;
    
        verts[i++] = x;
        verts[i++] = y + height;
        temp.set(color2 != null ? color2 : color);
        verts[i++] = temp.mul(batch.getColor()).toFloatBits();
        verts[i++] = u;
        verts[i++] = v2;
    
        verts[i++] = x + width;
        verts[i++] = y + height;
        temp.set(color3 != null ? color3 : color);
        verts[i++] = temp.mul(batch.getColor()).toFloatBits();
        verts[i++] = u2;
        verts[i++] = v2;
    
        verts[i++] = x + width;
        verts[i++] = y;
        temp.set(color4 != null ? color4 : color);
        verts[i++] = temp.mul(batch.getColor()).toFloatBits();
        verts[i++] = u2;
        verts[i++] = v;
        batch.draw(texture, verts, 0, verts.length);
    }
    
    /**
     * Not supported.
     * @see TenPatchDrawable#draw(Batch, float, float, float, float)
     * @param batch
     * @param x
     * @param y
     * @param originX
     * @param originY
     * @param width
     * @param height
     * @param scaleX
     * @param scaleY
     * @param rotation
     * @deprecated
     */
    @Deprecated
    public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX,
            float scaleY, float rotation) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * This method must be called to update the offset via offsetXspeed and offsetYspeed. If regions have been set for
     * animation, this will also update the animation. This sets autoUpdate to false.
     * @param delta
     * @see TenPatchDrawable#setOffsetSpeed(float, float)
     * @see TenPatchDrawable#setRegions(Array)
     * @see TenPatchDrawable#setAutoUpdate(boolean)
     */
    public void update(float delta) {
        update(delta, false);
    }
    
    /**
     * This method must be called to update the offset via offsetXspeed and offsetYspeed. If regions have been set for
     * animation, this will also update the animation.
     * @param delta
     * @param autoUpdate Sets autoUpdate to specified value for the next frame.
     * @see TenPatchDrawable#setOffsetSpeed(float, float)
     * @see TenPatchDrawable#setRegions(Array)
     * @see TenPatchDrawable#setAutoUpdate(boolean)
     * @see TenPatchDrawable#setAutoUpdate(boolean)
     */
    private void update(float delta, boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
        time += delta;
        
        offsetX = offsetX + offsetXspeed * delta;
        offsetY = offsetY + offsetYspeed * delta;

        if (regions != null && regions.size > 0) {
            TextureRegion region = getKeyFrame();
            if (getRegion() == null || !getRegion().equals(region)) {
                float minWidth = getMinWidth();
                float minHeight = getMinHeight();
                setRegion(region);
                setMinWidth(minWidth);
                setMinHeight(minHeight);
            }
        }
    }
    
    /**
     * Returns the Color instance for this drawable. Modify this Color to affect the appearance of the drawable.
     * @return The modifiable Color of this drawable.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Sets the color for this drawable. Note that this is a convenience method. Setting the color is achieved by
     * modifying the current color: getColor().set(color).
     * @param color
     * @see TenPatchDrawable#getColor()
     */
    public void setColor(Color color) {
        getColor().set(color);
    }
    
    /** Creates a new drawable that renders the same as this drawable tinted the specified color. */
    public TenPatchDrawable tint(Color tint) {
        TenPatchDrawable drawable = new TenPatchDrawable(this);
        drawable.color.set(tint);
        drawable.setLeftWidth(getLeftWidth());
        drawable.setRightWidth(getRightWidth());
        drawable.setTopHeight(getTopHeight());
        drawable.setBottomHeight(getBottomHeight());
        return drawable;
    }

    public int[] getHorizontalStretchAreas() {
        return horizontalStretchAreas;
    }

    /**
     * Specifies the horizontal stretch areas. All values must be specified in
     * pairs.
     * @param horizontalStretchAreas Pairs of values defining stretch areas from
     * the left of the graphic in ascending order in pixels. All values are
     * inclusive with 0 being the left-most pixel (i.e. (0,2) defines a 3 pixel
     * wide stretch area on the left of the graphic).
     */
    public void setHorizontalStretchAreas(int[] horizontalStretchAreas) {
        this.horizontalStretchAreas = horizontalStretchAreas;
    }

    public int[] getVerticalStretchAreas() {
        return verticalStretchAreas;
    }

    /**
     * Specifies the vertical stretch areas. All values must be specified in
     * pairs.
     * @param verticalStretchAreas Pairs of values defining stretch areas from
     * the bottom of the graphic in ascending order in pixels. All values are
     * inclusive with 0 being the bottom-most pixel (i.e. (0,2) defines a 3
     * pixel high stretch area on the bottom of the graphic).
     */
    public void setVerticalStretchAreas(int[] verticalStretchAreas) {
        this.verticalStretchAreas = verticalStretchAreas;
    }

    public boolean isTiling() {
        return tiling;
    }
    
    /**
     * Specifies if the area specified by the stretch areas will be tiled instead of just stretched.
     * @param tiling
     */
    public void setTiling(boolean tiling) {
        this.tiling = tiling;
    }
    
    public float getOffsetX() {
        return offsetX;
    }
    
    /**
     * Sets the x-offset of tiles in the stretchable areas. Only applicable if the TenPatchDrawable is set to tiling=true
     * @see TenPatchDrawable#setTiling(boolean)
     * @param offsetX
     */
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }
    
    public float getOffsetY() {
        return offsetY;
    }
    
    /**
     * Sets the y-offset of tiles in the stretchable areas. Only applicable if the TenPatchDrawable is set to tiling=true
     * @see TenPatchDrawable#setTiling(boolean)
     * @param offsetY
     */
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
    
    /**
     * Sets the offset of tiles in the stretchable areas. Only applicable if the TenPatchDrawable is set to tiling=true
     * @see TenPatchDrawable#setTiling(boolean)
     * @param offsetX
     * @param offsetY
     */
    public void setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
    
    /**
     * Sets the offset of tiles in the stretchable areas. Only applicable if the TenPatchDrawable is set to tiling=true
     * @see TenPatchDrawable#setTiling(boolean)
     * @param offset
     */
    public void setOffset(float offset) {
        setOffset(offset, offset);
    }
    
    public float getOffsetXspeed() {
        return offsetXspeed;
    }
    
    /**
     * Sets a horizontal speed to update the tile offsets to achieve an animated effect. update() must be called to
     * update the values;
     * @see TenPatchDrawable#setOffset(float, float)
     * @see TenPatchDrawable#update(float)
     * @param offsetXspeed
     */
    public void setOffsetXspeed(float offsetXspeed) {
        this.offsetXspeed = offsetXspeed;
    }
    
    public float getOffsetYspeed() {
        return offsetYspeed;
    }
    
    /**
     * Sets a vertical speed to update the tile offsets to achieve an animated effect. update() must be called to update
     * the values;
     * @see TenPatchDrawable#setOffset(float, float)
     * @see TenPatchDrawable#update(float)
     * @param offsetYspeed
     */
    public void setOffsetYspeed(float offsetYspeed) {
        this.offsetYspeed = offsetYspeed;
    }
    
    /**
     * Sets a horizontal and vertical speed to update the tile offsets to achieve an animated effect. update() must be
     * called to update the values;
     * @see TenPatchDrawable#setOffset(float, float)
     * @see TenPatchDrawable#update(float)
     * @param offsetXspeed
     * @param offsetYspeed
     */
    public void setOffsetSpeed(float offsetXspeed, float offsetYspeed) {
        this.offsetXspeed = offsetXspeed;
        this.offsetYspeed = offsetYspeed;
    }
    
    /**
     * Sets a horizontal and vertical speed to update the tile offsets to achieve an animated effect. update() must be
     * called to update the values;
     * @see TenPatchDrawable#setOffset(float, float)
     * @see TenPatchDrawable#update(float)
     * @param offsetSpeed
     */
    public void setOffsetSpeed(float offsetSpeed) {
        setOffsetSpeed(offsetSpeed, offsetSpeed);
    }
    
    public Color getColor1() {
        return color1;
    }
    
    /**
     * Sets the lower left color of the drawing of each patch. Useful for making gradients or psychedelic effects.
     * Overrides color.
     * @param color1 Can be null.
     * @see TenPatchDrawable#setColor(Color)
     */
    public void setColor1(Color color1) {
        this.color1 = (this.color1 == null ? new Color() : color1).set(color1);
    }
    
    public Color getColor2() {
        return color2;
    }
    
    /**
     * Sets the upper left color of the drawing of each patch. Useful for making gradients or psychedelic effects.
     * Overrides color.
     * @param color2 Can be null.
     * @see TenPatchDrawable#setColor(Color)
     */
    public void setColor2(Color color2) {
        this.color2 = (this.color2 == null ? new Color() : color2).set(color2);
    }
    
    public Color getColor3() {
        return color3;
    }
    
    /**
     * Sets the upper right color of the drawing of each patch. Useful for making gradients or psychedelic effects.
     * Overrides color.
     * @param color3 Can be null.
     * @see TenPatchDrawable#setColor(Color)
     */
    public void setColor3(Color color3) {
        this.color3 = (this.color3 == null ? new Color() : color3).set(color3);
    }
    
    public Color getColor4() {
        return color4;
    }
    
    /**
     * Sets the lower right color of the drawing of each patch. Useful for making gradients or psychedelic effects.
     * Overrides color.
     * @param color4 Can be null.
     * @see TenPatchDrawable#setColor(Color)
     */
    public void setColor4(Color color4) {
        this.color4 = (this.color4 == null ? new Color() : color4).set(color4);
    }
    
    /**
     * Sets the colors of the drawing of each patch. Useful for making gradients or psychedelic effects. Overrides color.
     * @param color1 The lower left color. Can be null.
     * @param color2 The upper left color. Can be null.
     * @param color3 The upper right color. Can be null.
     * @param color4 The lower right color. Can be null.
     */
    public void setColors(Color color1, Color color2, Color color3, Color color4) {
        setColor1(color1);
        setColor2(color2);
        setColor3(color3);
        setColor4(color4);
    }
    
    public Array<TextureRegion> getRegions() {
        return regions;
    }
    
    /**
     * Specify regions to enable animation of the TenPatch. Setting an animation will override the single region setting.
     * @param regions The list of regions to be animated in specified order. At least one region must be specified to
     *                enable animation.
     */
    public void setRegions(Array<TextureRegion> regions) {
        this.regions = regions;
    }
    
    public float getFrameDuration() {
        return frameDuration;
    }
    
    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }
    
    public TextureRegion getKeyFrame(float time) {
        switch (playMode) {
            case PlayMode.REVERSED:
                int index = (int) (time / frameDuration);
                return regions.get(regions.size - 1 - (index < regions.size ? index : regions.size - 1));
            case PlayMode.LOOP:
                return regions.get((int) (time / frameDuration) % regions.size);
            case PlayMode.LOOP_REVERSED:
                return regions.get(regions.size - 1 - (int) (time / frameDuration) % regions.size);
            case PlayMode.LOOP_PINGPONG:
                index = (int) (time / frameDuration);
                index = index % ((regions.size * 2) - 2);
                if (index >= regions.size) index = regions.size - 2 - (index - regions.size);
                return regions.get(index);
            case PlayMode.LOOP_RANDOM:
                index = (int) (time / frameDuration);
                randomXS128.setSeed(seed + index);
                return regions.get(randomXS128.nextInt(regions.size));
            default:
                index = (int) (time / frameDuration);
                return regions.get(index < regions.size ? index : regions.size - 1);
        }
    }
    
    public TextureRegion getKeyFrame() {
        return getKeyFrame(time);
    }
    
    public float getTime() {
        return time;
    }
    
    public void setTime(float time) {
        this.time = time;
    }
    
    /**
     * Returns whether the drawable automatically updates the animation and offset via Gdx.graphics.getDeltaTime().
     * @return default true
     * @see TenPatchDrawable#setOffsetSpeed(float, float)
     * @see TenPatchDrawable#setRegions(Array)
     */
    public boolean isAutoUpdate() {
        return autoUpdate;
    }
    
    /**
     * Automatically updates the animation and offset via Gdx.graphics.getDeltaTime().
     * @param autoUpdate default true
     * @see TenPatchDrawable#setOffsetSpeed(float, float)
     * @see TenPatchDrawable#setRegions(Array)
     */
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }
    
    public int getPlayMode() {
        return playMode;
    }
    
    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }
    
    public int getCrushMode() {
        return crushMode;
    }
    
    public void setCrushMode(int crushMode) {
        this.crushMode = crushMode;
    }
    
    /**
     * Multiplies the X and Y scaling by the specified amount.
     *
     * @param scaleX scaling in X
     * @param scaleY scaling in Y
     * @see NinePatch#scale(float, float)
     */
    public void scale(float scaleX, float scaleY) {
        this.scaleX *= scaleX;
        this.scaleY *= scaleY;
    }

    /**
     * Returns the x scaling of this TenPatch. The scaling of a TenPatch affects
     * the scaling of it's regions without affecting the size of the drawable.
     * Higher scaling means the region will be rendered bigger as if you were
     * to zoom in. This allows using TenPatch with viewports where world size
     * does not match pixel size.
     *
     * @return x scaling of this TenPatch
     */
    public float getScaleX()
    {
        return scaleX;
    }

    public void setScaleX(float scaleX)
    {
        this.scaleX = scaleX;
    }
    
    /**
     * Returns the y scaling of this TenPatch. The scaling of a TenPatch affects
     * the scaling of it's regions without affecting the size of the drawable.
     * Higher scaling means the region will be rendered bigger as if you were
     * to zoom in. This allows using TenPatch with viewports where world size
     * does not match pixel size.
     *
     * @return y scaling of this TenPatch
     */
    public float getScaleY()
    {
        return scaleY;
    }

    public void setScaleY(float scaleY)
    {
        this.scaleY = scaleY;
    }
    
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }
}
