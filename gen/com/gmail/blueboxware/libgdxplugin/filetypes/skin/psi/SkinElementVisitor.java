// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class SkinElementVisitor extends PsiElementVisitor {

  public void visitArray(@NotNull SkinArray o) {
    visitValue(o);
  }

  public void visitClassName(@NotNull SkinClassName o) {
    visitClassNameOwner(o);
    // visitElement(o);
  }

  public void visitClassSpecification(@NotNull SkinClassSpecification o) {
    visitClassOwner(o);
    // visitNamedElement(o);
  }

  public void visitObject(@NotNull SkinObject o) {
    visitValue(o);
    // visitObjectOwner(o);
  }

  public void visitProperty(@NotNull SkinProperty o) {
    visitPropertySpecification(o);
    // visitNamedElement(o);
  }

  public void visitPropertyName(@NotNull SkinPropertyName o) {
    visitValueOwner(o);
    // visitElement(o);
  }

  public void visitPropertyValue(@NotNull SkinPropertyValue o) {
    visitPropertyOwner(o);
    // visitElement(o);
  }

  public void visitResource(@NotNull SkinResource o) {
    visitResourceOwner(o);
    // visitNamedElement(o);
  }

  public void visitResourceName(@NotNull SkinResourceName o) {
    visitResourceNameOwner(o);
    // visitElement(o);
  }

  public void visitResources(@NotNull SkinResources o) {
    visitElement(o);
  }

  public void visitStringLiteral(@NotNull SkinStringLiteral o) {
    visitValue(o);
    // visitStringOwner(o);
  }

  public void visitValue(@NotNull SkinValue o) {
    visitPropertyValueOwner(o);
    // visitElement(o);
  }

  public void visitClassNameOwner(@NotNull SkinClassNameOwner o) {
    visitPsiElement(o);
  }

  public void visitClassOwner(@NotNull SkinClassOwner o) {
    visitPsiElement(o);
  }

  public void visitElement(@NotNull SkinElement o) {
    visitPsiElement(o);
  }

  public void visitPropertyOwner(@NotNull SkinPropertyOwner o) {
    visitPsiElement(o);
  }

  public void visitPropertySpecification(@NotNull SkinPropertySpecification o) {
    visitPsiElement(o);
  }

  public void visitPropertyValueOwner(@NotNull SkinPropertyValueOwner o) {
    visitPsiElement(o);
  }

  public void visitResourceNameOwner(@NotNull SkinResourceNameOwner o) {
    visitPsiElement(o);
  }

  public void visitResourceOwner(@NotNull SkinResourceOwner o) {
    visitPsiElement(o);
  }

  public void visitValueOwner(@NotNull SkinValueOwner o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
