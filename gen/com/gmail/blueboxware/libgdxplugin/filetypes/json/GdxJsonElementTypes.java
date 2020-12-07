// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.*;

public interface GdxJsonElementTypes {

  IElementType ARRAY = new GdxJsonElementType("ARRAY");
  IElementType JOBJECT = new GdxJsonElementType("JOBJECT");
  IElementType NUMBER_VALUE = new GdxJsonElementType("NUMBER_VALUE");
  IElementType PROPERTY = new GdxJsonElementType("PROPERTY");
  IElementType PROPERTY_NAME = new GdxJsonElementType("PROPERTY_NAME");
  IElementType STRING = new GdxJsonElementType("STRING");
  IElementType VALUE = new GdxJsonElementType("VALUE");

  IElementType ASTERIX = new GdxJsonTokenType("*");
  IElementType BACK_SLASH = new GdxJsonTokenType("\\");
  IElementType BLOCK_COMMENT = new GdxJsonTokenType("BLOCK COMMENT");
  IElementType COLON = new GdxJsonTokenType(":");
  IElementType COMMA = new GdxJsonTokenType(",");
  IElementType DOUBLE_QUOTE = new GdxJsonTokenType("\"");
  IElementType DOUBLE_QUOTED_STRING = new GdxJsonTokenType("QUOTED STRING");
  IElementType LINE_COMMENT = new GdxJsonTokenType("LINE COMMENT");
  IElementType L_BRACKET = new GdxJsonTokenType("[");
  IElementType L_CURLY = new GdxJsonTokenType("{");
  IElementType NUMBER = new GdxJsonTokenType("NUMBER");
  IElementType R_BRACKET = new GdxJsonTokenType("]");
  IElementType R_CURLY = new GdxJsonTokenType("}");
  IElementType SLASH = new GdxJsonTokenType("/");
  IElementType UNQUOTED_STRING = new GdxJsonTokenType("UNQUOTED STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY) {
        return new GdxJsonArrayImpl(node);
      }
      else if (type == JOBJECT) {
        return new GdxJsonJobjectImpl(node);
      }
      else if (type == NUMBER_VALUE) {
        return new GdxJsonNumberValueImpl(node);
      }
      else if (type == PROPERTY) {
        return new GdxJsonPropertyImpl(node);
      }
      else if (type == PROPERTY_NAME) {
        return new GdxJsonPropertyNameImpl(node);
      }
      else if (type == STRING) {
        return new GdxJsonStringImpl(node);
      }
      else if (type == VALUE) {
        return new GdxJsonValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
