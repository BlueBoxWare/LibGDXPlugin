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
    if (t == ARRAY) {
      r = array(b, 0);
    }
    else if (t == BOOLEAN) {
      r = boolean_$(b, 0);
    }
    else if (t == COMMENT) {
      r = comment(b, 0);
    }
    else if (t == JOBJECT) {
      r = jobject(b, 0);
    }
    else if (t == NULL) {
      r = null_$(b, 0);
    }
    else if (t == NUMBER) {
      r = number(b, 0);
    }
    else if (t == PROPERTY) {
      r = property(b, 0);
    }
    else if (t == PROPERTY_NAME) {
      r = property_name(b, 0);
    }
    else if (t == STRING) {
      r = string(b, 0);
    }
    else if (t == VALUE) {
      r = value(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return json(b, l + 1);
  }

  /* ********************************************************** */
  // '[' comment* value? comment* (separator comment* value comment*)* ','? comment* ']'
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
    r = r && array_5(b, l + 1);
    r = r && array_6(b, l + 1);
    r = r && consumeToken(b, R_BRACKET);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // comment*
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
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

  // comment*
  private static boolean array_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_3", c)) break;
    }
    return true;
  }

  // (separator comment* value comment*)*
  private static boolean array_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_4", c)) break;
    }
    return true;
  }

  // separator comment* value comment*
  private static boolean array_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && array_4_0_1(b, l + 1);
    r = r && value(b, l + 1);
    r = r && array_4_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment*
  private static boolean array_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_4_0_1", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean array_4_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_4_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_4_0_3", c)) break;
    }
    return true;
  }

  // ','?
  private static boolean array_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // comment*
  private static boolean array_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_6", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  public static boolean boolean_$(PsiBuilder b, int l) {
    Marker m = enter_section_(b);
    exit_section_(b, m, BOOLEAN, true);
    return true;
  }

  /* ********************************************************** */
  // <<parseComment>>
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMENT, "<comment>");
    r = parseComment(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '{' comment* property? comment* (separator comment* property)* ','? comment* '}'
  public static boolean jobject(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject")) return false;
    if (!nextTokenIs(b, "<object>", L_CURLY)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JOBJECT, "<object>");
    r = consumeToken(b, L_CURLY);
    r = r && jobject_1(b, l + 1);
    r = r && jobject_2(b, l + 1);
    r = r && jobject_3(b, l + 1);
    r = r && jobject_4(b, l + 1);
    r = r && jobject_5(b, l + 1);
    r = r && jobject_6(b, l + 1);
    r = r && consumeToken(b, R_CURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // comment*
  private static boolean jobject_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_1", c)) break;
    }
    return true;
  }

  // property?
  private static boolean jobject_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_2")) return false;
    property(b, l + 1);
    return true;
  }

  // comment*
  private static boolean jobject_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_3", c)) break;
    }
    return true;
  }

  // (separator comment* property)*
  private static boolean jobject_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jobject_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_4", c)) break;
    }
    return true;
  }

  // separator comment* property
  private static boolean jobject_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && jobject_4_0_1(b, l + 1);
    r = r && property(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment*
  private static boolean jobject_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_4_0_1", c)) break;
    }
    return true;
  }

  // ','?
  private static boolean jobject_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // comment*
  private static boolean jobject_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jobject_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jobject_6", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // comment* value comment*
  static boolean json(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = json_0(b, l + 1);
    r = r && value(b, l + 1);
    r = r && json_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment*
  private static boolean json_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "json_0", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean json_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "json_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "json_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !( '}' | separator)
  static boolean not_brace_or_separator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_brace_or_separator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !not_brace_or_separator_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '}' | separator
  private static boolean not_brace_or_separator_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_brace_or_separator_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, R_CURLY);
    if (!r) r = parseSeparator(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  public static boolean null_$(PsiBuilder b, int l) {
    Marker m = enter_section_(b);
    exit_section_(b, m, NULL, true);
    return true;
  }

  /* ********************************************************** */
  public static boolean number(PsiBuilder b, int l) {
    Marker m = enter_section_(b);
    exit_section_(b, m, NUMBER, true);
    return true;
  }

  /* ********************************************************** */
  // property_name comment* ':' comment* value comment*
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = property_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, property_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COLON)) && r;
    r = p && report_error_(b, property_3(b, l + 1)) && r;
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && property_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, not_brace_or_separator_parser_);
    return r || p;
  }

  // comment*
  private static boolean property_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "property_1", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean property_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "property_3", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean property_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!comment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "property_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // quoted_string | unquoted_name_string
  public static boolean property_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_name")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_NAME, "<property name>");
    r = quoted_string(b, l + 1);
    if (!r) r = parseUnquotedNameString(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '"' quoted_chars '"'
  static boolean quoted_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "quoted_string")) return false;
    if (!nextTokenIs(b, DOUBLE_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOUBLE_QUOTE);
    r = r && parseQuotedChars(b, l + 1);
    r = r && consumeToken(b, DOUBLE_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // quoted_string | unquoted_value_string
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING, "<string>");
    r = quoted_string(b, l + 1);
    if (!r) r = parseUnquotedValueString(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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

  static final Parser not_brace_or_separator_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return not_brace_or_separator(b, l + 1);
    }
  };
}
