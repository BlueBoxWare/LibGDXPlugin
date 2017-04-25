// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasNamedElement;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

public interface AtlasRegion extends AtlasNamedElement {

  @NotNull
  AtlasIndex getIndex();

  @NotNull
  AtlasOffset getOffset();

  @NotNull
  AtlasOrig getOrig();

  @Nullable
  AtlasPad getPad();

  @NotNull
  AtlasRegionName getRegionName();

  @NotNull
  AtlasRotate getRotate();

  @NotNull
  AtlasSize getSize();

  @Nullable
  AtlasSplit getSplit();

  @NotNull
  AtlasXy getXy();

  String getName();

  @Nullable
  AtlasPage getPage();

  @Nullable
  BufferedImage getImage();

  @Nullable
  Icon getPreviewIcon();

  @Nullable
  Integer getX();

  @Nullable
  Integer getY();

  @Nullable
  Integer getWidth();

  @Nullable
  Integer getHeight();

  int getOriginalSize();

  int getOriginalHeight();

  int getOriginalWidth();

}
