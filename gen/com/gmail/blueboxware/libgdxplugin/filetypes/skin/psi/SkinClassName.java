// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface SkinClassName extends SkinElement {

  @NotNull
  SkinStringLiteral getStringLiteral();

  @NotNull
  DollarClassName getValue();

  @Nullable
  PsiClass resolve();

  @NotNull
  List<PsiClass> multiResolve();

}
