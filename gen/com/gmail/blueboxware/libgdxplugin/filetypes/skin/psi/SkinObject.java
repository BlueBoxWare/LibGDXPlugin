// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiField;
import java.awt.Color;

public interface SkinObject extends SkinValue {

  @NotNull
  List<SkinProperty> getPropertyList();

  @Nullable
  Color asColor(boolean force);

  @Nullable
  SkinResource asResource();

  @NotNull
  List<String> getPropertyNames();

  @Nullable
  SkinObject setColor(@NotNull Color color);

  void addProperty(@NotNull SkinProperty property);

  void addComment(@NotNull PsiComment comment);

  @Nullable
  SkinProperty getProperty(@NotNull String name);

  @Nullable
  PsiField resolveToField(@NotNull SkinProperty property);

}
