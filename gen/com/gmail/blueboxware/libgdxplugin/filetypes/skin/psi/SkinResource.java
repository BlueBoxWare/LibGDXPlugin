// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import java.awt.Color;

public interface SkinResource extends SkinNamedElement {

  @NotNull
  SkinResourceName getResourceName();

  @Nullable
  SkinValue getValue();

  @NotNull
  String getName();

  @Nullable
  SkinClassSpecification getClassSpecification();

  @Nullable
  SkinObject getObject();

  @Nullable
  SkinStringLiteral getString();

  @Nullable
  SkinResource findDefinition();

  @Nullable
  Color asColor(boolean force);

}
