// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

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
