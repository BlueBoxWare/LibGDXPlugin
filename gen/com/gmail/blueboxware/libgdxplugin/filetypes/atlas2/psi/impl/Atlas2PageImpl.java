// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.mixins.Atlas2PageMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.*;

public class Atlas2PageImpl extends Atlas2PageMixin implements Atlas2Page {

  public Atlas2PageImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull Atlas2ElementVisitor visitor) {
    visitor.visitPage(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof Atlas2ElementVisitor) accept((Atlas2ElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<Atlas2Field> getFieldList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Atlas2Field.class);
  }

  @Override
  @NotNull
  public Atlas2Header getHeader() {
    return findNotNullChildByClass(Atlas2Header.class);
  }

  @Override
  @NotNull
  public List<Atlas2Region> getRegionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, Atlas2Region.class);
  }

}
