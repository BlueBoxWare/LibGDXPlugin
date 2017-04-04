// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.jetbrains.annotations.Nullable;

public interface SkinValue extends SkinElement {

  @Nullable
  SkinProperty getProperty();

  @Nullable
  PsiType resolveToType();

  @Nullable
  String resolveToTypeString();

  @Nullable
  PsiClass resolveToClass();

}
