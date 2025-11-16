// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class Atlas2Parser implements PsiParser, LightPsiParser {

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
    return atlas2(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // EMPTY_LINE* field* page*
  static boolean atlas2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atlas2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = atlas2_0(builder_, level_ + 1);
    result_ = result_ && atlas2_1(builder_, level_ + 1);
    result_ = result_ && atlas2_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // EMPTY_LINE*
  private static boolean atlas2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atlas2_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, EMPTY_LINE)) break;
      if (!empty_element_parsed_guard_(builder_, "atlas2_0", pos_)) break;
    }
    return true;
  }

  // field*
  private static boolean atlas2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atlas2_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!field(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "atlas2_1", pos_)) break;
    }
    return true;
  }

  // page*
  private static boolean atlas2_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atlas2_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!page(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "atlas2_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // key  COLON (value | COMMA)*
  public static boolean field(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field")) return false;
    if (!nextTokenIs(builder_, TKEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = key(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && field_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, FIELD, result_);
    return result_;
  }

  // (value | COMMA)*
  private static boolean field_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!field_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "field_2", pos_)) break;
    }
    return true;
  }

  // value | COMMA
  private static boolean field_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_2_0")) return false;
    boolean result_;
    result_ = value(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    return result_;
  }

  /* ********************************************************** */
  // THEADER
  public static boolean header(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "header")) return false;
    if (!nextTokenIs(builder_, THEADER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, THEADER);
    exit_section_(builder_, marker_, HEADER, result_);
    return result_;
  }

  /* ********************************************************** */
  // TKEY
  public static boolean key(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "key")) return false;
    if (!nextTokenIs(builder_, "<keyElement>", TKEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, KEY, "<keyElement>");
    result_ = consumeToken(builder_, TKEY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // header field* region* (EMPTY_LINE | <<eof>>)
  public static boolean page(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page")) return false;
    if (!nextTokenIs(builder_, THEADER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = header(builder_, level_ + 1);
    result_ = result_ && page_1(builder_, level_ + 1);
    result_ = result_ && page_2(builder_, level_ + 1);
    result_ = result_ && page_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, PAGE, result_);
    return result_;
  }

  // field*
  private static boolean page_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!field(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "page_1", pos_)) break;
    }
    return true;
  }

  // region*
  private static boolean page_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!region(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "page_2", pos_)) break;
    }
    return true;
  }

  // EMPTY_LINE | <<eof>>
  private static boolean page_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EMPTY_LINE);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // header field*
  public static boolean region(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "region")) return false;
    if (!nextTokenIs(builder_, THEADER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = header(builder_, level_ + 1);
    result_ = result_ && region_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, REGION, result_);
    return result_;
  }

  // field*
  private static boolean region_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "region_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!field(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "region_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TVALUE
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    if (!nextTokenIs(builder_, TVALUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TVALUE);
    exit_section_(builder_, marker_, VALUE, result_);
    return result_;
  }

}
