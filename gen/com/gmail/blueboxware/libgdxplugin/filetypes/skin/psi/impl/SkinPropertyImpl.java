// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinPropertyMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;

public class SkinPropertyImpl extends SkinPropertyMixin implements SkinProperty {

  public SkinPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitProperty(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public SkinPropertyName getPropertyName() {
    return findNotNullChildByClass(SkinPropertyName.class);
  }

  @Override
  @Nullable
  public SkinPropertyValue getPropertyValue() {
    return findChildByClass(SkinPropertyValue.class);
  }

}
