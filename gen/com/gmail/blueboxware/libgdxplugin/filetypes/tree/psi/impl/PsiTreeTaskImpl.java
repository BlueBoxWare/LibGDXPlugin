// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins.TreeTaskMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*;

public class PsiTreeTaskImpl extends TreeTaskMixin implements PsiTreeTask {

  public PsiTreeTaskImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTreeVisitor visitor) {
    visitor.visitTask(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTreeVisitor) accept((PsiTreeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiTreeAttribute> getAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiTreeAttribute.class);
  }

  @Override
  @Nullable
  public PsiTreeSubtreeref getSubtreeref() {
    return findChildByClass(PsiTreeSubtreeref.class);
  }

  @Override
  @Nullable
  public PsiTreeTaskname getTaskname() {
    return findChildByClass(PsiTreeTaskname.class);
  }

}
