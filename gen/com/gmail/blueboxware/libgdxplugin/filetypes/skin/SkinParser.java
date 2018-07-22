// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.skin;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SkinParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ARRAY) {
      r = array(b, 0);
    }
    else if (t == CLASS_NAME) {
      r = class_name(b, 0);
    }
    else if (t == CLASS_SPECIFICATION) {
      r = class_specification(b, 0);
    }
    else if (t == OBJECT) {
      r = object(b, 0);
    }
    else if (t == PROPERTY) {
      r = property(b, 0);
    }
    else if (t == PROPERTY_NAME) {
      r = property_name(b, 0);
    }
    else if (t == PROPERTY_VALUE) {
      r = property_value(b, 0);
    }
    else if (t == RESOURCE) {
      r = resource(b, 0);
    }
    else if (t == RESOURCE_NAME) {
      r = resource_name(b, 0);
    }
    else if (t == RESOURCES) {
      r = resources(b, 0);
    }
    else if (t == STRING_LITERAL) {
      r = string_literal(b, 0);
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
    return skin(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ARRAY, OBJECT, STRING_LITERAL, VALUE),
  };

  /* ********************************************************** */
  // '[' value? (separator value)* optionalComma ']'
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, L_BRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY, null);
    r = consumeToken(b, L_BRACKET);
    r = r && array_1(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, array_2(b, l + 1));
    r = p && report_error_(b, parseOtionalComma(b, l + 1)) && r;
    r = p && consumeToken(b, R_BRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // value?
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    value(b, l + 1);
    return true;
  }

  // (separator value)*
  private static boolean array_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_2", c)) break;
    }
    return true;
  }

  // separator value
  private static boolean array_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean class_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_name")) return false;
    if (!nextTokenIs(b, "<class name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CLASS_NAME, "<class name>");
    r = string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ':'
  static boolean class_spec_colon(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec_colon")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COLON);
    exit_section_(b, l, m, r, false, recover_class_colon_parser_);
    return r;
  }

  /* ********************************************************** */
  // '{'
  static boolean class_spec_lbrace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_spec_lbrace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, L_CURLY);
    exit_section_(b, l, m, r, false, recover_class_lbrace_parser_);
    return r;
  }

  /* ********************************************************** */
  // class_name class_spec_colon class_spec_lbrace resources '}'
  public static boolean class_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_specification")) return false;
    if (!nextTokenIs(b, "<class specification>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CLASS_SPECIFICATION, "<class specification>");
    r = class_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, class_spec_colon(b, l + 1));
    r = p && report_error_(b, class_spec_lbrace(b, l + 1)) && r;
    r = p && report_error_(b, resources(b, l + 1)) && r;
    r = p && consumeToken(b, R_CURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '{' resource_list '}'
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    if (!nextTokenIs(b, L_CURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OBJECT, null);
    r = consumeToken(b, L_CURLY);
    r = r && resource_list(b, l + 1);
    p = r; // pin = 2
    r = r && consumeToken(b, R_CURLY);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // property_name ':' property_value
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = property_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && property_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_property_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean property_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_name")) return false;
    if (!nextTokenIs(b, "<property name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_NAME, "<property name>");
    r = string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // value
  public static boolean property_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_VALUE, "<property value>");
    r = value(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !'}'
  static boolean recover_class(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, R_CURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !'{'
  static boolean recover_class_colon(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class_colon")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, L_CURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(string_literal | '}')
  static boolean recover_class_lbrace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class_lbrace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_class_lbrace_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // string_literal | '}'
  private static boolean recover_class_lbrace_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_class_lbrace_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_literal(b, l + 1);
    if (!r) r = consumeToken(b, R_CURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !'}'
  static boolean recover_object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_object")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, R_CURLY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(separator | '}')
  static boolean recover_property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_property")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_property_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // separator | '}'
  private static boolean recover_property_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_property_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    if (!r) r = consumeToken(b, R_CURLY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // resource_name ':' (object | string_literal)
  public static boolean resource(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource")) return false;
    if (!nextTokenIs(b, "<resource>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RESOURCE, "<resource>");
    r = resource_name(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && resource_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // object | string_literal
  private static boolean resource_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_2")) return false;
    boolean r;
    r = object(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // property? (separator property)* optionalComma
  static boolean resource_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = resource_list_0(b, l + 1);
    r = r && resource_list_1(b, l + 1);
    r = r && parseOtionalComma(b, l + 1);
    exit_section_(b, l, m, r, false, recover_object_parser_);
    return r;
  }

  // property?
  private static boolean resource_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_list_0")) return false;
    property(b, l + 1);
    return true;
  }

  // (separator property)*
  private static boolean resource_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!resource_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "resource_list_1", c)) break;
    }
    return true;
  }

  // separator property
  private static boolean resource_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && property(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean resource_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resource_name")) return false;
    if (!nextTokenIs(b, "<resource name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RESOURCE_NAME, "<resource name>");
    r = string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // resource? (separator resource)* optionalComma
  public static boolean resources(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resources")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RESOURCES, "<resources>");
    r = resources_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, resources_1(b, l + 1));
    r = p && parseOtionalComma(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_class_parser_);
    return r || p;
  }

  // resource?
  private static boolean resources_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resources_0")) return false;
    resource(b, l + 1);
    return true;
  }

  // (separator resource)*
  private static boolean resources_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resources_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!resources_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "resources_1", c)) break;
    }
    return true;
  }

  // separator resource
  private static boolean resources_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resources_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && resource(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{'  class_specification? (separator class_specification)* optionalComma '}'
  static boolean skin(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "skin")) return false;
    if (!nextTokenIs(b, L_CURLY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, L_CURLY);
    p = r; // pin = 1
    r = r && report_error_(b, skin_1(b, l + 1));
    r = p && report_error_(b, skin_2(b, l + 1)) && r;
    r = p && report_error_(b, parseOtionalComma(b, l + 1)) && r;
    r = p && consumeToken(b, R_CURLY) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // class_specification?
  private static boolean skin_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "skin_1")) return false;
    class_specification(b, l + 1);
    return true;
  }

  // (separator class_specification)*
  private static boolean skin_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "skin_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!skin_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "skin_2", c)) break;
    }
    return true;
  }

  // separator class_specification
  private static boolean skin_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "skin_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseSeparator(b, l + 1);
    r = r && class_specification(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | UNQUOTED_STRING
  public static boolean string_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_literal")) return false;
    if (!nextTokenIs(b, "<string literal>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_LITERAL, "<string literal>");
    r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = consumeToken(b, UNQUOTED_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // object | array | string_literal
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, VALUE, "<value>");
    r = object(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = string_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  final static Parser recover_class_colon_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_class_colon(b, l + 1);
    }
  };
  final static Parser recover_class_lbrace_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_class_lbrace(b, l + 1);
    }
  };
  final static Parser recover_class_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_class(b, l + 1);
    }
  };
  final static Parser recover_object_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_object(b, l + 1);
    }
  };
  final static Parser recover_property_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_property(b, l + 1);
    }
  };
}
