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

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return json(b, l + 1);
  }

  /* ********************************************************** */
  // '['  separator? array_element* COMMA? ']'
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, L_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY, null);
    r = consumeToken(b, L_BRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, array_1(b, l + 1));
    r = p && report_error_(b, array_2(b, l + 1)) && r;
    r = p && report_error_(b, array_3(b, l + 1)) && r;
    r = p && consumeToken(b, R_BRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // separator?
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    separator(b, l + 1);
    return true;
  }

  // array_element*
  private static boolean array_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_2", c)) break;
    }
    return true;
  }

  // COMMA?
  private static boolean array_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_3")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // value (separator|&']')
  static boolean array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = value(b, l + 1);
    p = r; // pin = 1
    r = r && array_element_1(b, l + 1);
    exit_section_(b, l, m, r, p, GdxJsonParser::not_bracket_or_next_value);
    return r || p;
  }

  // separator|&']'
  private static boolean array_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator(b, l + 1);
    if (!r) r = array_element_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &']'
  private static boolean array_element_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, R_BRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '{' separator? ('}' | object_element* COMMA? '}'?)
  public static boolean jobject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject")) return false;
    if (!nextTokenIs(b, "<object>", L_CURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, JOBJECT, "<object>");
    r = consumeToken(b, L_CURLY);
    p = r; // pin = 1
    r = r && report_error_(b, jobject_1(b, l + 1));
    r = p && jobject_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // separator?
  private static boolean jobject_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1")) return false;
    separator(b, l + 1);
    return true;
  }

  // '}' | object_element* COMMA? '}'?
  private static boolean jobject_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, R_CURLY);
    if (!r) r = jobject_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // object_element* COMMA? '}'?
  private static boolean jobject_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = jobject_2_1_0(b, l + 1);
    r = r && jobject_2_1_1(b, l + 1);
    r = r && jobject_2_1_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // object_element*
  private static boolean jobject_2_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2_1_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!object_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_2_1_0", c)) break;
    }
    return true;
  }

  // COMMA?
  private static boolean jobject_2_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2_1_1")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // '}'?
  private static boolean jobject_2_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2_1_2")) return false;
    consumeToken(b, R_CURLY);
    return true;
  }

  /* ********************************************************** */
  // value?
  static boolean json(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json")) return false;
    value(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !('}'|value|separator)
  static boolean not_brace_or_next_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_brace_or_next_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !not_brace_or_next_value_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '}'|value|separator
  private static boolean not_brace_or_next_value_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_brace_or_next_value_0")) return false;
    boolean r;
    r = consumeToken(b, R_CURLY);
    if (!r) r = value(b, l + 1);
    if (!r) r = separator(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // !(']'|value|separator)
  static boolean not_bracket_or_next_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_bracket_or_next_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !not_bracket_or_next_value_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ']'|value|separator
  private static boolean not_bracket_or_next_value_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_bracket_or_next_value_0")) return false;
    boolean r;
    r = consumeToken(b, R_BRACKET);
    if (!r) r = value(b, l + 1);
    if (!r) r = separator(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // property (separator|&'}')
  static boolean object_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_element")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = property(b, l + 1);
    p = r; // pin = 1
    r = r && object_element_1(b, l + 1);
    exit_section_(b, l, m, r, p, GdxJsonParser::not_brace_or_next_value);
    return r || p;
  }

  // separator|&'}'
  private static boolean object_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_element_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator(b, l + 1);
    if (!r) r = object_element_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &'}'
  private static boolean object_element_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_element_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, R_CURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // property_name ':' value
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = property_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | unquoted_key_string
  public static boolean property_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_NAME, "<property name>");
    r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = unquoted_key_string(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<parseSeparator>>
  static boolean separator(PsiBuilder b, int l) {
    return parseSeparator(b, l + 1);
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | unquoted_value_string
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING, "<string>");
    r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = unquoted_value_string(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (STRING_PART | L_CURLY | L_BRACKET | ASTERIX | SLASH)
  //     (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH))*
  static boolean unquoted_key_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unquoted_key_string_0(b, l + 1);
    r = r && unquoted_key_string_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | L_CURLY | L_BRACKET | ASTERIX | SLASH
  private static boolean unquoted_key_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, L_BRACKET);
    if (!r) r = consumeToken(b, ASTERIX);
    if (!r) r = consumeToken(b, SLASH);
    return r;
  }

  // (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH))*
  private static boolean unquoted_key_string_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!unquoted_key_string_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unquoted_key_string_1", c)) break;
    }
    return true;
  }

  // <<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH)
  private static boolean unquoted_key_string_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = no_comment_or_newline(b, l + 1);
    r = r && unquoted_key_string_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | DOUBLE_QUOTED_STRING | L_CURLY | L_BRACKET | R_BRACKET | COMMA | ASTERIX | SLASH
  private static boolean unquoted_key_string_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string_1_0_1")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, L_BRACKET);
    if (!r) r = consumeToken(b, R_BRACKET);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, ASTERIX);
    if (!r) r = consumeToken(b, SLASH);
    return r;
  }

  /* ********************************************************** */
  // (STRING_PART | L_CURLY | ASTERIX | SLASH | BACK_SLASH)
  //     (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH))*
  static boolean unquoted_value_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unquoted_value_string_0(b, l + 1);
    r = r && unquoted_value_string_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | L_CURLY | ASTERIX | SLASH | BACK_SLASH
  private static boolean unquoted_value_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, ASTERIX);
    if (!r) r = consumeToken(b, SLASH);
    if (!r) r = consumeToken(b, BACK_SLASH);
    return r;
  }

  // (<<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH))*
  private static boolean unquoted_value_string_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!unquoted_value_string_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unquoted_value_string_1", c)) break;
    }
    return true;
  }

  // <<no_comment_or_newline>> (STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH)
  private static boolean unquoted_value_string_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = no_comment_or_newline(b, l + 1);
    r = r && unquoted_value_string_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | DOUBLE_QUOTED_STRING | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE | ASTERIX | SLASH
  private static boolean unquoted_value_string_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string_1_0_1")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, L_BRACKET);
    if (!r) r = consumeToken(b, DOUBLE_QUOTE);
    if (!r) r = consumeToken(b, ASTERIX);
    if (!r) r = consumeToken(b, SLASH);
    return r;
  }

  /* ********************************************************** */
  // jobject | array | string
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = jobject(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = string(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
