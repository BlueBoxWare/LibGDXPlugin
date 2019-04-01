// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins.GdxJsonPropertyMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*;

public class GdxJsonPropertyImpl extends GdxJsonPropertyMixin implements GdxJsonProperty {

  public GdxJsonPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdxJsonElementVisitor visitor) {
    visitor.visitProperty(this);
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
  public GdxJsonPropertyName getPropertyName() {
    return findNotNullChildByClass(GdxJsonPropertyName.class);
  }

  @Override
  @Nullable
  public GdxJsonValue getValue() {
    return findChildByClass(GdxJsonValue.class);
  }

}
