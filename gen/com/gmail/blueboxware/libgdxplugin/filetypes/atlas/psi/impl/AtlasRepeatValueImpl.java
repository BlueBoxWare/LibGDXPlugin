// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.*;

public class AtlasRepeatValueImpl extends ASTWrapperPsiElement implements AtlasRepeatValue {

  public AtlasRepeatValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AtlasElementVisitor visitor) {
    visitor.visitRepeatValue(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AtlasElementVisitor) accept((AtlasElementVisitor)visitor);
    else super.accept(visitor);
  }

}
