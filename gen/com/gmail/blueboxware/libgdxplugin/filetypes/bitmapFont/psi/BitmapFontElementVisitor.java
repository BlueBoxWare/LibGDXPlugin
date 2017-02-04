// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class BitmapFontElementVisitor extends PsiElementVisitor {

  public void visitChars(@NotNull BitmapFontChars o) {
    visitPropertyContainer(o);
  }

  public void visitCommon(@NotNull BitmapFontCommon o) {
    visitPropertyContainer(o);
  }

  public void visitFontChar(@NotNull BitmapFontFontChar o) {
    visitPropertyContainer(o);
  }

  public void visitInfo(@NotNull BitmapFontInfo o) {
    visitPropertyContainer(o);
  }

  public void visitKerning(@NotNull BitmapFontKerning o) {
    visitPropertyContainer(o);
  }

  public void visitKernings(@NotNull BitmapFontKernings o) {
    visitPropertyContainer(o);
  }

  public void visitKey(@NotNull BitmapFontKey o) {
    visitPsiElement(o);
  }

  public void visitPageDefinition(@NotNull BitmapFontPageDefinition o) {
    visitPropertyContainer(o);
  }

  public void visitProperty(@NotNull BitmapFontProperty o) {
    visitPsiElement(o);
  }

  public void visitValue(@NotNull BitmapFontValue o) {
    visitElement(o);
  }

  public void visitElement(@NotNull BitmapFontElement o) {
    visitPsiElement(o);
  }

  public void visitPropertyContainer(@NotNull PropertyContainer o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
