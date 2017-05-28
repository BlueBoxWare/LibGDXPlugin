// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.BitmapFontFontCharImpl;

public interface BitmapFontKerning extends PropertyContainer {

  @NotNull
  List<BitmapFontProperty> getPropertyList();

  @Nullable
  BitmapFontFontCharImpl getFirstCharacter();

  @Nullable
  BitmapFontFontCharImpl getSecondCharacter();

}
