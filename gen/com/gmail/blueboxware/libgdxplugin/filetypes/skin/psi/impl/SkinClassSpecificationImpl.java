// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResources;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkinClassSpecificationImpl extends SkinClassSpecificationMixin implements SkinClassSpecification {

  public SkinClassSpecificationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitClassSpecification(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SkinClassName getClassName() {
    return findNotNullChildByClass(SkinClassName.class);
  }

  @Override
  @Nullable
  public SkinResources getResources() {
    return findChildByClass(SkinResources.class);
  }

}
