// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinArray;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinValue;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins.SkinArrayMixin;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkinArrayImpl extends SkinArrayMixin implements SkinArray {

  public SkinArrayImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitArray(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SkinValue> getValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SkinValue.class);
  }

}
