// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasProperty;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.*;

public class AtlasPadImpl extends AtlasProperty implements AtlasPad {

  public AtlasPadImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AtlasElementVisitor visitor) {
    visitor.visitPad(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AtlasElementVisitor) accept((AtlasElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AtlasValue> getValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AtlasValue.class);
  }

}
