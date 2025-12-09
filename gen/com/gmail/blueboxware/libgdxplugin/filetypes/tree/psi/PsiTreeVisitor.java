// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class PsiTreeVisitor extends PsiElementVisitor {

  public void visitAttribute(@NotNull PsiTreeAttribute o) {
    visitTreeElement(o);
  }

  public void visitGuard(@NotNull PsiTreeGuard o) {
    visitTreeElement(o);
  }

  public void visitImport(@NotNull PsiTreeImport o) {
    visitTreeTask(o);
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
    visitTreeTask(o);
    // visitTreeElement(o);
  }

  public void visitStatement(@NotNull PsiTreeStatement o) {
    visitTreeElement(o);
  }

  public void visitSubtree(@NotNull PsiTreeSubtree o) {
    visitTreeTask(o);
    // visitTreeElement(o);
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
    visitTreeElement(o);
  }

  public void visitTreeElement(@NotNull TreeElement o) {
    visitElement(o);
  }

  public void visitTreeIndent(@NotNull TreeIndent o) {
    visitElement(o);
  }

  public void visitTreeLine(@NotNull TreeLine o) {
    visitElement(o);
  }

  public void visitTreeTask(@NotNull TreeTask o) {
    visitElement(o);
  }

  public void visitTreeTaskName(@NotNull TreeTaskName o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
