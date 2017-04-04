// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinValueMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;

public abstract class SkinValueImpl extends SkinValueMixin implements SkinValue {

  public SkinValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitValue(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

}
