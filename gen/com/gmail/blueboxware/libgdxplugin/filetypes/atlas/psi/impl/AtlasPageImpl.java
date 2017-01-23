// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.mixins.AtlasPageMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.*;

public class AtlasPageImpl extends AtlasPageMixin implements AtlasPage {

  public AtlasPageImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AtlasElementVisitor visitor) {
    visitor.visitPage(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AtlasElementVisitor) accept((AtlasElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public AtlasFilter getFilter() {
    return findNotNullChildByClass(AtlasFilter.class);
  }

  @Override
  @NotNull
  public AtlasFormat getFormat() {
    return findNotNullChildByClass(AtlasFormat.class);
  }

  @Override
  @NotNull
  public List<AtlasRegion> getRegionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AtlasRegion.class);
  }

  @Override
  @NotNull
  public AtlasRepeat getRepeat() {
    return findNotNullChildByClass(AtlasRepeat.class);
  }

  @Override
  @Nullable
  public AtlasSize getSize() {
    return findChildByClass(AtlasSize.class);
  }

  @Override
  @NotNull
  public AtlasValue getPageName() {
    return findNotNullChildByClass(AtlasValue.class);
  }

}
