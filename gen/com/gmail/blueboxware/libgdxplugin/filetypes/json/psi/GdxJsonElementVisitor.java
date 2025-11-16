// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

public class GdxJsonElementVisitor extends PsiElementVisitor {

  public void visitArray(@NotNull GdxJsonArray o) {
    visitElement(o);
  }

  public void visitJobject(@NotNull GdxJsonJobject o) {
    visitPropertiesOwner(o);
    // visitElement(o);
  }

  public void visitProperty(@NotNull GdxJsonProperty o) {
    visitElement(o);
    // visitPsiNamedElement(o);
  }

  public void visitPropertyName(@NotNull GdxJsonPropertyName o) {
    visitString(o);
  }

  public void visitString(@NotNull GdxJsonString o) {
    visitStringOwner(o);
    // visitLiteral(o);
  }

  public void visitValue(@NotNull GdxJsonValue o) {
    visitValueOwner(o);
    // visitElement(o);
  }

  public void visitElement(@NotNull GdxJsonElement o) {
    visitPsiElement(o);
  }

  public void visitPropertiesOwner(@NotNull GdxJsonPropertiesOwner o) {
    visitPsiElement(o);
  }

  public void visitStringOwner(@NotNull GdxJsonStringOwner o) {
    visitPsiElement(o);
  }

  public void visitValueOwner(@NotNull GdxJsonValueOwner o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
