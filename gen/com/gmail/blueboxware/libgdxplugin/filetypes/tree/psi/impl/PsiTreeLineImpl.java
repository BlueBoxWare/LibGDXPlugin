// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins.TreeLineMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*;

public class PsiTreeLineImpl extends TreeLineMixin implements PsiTreeLine {

  public PsiTreeLineImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTreeVisitor visitor) {
    visitor.visitLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTreeVisitor) accept((PsiTreeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiTreeGuard> getGuardList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiTreeGuard.class);
  }

  @Override
  @NotNull
  public PsiTreeIndent getIndent() {
    return findNotNullChildByClass(PsiTreeIndent.class);
  }

  @Override
  @Nullable
  public PsiTreeStatement getStatement() {
    return findChildByClass(PsiTreeStatement.class);
  }

}
