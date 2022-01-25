// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2FieldOwner;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2NamedElement;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Atlas2Page extends Atlas2FieldOwner, Atlas2NamedElement {

  @NotNull
  List<Atlas2Field> getFieldList();

  @NotNull
  Atlas2Header getHeader();

  @NotNull
  List<Atlas2Region> getRegionList();

  @Nullable
  Integer getIndex();

  @Nullable
  VirtualFile getImageFile();

}
