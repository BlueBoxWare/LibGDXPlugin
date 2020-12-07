// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins.GdxJsonJobjectMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*;

public class GdxJsonJobjectImpl extends GdxJsonJobjectMixin implements GdxJsonJobject {

  public GdxJsonJobjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdxJsonElementVisitor visitor) {
    visitor.visitJobject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdxJsonElementVisitor) accept((GdxJsonElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<GdxJsonProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, GdxJsonProperty.class);
  }

}
