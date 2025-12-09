// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*;

public class PsiTreeStatementImpl extends ASTWrapperPsiElement implements PsiTreeStatement {

  public PsiTreeStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTreeVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTreeVisitor) accept((PsiTreeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiTreeImport getImport() {
    return findChildByClass(PsiTreeImport.class);
  }

  @Override
  @Nullable
  public PsiTreeRoot getRoot() {
    return findChildByClass(PsiTreeRoot.class);
  }

  @Override
  @Nullable
  public PsiTreeSubtree getSubtree() {
    return findChildByClass(PsiTreeSubtree.class);
  }

  @Override
  @Nullable
  public PsiTreeTask getTask() {
    return findChildByClass(PsiTreeTask.class);
  }

}
