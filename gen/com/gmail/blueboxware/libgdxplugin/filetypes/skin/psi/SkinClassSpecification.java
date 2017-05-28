// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;

public interface SkinClassSpecification extends SkinNamedElement {

  @NotNull
  SkinClassName getClassName();

  @Nullable
  SkinResources getResources();

  @Nullable
  PsiClass resolveClass();

  @NotNull
  String getClassNameAsString();

  @NotNull
  List<SkinResource> getResourcesAsList();

  @NotNull
  List<SkinResource> getResourcesAsList(PsiElement beforeElement);

  @NotNull
  List<String> getResourceNames();

  @Nullable
  SkinResource getResource(String name);

  void addComment(PsiComment comment);

}
