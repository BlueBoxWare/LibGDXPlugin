// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.psi.impl.*;

public interface BitmapFontElementTypes {

  IElementType CHARS = new BitmapFontElementType("CHARS");
  IElementType COMMON = new BitmapFontElementType("COMMON");
  IElementType FONT_CHAR = new BitmapFontElementType("FONT_CHAR");
  IElementType INFO = new BitmapFontElementType("INFO");
  IElementType KERNING = new BitmapFontElementType("KERNING");
  IElementType KERNINGS = new BitmapFontElementType("KERNINGS");
  IElementType KEY = new BitmapFontElementType("KEY");
  IElementType PAGE_DEFINITION = new BitmapFontElementType("PAGE_DEFINITION");
  IElementType PROPERTY = new BitmapFontElementType("PROPERTY");
  IElementType VALUE = new BitmapFontElementType("VALUE");

  IElementType COMMA = new BitmapFontTokenType(",");
  IElementType DOUBLE_QUOTED_STRING = new BitmapFontTokenType("DOUBLE_QUOTED_STRING");
  IElementType EOL = new BitmapFontTokenType("EOL");
  IElementType EQUALS = new BitmapFontTokenType("=");
  IElementType SINGLE_QUOTED_STRING = new BitmapFontTokenType("SINGLE_QUOTED_STRING");
  IElementType UNQUOTED_STRING = new BitmapFontTokenType("UNQUOTED_STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CHARS) {
        return new BitmapFontCharsImpl(node);
      }
      else if (type == COMMON) {
        return new BitmapFontCommonImpl(node);
      }
      else if (type == FONT_CHAR) {
        return new BitmapFontFontCharImpl(node);
      }
      else if (type == INFO) {
        return new BitmapFontInfoImpl(node);
      }
      else if (type == KERNING) {
        return new BitmapFontKerningImpl(node);
      }
      else if (type == KERNINGS) {
        return new BitmapFontKerningsImpl(node);
      }
      else if (type == KEY) {
        return new BitmapFontKeyImpl(node);
      }
      else if (type == PAGE_DEFINITION) {
        return new BitmapFontPageDefinitionImpl(node);
      }
      else if (type == PROPERTY) {
        return new BitmapFontPropertyImpl(node);
      }
      else if (type == VALUE) {
        return new BitmapFontValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
