// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SkinProperty extends SkinNamedElement {

  @NotNull
  SkinPropertyName getPropertyName();

  @Nullable
  SkinPropertyValue getPropertyValue();

  @NotNull
  String getName();

  @Nullable
  SkinValue getValue();

  @Nullable
  SkinObject getContainingObject();

  @Nullable
  PsiField resolveToField();

  @Nullable
  PsiType resolveToType();

  @Nullable
  String resolveToTypeString();

}
