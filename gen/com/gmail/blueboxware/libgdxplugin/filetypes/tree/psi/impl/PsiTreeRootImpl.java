// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins.TreeRootMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*;

public class PsiTreeRootImpl extends TreeRootMixin implements PsiTreeRoot {

  public PsiTreeRootImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTreeElementVisitor visitor) {
    visitor.visitRoot(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTreeElementVisitor) accept((PsiTreeElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiTreeAttribute> getAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiTreeAttribute.class);
  }

}
