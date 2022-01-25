// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2ElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Header;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins.Atlas2HeaderMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class Atlas2HeaderImpl extends Atlas2HeaderMixin implements Atlas2Header {

  public Atlas2HeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Atlas2ElementVisitor visitor) {
    visitor.visitHeader(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Atlas2ElementVisitor) accept((Atlas2ElementVisitor)visitor);
    else super.accept(visitor);
  }

}
