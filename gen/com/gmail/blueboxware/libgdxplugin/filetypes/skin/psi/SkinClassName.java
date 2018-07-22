// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiClass;

public interface SkinClassName extends SkinElement {

  @NotNull
  SkinStringLiteral getStringLiteral();

  @NotNull
  String getValue();

  @Nullable
  PsiClass resolve();

  @NotNull
  List<PsiClass> multiResolve();

}
