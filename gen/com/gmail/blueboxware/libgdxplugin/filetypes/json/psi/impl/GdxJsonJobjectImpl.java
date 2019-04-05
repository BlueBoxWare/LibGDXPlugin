// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins.GdxJsonJobjectMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GdxJsonJobjectImpl extends GdxJsonJobjectMixin implements GdxJsonJobject {

  public GdxJsonJobjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdxJsonElementVisitor visitor) {
    visitor.visitJobject(this);
  }

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
