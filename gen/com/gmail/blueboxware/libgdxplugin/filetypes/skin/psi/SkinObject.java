// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public interface SkinObject extends SkinValue {

  @NotNull
  List<SkinProperty> getPropertyList();

  @Nullable
  Color asColor(boolean force);

  @Nullable
  SkinResource asResource();

  @NotNull
  List<String> getPropertyNames();

  void addProperty(SkinProperty property);

  void addComment(PsiComment comment);

  @Nullable
  SkinProperty getProperty(String name);

  @Nullable
  PsiField resolveToField(SkinProperty property);

}
