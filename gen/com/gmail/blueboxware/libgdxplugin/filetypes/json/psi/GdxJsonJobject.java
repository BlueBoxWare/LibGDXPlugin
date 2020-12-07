// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface GdxJsonJobject extends GdxJsonElement {

  @NotNull
  List<GdxJsonProperty> getPropertyList();

  @Nullable
  GdxJsonProperty getProperty(@NotNull String name);

  boolean getProperties(@NotNull String name);

}
