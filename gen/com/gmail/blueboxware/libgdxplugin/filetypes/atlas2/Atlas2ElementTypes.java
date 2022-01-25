// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.impl.*;

public interface Atlas2ElementTypes {

  IElementType FIELD = new Atlas2ElementType("FIELD");
  IElementType HEADER = new Atlas2ElementType("HEADER");
  IElementType KEY = new Atlas2ElementType("KEY");
  IElementType PAGE = new Atlas2ElementType("PAGE");
  IElementType REGION = new Atlas2ElementType("REGION");
  IElementType VALUE = new Atlas2ElementType("VALUE");

  IElementType COLON = new Atlas2TokenType(":");
  IElementType COMMA = new Atlas2TokenType(",");
  IElementType EMPTY_LINE = new Atlas2TokenType("");
  IElementType THEADER = new Atlas2TokenType("header");
  IElementType TKEY = new Atlas2TokenType("key");
  IElementType TVALUE = new Atlas2TokenType("value");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == FIELD) {
        return new Atlas2FieldImpl(node);
      }
      else if (type == HEADER) {
        return new Atlas2HeaderImpl(node);
      }
      else if (type == KEY) {
        return new Atlas2KeyImpl(node);
      }
      else if (type == PAGE) {
        return new Atlas2PageImpl(node);
      }
      else if (type == REGION) {
        return new Atlas2RegionImpl(node);
      }
      else if (type == VALUE) {
        return new Atlas2ValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
