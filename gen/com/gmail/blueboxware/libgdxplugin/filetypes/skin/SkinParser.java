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

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, EXTENDS_SETS_);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return skin(builder_, level_ + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ARRAY, OBJECT, STRING_LITERAL, VALUE),
  };

  /* ********************************************************** */
  // '[' value? (separator value)* optionalComma ']'
  public static boolean array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array")) return false;
    if (!nextTokenIs(builder_, L_BRACKET)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY, null);
    result_ = consumeToken(builder_, L_BRACKET);
    result_ = result_ && array_1(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && report_error_(builder_, array_2(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, parseOtionalComma(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, R_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // value?
  private static boolean array_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_1")) return false;
    value(builder_, level_ + 1);
    return true;
  }

  // (separator value)*
  private static boolean array_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_2", pos_)) break;
    }
    return true;
  }

  // separator value
  private static boolean array_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSeparator(builder_, level_ + 1);
    result_ = result_ && value(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean class_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_name")) return false;
    if (!nextTokenIs(builder_, "<class name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CLASS_NAME, "<class name>");
    result_ = string_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // ':'
  static boolean class_spec_colon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_spec_colon")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, COLON);
    exit_section_(builder_, level_, marker_, result_, false, SkinParser::recover_class_colon);
    return result_;
  }

  /* ********************************************************** */
  // '{'
  static boolean class_spec_lbrace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_spec_lbrace")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, L_CURLY);
    exit_section_(builder_, level_, marker_, result_, false, SkinParser::recover_class_lbrace);
    return result_;
  }

  /* ********************************************************** */
  // class_name class_spec_colon class_spec_lbrace resources '}'
  public static boolean class_specification(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_specification")) return false;
    if (!nextTokenIs(builder_, "<class specification>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CLASS_SPECIFICATION, "<class specification>");
    result_ = class_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, class_spec_colon(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, class_spec_lbrace(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, resources(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, R_CURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '{' resource_list '}'
  public static boolean object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "object")) return false;
    if (!nextTokenIs(builder_, L_CURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OBJECT, null);
    result_ = consumeToken(builder_, L_CURLY);
    result_ = result_ && resource_list(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && consumeToken(builder_, R_CURLY);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // property_name ':' property_value
  public static boolean property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY, "<property>");
    result_ = property_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, COLON));
    result_ = pinned_ && property_value(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, SkinParser::recover_property);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean property_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_name")) return false;
    if (!nextTokenIs(builder_, "<property name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_NAME, "<property name>");
    result_ = string_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // value
  public static boolean property_value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROPERTY_VALUE, "<property value>");
    result_ = value(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !'}'
  static boolean recover_class(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_class")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, R_CURLY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !'{'
  static boolean recover_class_colon(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_class_colon")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, L_CURLY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(string_literal | '}')
  static boolean recover_class_lbrace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_class_lbrace")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_class_lbrace_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // string_literal | '}'
  private static boolean recover_class_lbrace_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_class_lbrace_0")) return false;
    boolean result_;
    result_ = string_literal(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, R_CURLY);
    return result_;
  }

  /* ********************************************************** */
  // !'}'
  static boolean recover_object(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_object")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, R_CURLY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(separator | '}')
  static boolean recover_property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_property")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_property_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // separator | '}'
  private static boolean recover_property_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_property_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSeparator(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, R_CURLY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // resource_name ':' (object | string_literal)
  public static boolean resource(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource")) return false;
    if (!nextTokenIs(builder_, "<resource>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RESOURCE, "<resource>");
    result_ = resource_name(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, consumeToken(builder_, COLON));
    result_ = pinned_ && resource_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // object | string_literal
  private static boolean resource_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_2")) return false;
    boolean result_;
    result_ = object(builder_, level_ + 1);
    if (!result_) result_ = string_literal(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // property? (separator property)* optionalComma
  static boolean resource_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_list")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = resource_list_0(builder_, level_ + 1);
    result_ = result_ && resource_list_1(builder_, level_ + 1);
    result_ = result_ && parseOtionalComma(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, SkinParser::recover_object);
    return result_;
  }

  // property?
  private static boolean resource_list_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_list_0")) return false;
    property(builder_, level_ + 1);
    return true;
  }

  // (separator property)*
  private static boolean resource_list_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_list_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!resource_list_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "resource_list_1", pos_)) break;
    }
    return true;
  }

  // separator property
  private static boolean resource_list_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_list_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSeparator(builder_, level_ + 1);
    result_ = result_ && property(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // string_literal
  public static boolean resource_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resource_name")) return false;
    if (!nextTokenIs(builder_, "<resource name>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RESOURCE_NAME, "<resource name>");
    result_ = string_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // resource? (separator resource)* optionalComma
  public static boolean resources(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resources")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RESOURCES, "<resources>");
    result_ = resources_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, resources_1(builder_, level_ + 1));
    result_ = pinned_ && parseOtionalComma(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, SkinParser::recover_class);
    return result_ || pinned_;
  }

  // resource?
  private static boolean resources_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resources_0")) return false;
    resource(builder_, level_ + 1);
    return true;
  }

  // (separator resource)*
  private static boolean resources_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resources_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!resources_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "resources_1", pos_)) break;
    }
    return true;
  }

  // separator resource
  private static boolean resources_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "resources_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSeparator(builder_, level_ + 1);
    result_ = result_ && resource(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // '{'  class_specification? (separator class_specification)* optionalComma '}'
  static boolean skin(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "skin")) return false;
    if (!nextTokenIs(builder_, L_CURLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, L_CURLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, skin_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, skin_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, parseOtionalComma(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, R_CURLY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // class_specification?
  private static boolean skin_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "skin_1")) return false;
    class_specification(builder_, level_ + 1);
    return true;
  }

  // (separator class_specification)*
  private static boolean skin_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "skin_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!skin_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "skin_2", pos_)) break;
    }
    return true;
  }

  // separator class_specification
  private static boolean skin_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "skin_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSeparator(builder_, level_ + 1);
    result_ = result_ && class_specification(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // DOUBLE_QUOTED_STRING | UNQUOTED_STRING
  public static boolean string_literal(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_literal")) return false;
    if (!nextTokenIs(builder_, "<string literal>", DOUBLE_QUOTED_STRING, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_LITERAL, "<string literal>");
    result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, UNQUOTED_STRING);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // object | array | string_literal
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, VALUE, "<value>");
    result_ = object(builder_, level_ + 1);
    if (!result_) result_ = array(builder_, level_ + 1);
    if (!result_) result_ = string_literal(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
