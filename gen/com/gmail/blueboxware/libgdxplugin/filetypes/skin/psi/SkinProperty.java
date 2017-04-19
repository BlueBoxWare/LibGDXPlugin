// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

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
