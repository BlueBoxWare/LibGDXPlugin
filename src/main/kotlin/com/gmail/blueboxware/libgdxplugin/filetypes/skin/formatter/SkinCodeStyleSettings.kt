package com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.json.JsonBundle
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/171.2152/json/src/com/intellij/json/formatter/JsonCodeStyleSettings.java
 *
 */


class SkinCodeStyleSettings(container: CodeStyleSettings) : CustomCodeStyleSettings(LibGDXSkinLanguage.INSTANCE.id, container) {

  companion object {
    val DO_NOT_ALIGN_PROPERTY = PropertyAlignment.DO_NOT_ALIGN.id
    val ALIGN_PROPERTY_ON_VALUE = PropertyAlignment.ALIGN_ON_VALUE.id
    val ALIGN_PROPERTY_ON_COLON = PropertyAlignment.ALIGN_ON_COLON.id
  }

  @JvmField
  var SPACE_BEFORE_COLON = false
  @JvmField
  var SPACE_AFTER_COLON = true

  @JvmField
  var OBJECT_WRAPPING = CommonCodeStyleSettings.WRAP_ALWAYS
  @JvmField
  var ARRAY_WRAPPING = CommonCodeStyleSettings.WRAP_ALWAYS

  @JvmField
  var PROPERTY_ALIGNMENT = DO_NOT_ALIGN_PROPERTY

  @JvmField
  var DO_NOT_WRAP_COLORS = true

  enum class PropertyAlignment(val description: String, val id: Int) {
    DO_NOT_ALIGN(JsonBundle.message("formatter.align.properties.none"), 0),
    ALIGN_ON_VALUE(JsonBundle.message("formatter.align.properties.on.value"), 1),
    ALIGN_ON_COLON(JsonBundle.message("formatter.align.properties.on.colon"), 2);
  }

}