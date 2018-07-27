// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinObjectMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiField;
import java.awt.Color;

public class SkinObjectImpl extends SkinObjectMixin implements SkinObject {

  public SkinObjectImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitObject(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SkinProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SkinProperty.class);
  }

}
