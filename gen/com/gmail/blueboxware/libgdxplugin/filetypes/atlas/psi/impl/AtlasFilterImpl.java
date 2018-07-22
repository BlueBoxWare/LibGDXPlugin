// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl;

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasProperty;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasElementVisitor;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasFilter;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.AtlasFilterValue;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AtlasFilterImpl extends AtlasProperty implements AtlasFilter {

  public AtlasFilterImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AtlasElementVisitor visitor) {
    visitor.visitFilter(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AtlasElementVisitor) accept((AtlasElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AtlasFilterValue> getFilterValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AtlasFilterValue.class);
  }

}
