// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassNameMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class SkinClassNameImpl extends SkinClassNameMixin implements SkinClassName {

  public SkinClassNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitClassName(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SkinStringLiteral getStringLiteral() {
    return findNotNullChildByClass(SkinStringLiteral.class);
  }

}
