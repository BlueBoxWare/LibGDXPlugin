// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElement;
import com.intellij.openapi.vfs.VirtualFile;

public interface AtlasPage extends AtlasElement {

  @NotNull
  AtlasFilter getFilter();

  @NotNull
  AtlasFormat getFormat();

  @NotNull
  List<AtlasRegion> getRegionList();

  @NotNull
  AtlasRepeat getRepeat();

  @Nullable
  AtlasSize getSize();

  @NotNull
  AtlasValue getPageName();

  @Nullable
  Integer getIndex();

  @Nullable
  VirtualFile getImageFile();

}
