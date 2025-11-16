// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface Atlas2Page extends Atlas2PageOwner, Atlas2NamedElement {

  @NotNull
  List<Atlas2Field> getFieldList();

  @NotNull
  Atlas2Header getHeader();

  @NotNull
  List<Atlas2Region> getRegionList();

}
