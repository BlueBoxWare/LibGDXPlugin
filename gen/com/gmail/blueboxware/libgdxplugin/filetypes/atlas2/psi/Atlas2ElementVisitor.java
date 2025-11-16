// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class Atlas2ElementVisitor extends PsiElementVisitor {

  public void visitField(@NotNull Atlas2Field o) {
    visitValuesOwner(o);
    // visitNamedElement(o);
  }

  public void visitHeader(@NotNull Atlas2Header o) {
    visitValueOwner(o);
  }

  public void visitKey(@NotNull Atlas2Key o) {
    visitValueOwner(o);
  }

  public void visitPage(@NotNull Atlas2Page o) {
    visitPageOwner(o);
    // visitNamedElement(o);
  }

  public void visitRegion(@NotNull Atlas2Region o) {
    visitImageOwner(o);
    // visitNamedElement(o);
  }

  public void visitValue(@NotNull Atlas2Value o) {
    visitValueOwner(o);
  }

  public void visitImageOwner(@NotNull Atlas2ImageOwner o) {
    visitPsiElement(o);
  }

  public void visitPageOwner(@NotNull Atlas2PageOwner o) {
    visitPsiElement(o);
  }

  public void visitValueOwner(@NotNull Atlas2ValueOwner o) {
    visitPsiElement(o);
  }

  public void visitValuesOwner(@NotNull Atlas2ValuesOwner o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
