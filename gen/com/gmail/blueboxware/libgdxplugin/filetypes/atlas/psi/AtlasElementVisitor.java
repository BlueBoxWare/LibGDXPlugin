// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasValueElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasNamedElement;

public class AtlasElementVisitor extends PsiElementVisitor {

  public void visitFilter(@NotNull AtlasFilter o) {
    visitElement(o);
  }

  public void visitFilterValue(@NotNull AtlasFilterValue o) {
    visitValueElement(o);
  }

  public void visitFormat(@NotNull AtlasFormat o) {
    visitElement(o);
  }

  public void visitFormatValue(@NotNull AtlasFormatValue o) {
    visitValueElement(o);
  }

  public void visitIndex(@NotNull AtlasIndex o) {
    visitElement(o);
  }

  public void visitOffset(@NotNull AtlasOffset o) {
    visitElement(o);
  }

  public void visitOrig(@NotNull AtlasOrig o) {
    visitElement(o);
  }

  public void visitPad(@NotNull AtlasPad o) {
    visitElement(o);
  }

  public void visitPage(@NotNull AtlasPage o) {
    visitElement(o);
  }

  public void visitRegion(@NotNull AtlasRegion o) {
    visitNamedElement(o);
  }

  public void visitRegionName(@NotNull AtlasRegionName o) {
    visitPsiElement(o);
  }

  public void visitRepeat(@NotNull AtlasRepeat o) {
    visitElement(o);
  }

  public void visitRepeatValue(@NotNull AtlasRepeatValue o) {
    visitValueElement(o);
  }

  public void visitRotate(@NotNull AtlasRotate o) {
    visitElement(o);
  }

  public void visitRotateValue(@NotNull AtlasRotateValue o) {
    visitValueElement(o);
  }

  public void visitSize(@NotNull AtlasSize o) {
    visitElement(o);
  }

  public void visitSplit(@NotNull AtlasSplit o) {
    visitElement(o);
  }

  public void visitValue(@NotNull AtlasValue o) {
    visitValueElement(o);
  }

  public void visitXy(@NotNull AtlasXy o) {
    visitElement(o);
  }

  public void visitElement(@NotNull AtlasElement o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull AtlasNamedElement o) {
    visitPsiElement(o);
  }

  public void visitValueElement(@NotNull AtlasValueElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
