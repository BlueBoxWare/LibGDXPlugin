// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2Element;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2NamedElement;

public interface Atlas2Field extends Atlas2Element, Atlas2NamedElement {

  @NotNull
  List<Atlas2Value> getValueList();

  @NotNull
  Atlas2Key getKeyElement();

  @NotNull
  String getKey();

  @NotNull
  List<Atlas2Value> getValueElements();

  @NotNull
  List<String> getValues();

}
