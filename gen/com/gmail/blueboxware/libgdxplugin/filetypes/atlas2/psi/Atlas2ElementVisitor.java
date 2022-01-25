// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2Element;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2FieldOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class Atlas2ElementVisitor extends PsiElementVisitor {

  public void visitField(@NotNull Atlas2Field o) {
    visitElement(o);
    // visitNamedElement(o);
  }

  public void visitHeader(@NotNull Atlas2Header o) {
    visitElement(o);
  }

  public void visitKey(@NotNull Atlas2Key o) {
    visitElement(o);
  }

  public void visitPage(@NotNull Atlas2Page o) {
    visitFieldOwner(o);
    // visitNamedElement(o);
  }

  public void visitRegion(@NotNull Atlas2Region o) {
    visitFieldOwner(o);
    // visitNamedElement(o);
  }

  public void visitValue(@NotNull Atlas2Value o) {
    visitElement(o);
  }

  public void visitElement(@NotNull Atlas2Element o) {
    visitPsiElement(o);
  }

  public void visitFieldOwner(@NotNull Atlas2FieldOwner o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
