// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonComment;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins.GdxJsonArrayMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdxJsonArrayImpl extends GdxJsonArrayMixin implements GdxJsonArray {

  public GdxJsonArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdxJsonElementVisitor visitor) {
    visitor.visitArray(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdxJsonElementVisitor) accept((GdxJsonElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdxJsonComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdxJsonComment.class);
  }

  @Override
  @NotNull
  public List<GdxJsonValue> getValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdxJsonValue.class);
  }

}
