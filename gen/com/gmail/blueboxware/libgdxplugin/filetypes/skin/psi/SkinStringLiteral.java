// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SkinStringLiteral extends SkinValue {

  @NotNull
  String getValue();

  void setValue(String string, Character quotationChar);

  @Nullable
  SkinPropertyName asPropertyName();

  @Nullable
  Character getQuotationChar();

}
