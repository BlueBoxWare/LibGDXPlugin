// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinClassSpecificationMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*;
import com.gmail.blueboxware.libgdxplugin.utils.DollarClassName;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;

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
