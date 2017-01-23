// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*;

public class SkinResourcesImpl extends ASTWrapperPsiElement implements SkinResources {

  public SkinResourcesImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SkinElementVisitor visitor) {
    visitor.visitResources(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SkinElementVisitor) accept((SkinElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SkinResource> getResourceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SkinResource.class);
  }

}
