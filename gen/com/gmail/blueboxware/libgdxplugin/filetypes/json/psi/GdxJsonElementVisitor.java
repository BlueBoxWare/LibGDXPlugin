// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class GdxJsonElementVisitor extends PsiElementVisitor {

  public void visitArray(@NotNull GdxJsonArray o) {
    visitElement(o);
  }

  public void visitJobject(@NotNull GdxJsonJobject o) {
    visitElement(o);
  }

  public void visitNumberValue(@NotNull GdxJsonNumberValue o) {
    visitLiteral(o);
  }

  public void visitProperty(@NotNull GdxJsonProperty o) {
    visitElement(o);
  }

  public void visitPropertyName(@NotNull GdxJsonPropertyName o) {
    visitString(o);
  }

  public void visitString(@NotNull GdxJsonString o) {
    visitLiteral(o);
  }

  public void visitValue(@NotNull GdxJsonValue o) {
    visitElement(o);
  }

  public void visitElement(@NotNull GdxJsonElement o) {
    visitPsiElement(o);
  }

  public void visitLiteral(@NotNull GdxJsonLiteral o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
