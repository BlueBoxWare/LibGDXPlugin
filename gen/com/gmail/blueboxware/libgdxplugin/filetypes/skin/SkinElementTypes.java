// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.*;

public interface SkinElementTypes {

  IElementType ARRAY = new SkinElementType("ARRAY");
  IElementType CLASS_NAME = new SkinElementType("CLASS_NAME");
  IElementType CLASS_SPECIFICATION = new SkinElementType("CLASS_SPECIFICATION");
  IElementType OBJECT = new SkinElementType("OBJECT");
  IElementType PROPERTY = new SkinElementType("PROPERTY");
  IElementType PROPERTY_NAME = new SkinElementType("PROPERTY_NAME");
  IElementType PROPERTY_VALUE = new SkinElementType("PROPERTY_VALUE");
  IElementType RESOURCE = new SkinElementType("RESOURCE");
  IElementType RESOURCES = new SkinElementType("RESOURCES");
  IElementType RESOURCE_NAME = new SkinElementType("RESOURCE_NAME");
  IElementType STRING_LITERAL = new SkinElementType("STRING_LITERAL");
  IElementType VALUE = new SkinElementType("VALUE");

  IElementType BLOCK_COMMENT = new SkinTokenType("BLOCK_COMMENT");
  IElementType COLON = new SkinTokenType(":");
  IElementType COMMA = new SkinTokenType(",");
  IElementType DOUBLE_QUOTED_STRING = new SkinTokenType("DOUBLE_QUOTED_STRING");
  IElementType LINE_COMMENT = new SkinTokenType("LINE_COMMENT");
  IElementType L_BRACKET = new SkinTokenType("[");
  IElementType L_CURLY = new SkinTokenType("{");
  IElementType R_BRACKET = new SkinTokenType("]");
  IElementType R_CURLY = new SkinTokenType("}");
  IElementType UNQUOTED_STRING = new SkinTokenType("UNQUOTED_STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY) {
        return new SkinArrayImpl(node);
      }
      else if (type == CLASS_NAME) {
        return new SkinClassNameImpl(node);
      }
      else if (type == CLASS_SPECIFICATION) {
        return new SkinClassSpecificationImpl(node);
      }
      else if (type == OBJECT) {
        return new SkinObjectImpl(node);
      }
      else if (type == PROPERTY) {
        return new SkinPropertyImpl(node);
      }
      else if (type == PROPERTY_NAME) {
        return new SkinPropertyNameImpl(node);
      }
      else if (type == PROPERTY_VALUE) {
        return new SkinPropertyValueImpl(node);
      }
      else if (type == RESOURCE) {
        return new SkinResourceImpl(node);
      }
      else if (type == RESOURCES) {
        return new SkinResourcesImpl(node);
      }
      else if (type == RESOURCE_NAME) {
        return new SkinResourceNameImpl(node);
      }
      else if (type == STRING_LITERAL) {
        return new SkinStringLiteralImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
