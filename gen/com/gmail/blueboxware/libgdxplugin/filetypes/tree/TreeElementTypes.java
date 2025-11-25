// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.impl.*;

public interface TreeElementTypes {

  IElementType ATTRIBUTE = new TreeElementType("ATTRIBUTE");
  IElementType GUARD = new TreeElementType("GUARD");
  IElementType LINE = new TreeElementType("LINE");
  IElementType SUBTREEREF = new TreeElementType("SUBTREEREF");
  IElementType TASK = new TreeElementType("TASK");
  IElementType TASKNAME = new TreeElementType("TASKNAME");
  IElementType VALUE = new TreeElementType("VALUE");

  IElementType ATTRNAME = new TreeTokenType("attribute name");
  IElementType COLON = new TreeTokenType(":");
  IElementType COMMENT = new TreeTokenType("comment");
  IElementType EOL = new TreeTokenType("end of line");
  IElementType FALSE = new TreeTokenType("false");
  IElementType IMPORT = new TreeTokenType("import");
  IElementType INDENT = new TreeTokenType("indent");
  IElementType LPAREN = new TreeTokenType("(");
  IElementType NULL = new TreeTokenType("null");
  IElementType NUMBER = new TreeTokenType("number");
  IElementType QUESTION_MARK = new TreeTokenType("?");
  IElementType ROOT = new TreeTokenType("root");
  IElementType RPAREN = new TreeTokenType(")");
  IElementType STRING = new TreeTokenType("string");
  IElementType SUBTREE = new TreeTokenType("subtree");
  IElementType SUBTREEREFERENCE = new TreeTokenType("subtree ref");
  IElementType TASK_NAME = new TreeTokenType("task name");
  IElementType TRUE = new TreeTokenType("true");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ATTRIBUTE) {
        return new PsiTreeAttributeImpl(node);
      }
      else if (type == GUARD) {
        return new PsiTreeGuardImpl(node);
      }
      else if (type == LINE) {
        return new PsiTreeLineImpl(node);
      }
      else if (type == SUBTREEREF) {
        return new PsiTreeSubtreerefImpl(node);
      }
      else if (type == TASK) {
        return new PsiTreeTaskImpl(node);
      }
      else if (type == TASKNAME) {
        return new PsiTreeTasknameImpl(node);
      }
      else if (type == VALUE) {
        return new PsiTreeValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
