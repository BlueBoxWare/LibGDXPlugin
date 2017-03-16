// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.psi.impl.*;

public interface AtlasElementTypes {

  IElementType FILTER = new AtlasElementType("FILTER");
  IElementType FILTER_VALUE = new AtlasElementType("FILTER_VALUE");
  IElementType FORMAT = new AtlasElementType("FORMAT");
  IElementType FORMAT_VALUE = new AtlasElementType("FORMAT_VALUE");
  IElementType INDEX = new AtlasElementType("INDEX");
  IElementType OFFSET = new AtlasElementType("OFFSET");
  IElementType ORIG = new AtlasElementType("ORIG");
  IElementType PAD = new AtlasElementType("PAD");
  IElementType PAGE = new AtlasElementType("PAGE");
  IElementType REGION = new AtlasElementType("REGION");
  IElementType REGION_NAME = new AtlasElementType("REGION_NAME");
  IElementType REPEAT = new AtlasElementType("REPEAT");
  IElementType REPEAT_VALUE = new AtlasElementType("REPEAT_VALUE");
  IElementType ROTATE = new AtlasElementType("ROTATE");
  IElementType ROTATE_VALUE = new AtlasElementType("ROTATE_VALUE");
  IElementType SIZE = new AtlasElementType("SIZE");
  IElementType SPLIT = new AtlasElementType("SPLIT");
  IElementType VALUE = new AtlasElementType("VALUE");
  IElementType XY = new AtlasElementType("XY");

  IElementType COLON = new AtlasTokenType(":");
  IElementType COMMA = new AtlasTokenType(",");
  IElementType EOL = new AtlasTokenType("EOL");
  IElementType STRING = new AtlasTokenType("STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == FILTER) {
        return new AtlasFilterImpl(node);
      }
      else if (type == FILTER_VALUE) {
        return new AtlasFilterValueImpl(node);
      }
      else if (type == FORMAT) {
        return new AtlasFormatImpl(node);
      }
      else if (type == FORMAT_VALUE) {
        return new AtlasFormatValueImpl(node);
      }
      else if (type == INDEX) {
        return new AtlasIndexImpl(node);
      }
      else if (type == OFFSET) {
        return new AtlasOffsetImpl(node);
      }
      else if (type == ORIG) {
        return new AtlasOrigImpl(node);
      }
      else if (type == PAD) {
        return new AtlasPadImpl(node);
      }
      else if (type == PAGE) {
        return new AtlasPageImpl(node);
      }
      else if (type == REGION) {
        return new AtlasRegionImpl(node);
      }
      else if (type == REGION_NAME) {
        return new AtlasRegionNameImpl(node);
      }
      else if (type == REPEAT) {
        return new AtlasRepeatImpl(node);
      }
      else if (type == REPEAT_VALUE) {
        return new AtlasRepeatValueImpl(node);
      }
      else if (type == ROTATE) {
        return new AtlasRotateImpl(node);
      }
      else if (type == ROTATE_VALUE) {
        return new AtlasRotateValueImpl(node);
      }
      else if (type == SIZE) {
        return new AtlasSizeImpl(node);
      }
      else if (type == SPLIT) {
        return new AtlasSplitImpl(node);
      }
      else if (type == VALUE) {
        return new AtlasValueImpl(node);
      }
      else if (type == XY) {
        return new AtlasXyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
