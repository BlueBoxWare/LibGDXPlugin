// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GdxJsonElementVisitor extends PsiElementVisitor {

  public void visitArray(@NotNull GdxJsonArray o) {
    visitElement(o);
  }

  public void visitJobject(@NotNull GdxJsonJobject o) {
    visitElement(o);
  }

  public void visitProperty(@NotNull GdxJsonProperty o) {
    visitElement(o);
    // visitPsiNamedElement(o);
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
