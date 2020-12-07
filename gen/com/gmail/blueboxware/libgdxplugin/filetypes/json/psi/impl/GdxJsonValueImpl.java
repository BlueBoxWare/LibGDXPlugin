// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins.GdxJsonValueMixin;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*;

public class GdxJsonValueImpl extends GdxJsonValueMixin implements GdxJsonValue {

  public GdxJsonValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull GdxJsonElementVisitor visitor) {
    visitor.visitValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof GdxJsonElementVisitor) accept((GdxJsonElementVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public GdxJsonArray getArray() {
    return findChildByClass(GdxJsonArray.class);
  }

  @Override
  @Nullable
  public GdxJsonJobject getJobject() {
    return findChildByClass(GdxJsonJobject.class);
  }

  @Override
  @Nullable
  public GdxJsonNumberValue getNumberValue() {
    return findChildByClass(GdxJsonNumberValue.class);
  }

  @Override
  @Nullable
  public GdxJsonString getString() {
    return findChildByClass(GdxJsonString.class);
  }

}
