// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi;

import org.jetbrains.annotations.Nullable;

public interface GdxJsonValue extends GdxJsonElement {

  @Nullable
  GdxJsonArray getArray();

  @Nullable
  GdxJsonJobject getJobject();

  @Nullable
  GdxJsonString getString();

  @Nullable
  GdxJsonElement getValue();

  boolean isPropertyValue();

}
