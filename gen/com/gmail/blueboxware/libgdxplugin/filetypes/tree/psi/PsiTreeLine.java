// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiTreeLine extends TreeLine, TreeElement {

  @NotNull
  List<PsiTreeGuard> getGuardList();

  @NotNull
  PsiTreeIndent getIndent();

  @Nullable
  PsiTreeStatement getStatement();

}
