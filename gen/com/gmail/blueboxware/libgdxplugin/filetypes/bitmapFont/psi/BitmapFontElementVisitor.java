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
    visitCharOwner(o);
    // visitPropertyContainer(o);
  }

  public void visitInfo(@NotNull BitmapFontInfo o) {
    visitPropertyContainer(o);
  }

  public void visitKerning(@NotNull BitmapFontKerning o) {
    visitKerningOwner(o);
    // visitPropertyContainer(o);
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
    visitPropertyOwner(o);
  }

  public void visitValue(@NotNull BitmapFontValue o) {
    visitValueOwner(o);
    // visitElement(o);
  }

  public void visitCharOwner(@NotNull BitmapFontCharOwner o) {
    visitPsiElement(o);
  }

  public void visitKerningOwner(@NotNull BitmapFontKerningOwner o) {
    visitPsiElement(o);
  }

  public void visitPropertyOwner(@NotNull BitmapFontPropertyOwner o) {
    visitPsiElement(o);
  }

  public void visitValueOwner(@NotNull BitmapFontValueOwner o) {
    visitPsiElement(o);
  }

  public void visitPropertyContainer(@NotNull PropertyContainer o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
