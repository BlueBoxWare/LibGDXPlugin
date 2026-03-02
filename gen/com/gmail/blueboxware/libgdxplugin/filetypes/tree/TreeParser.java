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
  // attributeName QUESTION_MARK? COLON value
  public static boolean attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute")) return false;
    if (!nextTokenIs(builder_, ATTRNAME)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTRIBUTE, null);
    result_ = attributeName(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, attribute_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, COLON)) && result_;
    result_ = pinned_ && value(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // QUESTION_MARK?
  private static boolean attribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_1")) return false;
    consumeToken(builder_, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // ATTRNAME
  public static boolean attributeName(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attributeName")) return false;
    if (!nextTokenIs(builder_, ATTRNAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ATTRNAME);
    exit_section_(builder_, marker_, ATTRIBUTE_NAME, result_);
    return result_;
  }

  /* ********************************************************** */
  // LPAREN task? RPAREN
  public static boolean guard(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guard")) return false;
    if (!nextTokenIs(builder_, LPAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GUARD, null);
    result_ = consumeToken(builder_, LPAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, guard_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RPAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // task?
  private static boolean guard_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guard_1")) return false;
    task(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // guard* statement?
  static boolean guardableTask(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guardableTask")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = guardableTask_0(builder_, level_ + 1);
    result_ = result_ && guardableTask_1(builder_, level_ + 1);
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

  // statement?
  private static boolean guardableTask_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "guardableTask_1")) return false;
    statement(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TIMPORT attribute*
  public static boolean import_$(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "import_$")) return false;
    if (!nextTokenIs(builder_, TIMPORT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IMPORT, null);
    result_ = consumeToken(builder_, TIMPORT);
    pinned_ = result_; // pin = 1
    result_ = result_ && import_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
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
  // (TINDENT <<eof>>) | line
  static boolean line_(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "line_")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = line__0(builder_, level_ + 1);
    if (!result_) result_ = line(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TINDENT <<eof>>
  private static boolean line__0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "line__0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TINDENT);
    result_ = result_ && eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TROOT attribute*
  public static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    if (!nextTokenIs(builder_, TROOT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ROOT, null);
    result_ = consumeToken(builder_, TROOT);
    pinned_ = result_; // pin = 1
    result_ = result_ && root_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
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
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STATEMENT, "<task>");
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
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUBTREE, null);
    result_ = consumeToken(builder_, TSUBTREE);
    pinned_ = result_; // pin = 1
    result_ = result_ && subtree_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
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
  // (TASK_NAME | <<parseMissingTaskName>>) QUESTION_MARK?
  public static boolean taskname(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "taskname")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TASKNAME, "<taskname>");
    result_ = taskname_0(builder_, level_ + 1);
    result_ = result_ && taskname_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TASK_NAME | <<parseMissingTaskName>>
  private static boolean taskname_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "taskname_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TASK_NAME);
    if (!result_) result_ = parseMissingTaskName(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // QUESTION_MARK?
  private static boolean taskname_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "taskname_1")) return false;
    consumeToken(builder_, QUESTION_MARK);
    return true;
  }

  /* ********************************************************** */
  // line_*
  static boolean tree(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tree")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!line_(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "tree", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // vkeyword | vnumber | vstring
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUE, "<value>");
    result_ = vkeyword(builder_, level_ + 1);
    if (!result_) result_ = vnumber(builder_, level_ + 1);
    if (!result_) result_ = vstring(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TRUE | FALSE | NULL
  public static boolean vkeyword(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "vkeyword")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VKEYWORD, "<vkeyword>");
    result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    if (!result_) result_ = consumeToken(builder_, NULL);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER
  public static boolean vnumber(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "vnumber")) return false;
    if (!nextTokenIs(builder_, NUMBER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, NUMBER);
    exit_section_(builder_, marker_, VNUMBER, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING
  public static boolean vstring(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "vstring")) return false;
    if (!nextTokenIs(builder_, STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, STRING);
    exit_section_(builder_, marker_, VSTRING, result_);
    return result_;
  }

}
