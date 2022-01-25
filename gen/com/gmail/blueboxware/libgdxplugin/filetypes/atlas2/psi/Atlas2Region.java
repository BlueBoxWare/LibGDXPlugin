// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2FieldOwner;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2NamedElement;
import java.awt.image.BufferedImage;

public interface Atlas2Region extends Atlas2FieldOwner, Atlas2NamedElement {

  @NotNull
  List<Atlas2Field> getFieldList();

  @NotNull
  Atlas2Header getHeader();

  @Nullable
  Integer getIndex();

  @Nullable
  BufferedImage getImage();

  @Nullable
  Atlas2Page getPage();

  @Nullable
  Integer getX();

  @Nullable
  Integer getY();

  @Nullable
  Integer getWidth();

  @Nullable
  Integer getHeight();

}
