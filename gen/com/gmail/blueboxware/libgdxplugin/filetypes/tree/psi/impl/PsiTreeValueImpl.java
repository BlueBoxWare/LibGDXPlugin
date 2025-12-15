// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.mixins.TreeValueMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.*;

public class PsiTreeValueImpl extends TreeValueMixin implements PsiTreeValue {

  public PsiTreeValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiTreeVisitor visitor) {
    visitor.visitValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiTreeVisitor) accept((PsiTreeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiTreeVkeyword getVkeyword() {
    return findChildByClass(PsiTreeVkeyword.class);
  }

  @Override
  @Nullable
  public PsiTreeVnumber getVnumber() {
    return findChildByClass(PsiTreeVnumber.class);
  }

  @Override
  @Nullable
  public PsiTreeVstring getVstring() {
    return findChildByClass(PsiTreeVstring.class);
  }

}
