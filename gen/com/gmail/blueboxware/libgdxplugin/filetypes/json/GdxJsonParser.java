// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class GdxJsonParser implements PsiParser, LightPsiParser {

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
    return json(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // '['  separator? array_element* COMMA? ']'
  public static boolean array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array")) return false;
    if (!nextTokenIs(builder_, L_BRACKET)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY, null);
    result_ = consumeToken(builder_, L_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, array_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, array_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, array_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, R_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // separator?
  private static boolean array_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_1")) return false;
    separator(builder_, level_ + 1);
    return true;
  }

  // array_element*
  private static boolean array_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_2", pos_)) break;
    }
    return true;
  }

  // COMMA?
  private static boolean array_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_3")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  /* ********************************************************** */
  // value (separator|&']')
  static boolean array_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = value(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && array_element_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, GdxJsonParser::not_bracket_or_next_value);
    return result_ || pinned_;
  }

  // separator|&']'
  private static boolean array_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = separator(builder_, level_ + 1);
    if (!result_) result_ = array_element_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &']'
  private static boolean array_element_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, R_BRACKET);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' separator? ('}' | object_element* COMMA? '}'?)
  public static boolean jobject(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject")) return false;
    if (!nextTokenIs(builder_, "<object>", L_CURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, JOBJECT, "<object>");
    result_ = consumeToken(builder_, L_CURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, jobject_1(builder_, level_ + 1));
    result_ = pinned_ && jobject_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // separator?
  private static boolean jobject_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_1")) return false;
    separator(builder_, level_ + 1);
    return true;
  }

  // '}' | object_element* COMMA? '}'?
  private static boolean jobject_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, R_CURLY);
    if (!result_) result_ = jobject_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // object_element* COMMA? '}'?
  private static boolean jobject_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_2_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = jobject_2_1_0(builder_, level_ + 1);
    result_ = result_ && jobject_2_1_1(builder_, level_ + 1);
    result_ = result_ && jobject_2_1_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // object_element*
  private static boolean jobject_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_2_1_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!object_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "jobject_2_1_0", pos_)) break;
    }
    return true;
  }

  // COMMA?
  private static boolean jobject_2_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_2_1_1")) return false;
    consumeToken(builder_, COMMA);
    return true;
  }

  // '}'?
  private static boolean jobject_2_1_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "jobject_2_1_2")) return false;
    consumeToken(builder_, R_CURLY);
    return true;
  }

  /* ********************************************************** */
  // value?
  static boolean json(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "json")) return false;
    value(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !('}'|value|separator)
  static boolean not_brace_or_next_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_brace_or_next_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !not_brace_or_next_value_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '}'|value|separator
  private static boolean not_brace_or_next_value_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_brace_or_next_value_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, R_CURLY);
    if (!result_) result_ = value(builder_, level_ + 1);
    if (!result_) result_ = separator(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // !(']'|value|separator)
  static boolean not_bracket_or_next_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_bracket_or_next_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !not_bracket_or_next_value_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ']'|value|separator
  private static boolean not_bracket_or_next_value_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "not_bracket_or_next_value_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, R_BRACKET);
    if (!result_) result_ = value(builder_, level_ + 1);
    if (!result_) result_ = separator(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // property (separator|&'}')
  static boolean object_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_element")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = property(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && object_element_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, GdxJsonParser::not_brace_or_next_value);
    return result_ || pinned_;
  }

  // separator|&'}'
  private static boolean object_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_element_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = separator(builder_, level_ + 1);
    if (!result_) result_ = object_element_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &'}'
  private static boolean object_element_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object_element_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, R_CURLY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // property_name ':' value
  public static boolean property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY, "<property>");
    result_ = property_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, COLON));
    result_ = pinned_ && value(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | unquoted_key_string
  public static boolean property_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_NAME, "<property name>");
    result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = unquoted_key_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<parseSeparator>>
  static boolean separator(PsiBuilder builder_, int level_) {
    return parseSeparator(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | unquoted_value_string
  public static boolean string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING, "<string>");
    result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = unquoted_value_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // (STRING_PART | L_CURLY | L_BRACKET | ASTERIX | SLASH)
  //     (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH))*
  static boolean unquoted_key_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_key_string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = unquoted_key_string_0(builder_, level_ + 1);
    result_ = result_ && unquoted_key_string_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // STRING_PART | L_CURLY | L_BRACKET | ASTERIX | SLASH
  private static boolean unquoted_key_string_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_key_string_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_PART);
    if (!result_) result_ = consumeToken(builder_, L_CURLY);
    if (!result_) result_ = consumeToken(builder_, L_BRACKET);
    if (!result_) result_ = consumeToken(builder_, ASTERIX);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    return result_;
  }

  // (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH))*
  private static boolean unquoted_key_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_key_string_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!unquoted_key_string_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "unquoted_key_string_1", pos_)) break;
    }
    return true;
  }

  // <<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH)
  private static boolean unquoted_key_string_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_key_string_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = no_comment_or_newline(builder_, level_ + 1);
    result_ = result_ && unquoted_key_string_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH
  private static boolean unquoted_key_string_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_key_string_1_0_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_PART);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, L_CURLY);
    if (!result_) result_ = consumeToken(builder_, L_BRACKET);
    if (!result_) result_ = consumeToken(builder_, R_BRACKET);
    if (!result_) result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, ASTERIX);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    return result_;
  }

  /* ********************************************************** */
  // (STRING_PART | L_CURLY | ASTERIX | SLASH | BACK_SLASH)
  //     (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH))*
  static boolean unquoted_value_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_value_string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = unquoted_value_string_0(builder_, level_ + 1);
    result_ = result_ && unquoted_value_string_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // STRING_PART | L_CURLY | ASTERIX | SLASH | BACK_SLASH
  private static boolean unquoted_value_string_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_value_string_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_PART);
    if (!result_) result_ = consumeToken(builder_, L_CURLY);
    if (!result_) result_ = consumeToken(builder_, ASTERIX);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    if (!result_) result_ = consumeToken(builder_, BACK_SLASH);
    return result_;
  }

  // (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH))*
  private static boolean unquoted_value_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_value_string_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!unquoted_value_string_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "unquoted_value_string_1", pos_)) break;
    }
    return true;
  }

  // <<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH)
  private static boolean unquoted_value_string_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_value_string_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = no_comment_or_newline(builder_, level_ + 1);
    result_ = result_ && unquoted_value_string_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH
  private static boolean unquoted_value_string_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unquoted_value_string_1_0_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_PART);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, COLON);
    if (!result_) result_ = consumeToken(builder_, L_CURLY);
    if (!result_) result_ = consumeToken(builder_, L_BRACKET);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_QUOTE);
    if (!result_) result_ = consumeToken(builder_, ASTERIX);
    if (!result_) result_ = consumeToken(builder_, SLASH);
    return result_;
  }

  /* ********************************************************** */
  // jobject | array | string
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUE, "<value>");
    result_ = jobject(builder_, level_ + 1);
    if (!result_) result_ = array(builder_, level_ + 1);
    if (!result_) result_ = string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
