// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.tree;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TreeParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return tree(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // ATTRNAME QUESTION_MARK? COLON value
  public static boolean attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute")) return false;
    if (!nextTokenIs(builder_, ATTRNAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ATTRNAME);
    result_ = result_ && attribute_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, ATTRIBUTE, result_);
    return result_;
  }

  // QUESTION_MARK?
  private static boolean attribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_1")) return false;
    consumeToken(builder_, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // LPAREN task? RPAREN
  public static boolean guard(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guard")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LPAREN);
    result_ = result_ && guard_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RPAREN);
    exit_section_(builder_, marker_, GUARD, result_);
    return result_;
  }

  // task?
  private static boolean guard_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guard_1")) return false;
    task(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // guard* statement
  static boolean guardableTask(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guardableTask")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = guardableTask_0(builder_, level_ + 1);
    result_ = result_ && statement(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // guard*
  private static boolean guardableTask_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guardableTask_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!guard(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "guardableTask_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TIMPORT attribute*
  public static boolean import_$(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_$")) return false;
    if (!nextTokenIs(builder_, TIMPORT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TIMPORT);
    result_ = result_ && import_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, IMPORT, result_);
    return result_;
  }

  // attribute*
  private static boolean import_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attribute(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "import_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TINDENT?
  public static boolean indent(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "indent")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INDENT, "<indent>");
    consumeToken(builder_, TINDENT);
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // indent guardableTask? COMMENT? EOL
  public static boolean line(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "line")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LINE, "<line>");
    result_ = indent(builder_, level_ + 1);
    result_ = result_ && line_1(builder_, level_ + 1);
    result_ = result_ && line_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EOL);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // guardableTask?
  private static boolean line_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "line_1")) return false;
    guardableTask(builder_, level_ + 1);
    return true;
  }

  // COMMENT?
  private static boolean line_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "line_2")) return false;
    consumeToken(builder_, COMMENT);
    return true;
  }

  /* ********************************************************** */
  // TROOT attribute*
  public static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    if (!nextTokenIs(builder_, TROOT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TROOT);
    result_ = result_ && root_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, ROOT, result_);
    return result_;
  }

  // attribute*
  private static boolean root_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attribute(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // task | import | subtree | root
  public static boolean statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STATEMENT, "<statement>");
    result_ = task(builder_, level_ + 1);
    if (!result_) result_ = import_$(builder_, level_ + 1);
    if (!result_) result_ = subtree(builder_, level_ + 1);
    if (!result_) result_ = root(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TSUBTREE attribute*
  public static boolean subtree(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "subtree")) return false;
    if (!nextTokenIs(builder_, TSUBTREE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TSUBTREE);
    result_ = result_ && subtree_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, SUBTREE, result_);
    return result_;
  }

  // attribute*
  private static boolean subtree_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "subtree_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attribute(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "subtree_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SUBTREEREFERENCE QUESTION_MARK?
  public static boolean subtreeref(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "subtreeref")) return false;
    if (!nextTokenIs(builder_, SUBTREEREFERENCE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SUBTREEREFERENCE);
    result_ = result_ && subtreeref_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, SUBTREEREF, result_);
    return result_;
  }

  // QUESTION_MARK?
  private static boolean subtreeref_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "subtreeref_1")) return false;
    consumeToken(builder_, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // (taskname attribute*) | subtreeref
  public static boolean task(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "task")) return false;
    if (!nextTokenIs(builder_, "<task>", SUBTREEREFERENCE, TASK_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TASK, "<task>");
    result_ = task_0(builder_, level_ + 1);
    if (!result_) result_ = subtreeref(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // taskname attribute*
  private static boolean task_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "task_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = taskname(builder_, level_ + 1);
    result_ = result_ && task_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // attribute*
  private static boolean task_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "task_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!attribute(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "task_0_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TASK_NAME QUESTION_MARK?
  public static boolean taskname(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "taskname")) return false;
    if (!nextTokenIs(builder_, TASK_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TASK_NAME);
    result_ = result_ && taskname_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, TASKNAME, result_);
    return result_;
  }

  // QUESTION_MARK?
  private static boolean taskname_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "taskname_1")) return false;
    consumeToken(builder_, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // line*
  static boolean tree(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tree")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!line(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "tree", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TRUE | FALSE | NULL | NUMBER | STRING
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUE, "<value>");
    result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, NULL);
    if (!result_) result_ = consumeToken(builder_, NUMBER);
    if (!result_) result_ = consumeToken(builder_, STRING);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
