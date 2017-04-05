// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class SkinElementVisitor extends PsiElementVisitor {

  public void visitArray(@NotNull SkinArray o) {
    visitValue(o);
  }

  public void visitBooleanLiteral(@NotNull SkinBooleanLiteral o) {
    visitLiteral(o);
  }

  public void visitClassName(@NotNull SkinClassName o) {
    visitElement(o);
  }

  public void visitClassSpecification(@NotNull SkinClassSpecification o) {
    visitNamedElement(o);
  }

  public void visitLiteral(@NotNull SkinLiteral o) {
    visitValue(o);
  }

  public void visitNullLiteral(@NotNull SkinNullLiteral o) {
    visitLiteral(o);
  }

  public void visitNumberLiteral(@NotNull SkinNumberLiteral o) {
    visitLiteral(o);
  }

  public void visitObject(@NotNull SkinObject o) {
    visitValue(o);
  }

  public void visitProperty(@NotNull SkinProperty o) {
    visitNamedElement(o);
  }

  public void visitPropertyName(@NotNull SkinPropertyName o) {
    visitElement(o);
  }

  public void visitPropertyValue(@NotNull SkinPropertyValue o) {
    visitElement(o);
  }

  public void visitResource(@NotNull SkinResource o) {
    visitNamedElement(o);
  }

  public void visitResourceName(@NotNull SkinResourceName o) {
    visitElement(o);
  }

  public void visitResources(@NotNull SkinResources o) {
    visitPsiElement(o);
  }

  public void visitStringLiteral(@NotNull SkinStringLiteral o) {
    visitLiteral(o);
  }

  public void visitValue(@NotNull SkinValue skinValue) {
    visitElement(skinValue);
  }

  public void visitElement(@NotNull SkinElement o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull SkinNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
