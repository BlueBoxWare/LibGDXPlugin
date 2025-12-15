// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class PsiTreeVisitor extends PsiElementVisitor {

  public void visitAttribute(@NotNull PsiTreeAttribute o) {
    visitTreeElement(o);
  }

  public void visitAttributeName(@NotNull PsiTreeAttributeName o) {
    visitTreeElement(o);
  }

  public void visitGuard(@NotNull PsiTreeGuard o) {
    visitTreeElement(o);
  }

  public void visitImport(@NotNull PsiTreeImport o) {
    visitTreeImport(o);
    // visitTreeElement(o);
  }

  public void visitIndent(@NotNull PsiTreeIndent o) {
    visitTreeIndent(o);
    // visitTreeElement(o);
  }

  public void visitLine(@NotNull PsiTreeLine o) {
    visitTreeLine(o);
    // visitTreeElement(o);
  }

  public void visitRoot(@NotNull PsiTreeRoot o) {
    visitTreeElement(o);
  }

  public void visitStatement(@NotNull PsiTreeStatement o) {
    visitTreeElement(o);
  }

  public void visitSubtree(@NotNull PsiTreeSubtree o) {
    visitTreeElement(o);
  }

  public void visitSubtreeref(@NotNull PsiTreeSubtreeref o) {
    visitTreeElement(o);
  }

  public void visitTask(@NotNull PsiTreeTask o) {
    visitTreeTask(o);
    // visitTreeElement(o);
  }

  public void visitTaskname(@NotNull PsiTreeTaskname o) {
    visitTreeTaskName(o);
    // visitTreeElement(o);
  }

  public void visitValue(@NotNull PsiTreeValue o) {
    visitTreeValue(o);
    // visitTreeElement(o);
  }

  public void visitVkeyword(@NotNull PsiTreeVkeyword o) {
    visitTreeElement(o);
  }

  public void visitVnumber(@NotNull PsiTreeVnumber o) {
    visitTreeElement(o);
  }

  public void visitVstring(@NotNull PsiTreeVstring o) {
    visitTreeString(o);
    // visitTreeElement(o);
  }

  public void visitTreeElement(@NotNull TreeElement o) {
    visitElement(o);
  }

  public void visitTreeImport(@NotNull TreeImport o) {
    visitElement(o);
  }

  public void visitTreeIndent(@NotNull TreeIndent o) {
    visitElement(o);
  }

  public void visitTreeLine(@NotNull TreeLine o) {
    visitElement(o);
  }

  public void visitTreeString(@NotNull TreeString o) {
    visitElement(o);
  }

  public void visitTreeTask(@NotNull TreeTask o) {
    visitElement(o);
  }

  public void visitTreeTaskName(@NotNull TreeTaskName o) {
    visitElement(o);
  }

  public void visitTreeValue(@NotNull TreeValue o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
