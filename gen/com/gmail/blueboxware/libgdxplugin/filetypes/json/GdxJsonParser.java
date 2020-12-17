// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.json;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserUtil.*;

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
  // '['  NEWLINE* value?  (separator  value)* separator?  ']'
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, L_BRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_BRACKET);
    r = r && array_1(b, l + 1);
    r = r && array_2(b, l + 1);
    r = r && array_3(b, l + 1);
    r = r && array_4(b, l + 1);
    r = r && consumeToken(b, R_BRACKET);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // NEWLINE*
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "array_1", c)) break;
    }
    return true;
  }

  // value?
  private static boolean array_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_2")) return false;
    value(b, l + 1);
    return true;
  }

  // (separator  value)*
  private static boolean array_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_3", c)) break;
    }
    return true;
  }

  // separator  value
  private static boolean array_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator(b, l + 1);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // separator?
  private static boolean array_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_4")) return false;
    separator(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '{' separator? '}' | '{' separator? property?  (separator  property)* COMMA? NEWLINE*'}'
  public static boolean jobject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject")) return false;
    if (!nextTokenIs(b, "<object>", L_CURLY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JOBJECT, "<object>");
    r = jobject_0(b, l + 1);
    if (!r) r = jobject_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '{' separator? '}'
  private static boolean jobject_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_CURLY);
    r = r && jobject_0_1(b, l + 1);
    r = r && consumeToken(b, R_CURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // separator?
  private static boolean jobject_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_0_1")) return false;
    separator(b, l + 1);
    return true;
  }

  // '{' separator? property?  (separator  property)* COMMA? NEWLINE*'}'
  private static boolean jobject_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, L_CURLY);
    r = r && jobject_1_1(b, l + 1);
    r = r && jobject_1_2(b, l + 1);
    r = r && jobject_1_3(b, l + 1);
    r = r && jobject_1_4(b, l + 1);
    r = r && jobject_1_5(b, l + 1);
    r = r && consumeToken(b, R_CURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  // separator?
  private static boolean jobject_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_1")) return false;
    separator(b, l + 1);
    return true;
  }

  // property?
  private static boolean jobject_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_2")) return false;
    property(b, l + 1);
    return true;
  }

  // (separator  property)*
  private static boolean jobject_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jobject_1_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_1_3", c)) break;
    }
    return true;
  }

  // separator  property
  private static boolean jobject_1_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator(b, l + 1);
    r = r && property(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // COMMA?
  private static boolean jobject_1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_4")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // NEWLINE*
  private static boolean jobject_1_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "jobject_1_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // NEWLINE* value NEWLINE* | NEWLINE*
  static boolean json(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = json_0(b, l + 1);
    if (!r) r = json_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE* value NEWLINE*
  private static boolean json_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = json_0_0(b, l + 1);
    r = r && value(b, l + 1);
    r = r && json_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE*
  private static boolean json_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "json_0_0", c)) break;
    }
    return true;
  }

  // NEWLINE*
  private static boolean json_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "json_0_2", c)) break;
    }
    return true;
  }

  // NEWLINE*
  private static boolean json_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "json_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // property_name NEWLINE* ':' NEWLINE* value
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = property_name(b, l + 1);
    r = r && property_1(b, l + 1);
    r = r && consumeToken(b, COLON);
    p = r; // pin = 3
    r = r && report_error_(b, property_3(b, l + 1));
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, property_auto_recover_);
    return r || p;
  }

  // NEWLINE*
  private static boolean property_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "property_1", c)) break;
    }
    return true;
  }

  // NEWLINE*
  private static boolean property_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "property_3", c)) break;
    }
    return true;
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
  // NEWLINE* COMMA NEWLINE* | NEWLINE+
  static boolean separator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "separator")) return false;
    if (!nextTokenIs(b, "", COMMA, NEWLINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator_0(b, l + 1);
    if (!r) r = separator_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE* COMMA NEWLINE*
  private static boolean separator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "separator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = separator_0_0(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && separator_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // NEWLINE*
  private static boolean separator_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "separator_0_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "separator_0_0", c)) break;
    }
    return true;
  }

  // NEWLINE*
  private static boolean separator_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "separator_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "separator_0_2", c)) break;
    }
    return true;
  }

  // NEWLINE+
  private static boolean separator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "separator_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEWLINE);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "separator_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
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
  // (STRING_PART | L_CURLY | R_CURLY | L_BRACKET | R_BRACKET | COMMA | DOUBLE_QUOTE)+
  static boolean unquoted_key_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unquoted_key_string_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!unquoted_key_string_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unquoted_key_string", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | L_CURLY | R_CURLY | L_BRACKET | R_BRACKET | COMMA | DOUBLE_QUOTE
  private static boolean unquoted_key_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_key_string_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, R_CURLY);
    if (!r) r = consumeToken(b, L_BRACKET);
    if (!r) r = consumeToken(b, R_BRACKET);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, DOUBLE_QUOTE);
    return r;
  }

  /* ********************************************************** */
  // (STRING_PART | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE)+
  static boolean unquoted_value_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unquoted_value_string_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!unquoted_value_string_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unquoted_value_string", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // STRING_PART | COLON | L_CURLY | L_BRACKET | DOUBLE_QUOTE
  private static boolean unquoted_value_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unquoted_value_string_0")) return false;
    boolean r;
    r = consumeToken(b, STRING_PART);
    if (!r) r = consumeToken(b, COLON);
    if (!r) r = consumeToken(b, L_CURLY);
    if (!r) r = consumeToken(b, L_BRACKET);
    if (!r) r = consumeToken(b, DOUBLE_QUOTE);
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

  static final Parser property_auto_recover_ = (b, l) -> !nextTokenIsFast(b, R_CURLY, COMMA, NEWLINE);
}
