// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.mixins.AtlasRegionMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.*;

public class AtlasRegionImpl extends AtlasRegionMixin implements AtlasRegion {

  public AtlasRegionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AtlasElementVisitor visitor) {
    visitor.visitRegion(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AtlasElementVisitor) accept((AtlasElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public AtlasIndex getIndex() {
    return findNotNullChildByClass(AtlasIndex.class);
  }

  @Override
  @NotNull
  public AtlasOffset getOffset() {
    return findNotNullChildByClass(AtlasOffset.class);
  }

  @Override
  @NotNull
  public AtlasOrig getOrig() {
    return findNotNullChildByClass(AtlasOrig.class);
  }

  @Override
  @Nullable
  public AtlasPad getPad() {
    return findChildByClass(AtlasPad.class);
  }

  @Override
  @NotNull
  public AtlasRegionName getRegionName() {
    return findNotNullChildByClass(AtlasRegionName.class);
  }

  @Override
  @NotNull
  public AtlasRotate getRotate() {
    return findNotNullChildByClass(AtlasRotate.class);
  }

  @Override
  @NotNull
  public AtlasSize getSize() {
    return findNotNullChildByClass(AtlasSize.class);
  }

  @Override
  @Nullable
  public AtlasSplit getSplit() {
    return findChildByClass(AtlasSplit.class);
  }

  @Override
  @NotNull
  public AtlasXy getXy() {
    return findNotNullChildByClass(AtlasXy.class);
  }

}
