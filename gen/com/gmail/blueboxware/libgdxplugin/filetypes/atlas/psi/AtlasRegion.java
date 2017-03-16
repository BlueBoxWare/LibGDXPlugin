// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElement;

public interface AtlasRegion extends AtlasElement {

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

}
