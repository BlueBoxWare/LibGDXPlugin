// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class BitmapFontParser implements PsiParser, LightPsiParser {

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
    return bitmapFont(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // info common page_definition* chars? font_char* kernings? kerning*
  static boolean bitmapFont(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = info(builder_, level_ + 1);
    result_ = result_ && common(builder_, level_ + 1);
    result_ = result_ && bitmapFont_2(builder_, level_ + 1);
    result_ = result_ && bitmapFont_3(builder_, level_ + 1);
    result_ = result_ && bitmapFont_4(builder_, level_ + 1);
    result_ = result_ && bitmapFont_5(builder_, level_ + 1);
    result_ = result_ && bitmapFont_6(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // page_definition*
  private static boolean bitmapFont_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!page_definition(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "bitmapFont_2", pos_)) break;
    }
    return true;
  }

  // chars?
  private static boolean bitmapFont_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont_3")) return false;
    chars(builder_, level_ + 1);
    return true;
  }

  // font_char*
  private static boolean bitmapFont_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont_4")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!font_char(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "bitmapFont_4", pos_)) break;
    }
    return true;
  }

  // kernings?
  private static boolean bitmapFont_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont_5")) return false;
    kernings(builder_, level_ + 1);
    return true;
  }

  // kerning*
  private static boolean bitmapFont_6(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitmapFont_6")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!kerning(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "bitmapFont_6", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'chars' property* endofline
  public static boolean chars(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "chars")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CHARS, "<chars>");
    result_ = consumeToken(builder_, "chars");
    result_ = result_ && chars_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean chars_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "chars_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "chars_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'common' property* endofline
  public static boolean common(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "common")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMON, "<common>");
    result_ = consumeToken(builder_, "common");
    result_ = result_ && common_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean common_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "common_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "common_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // EOL+ | EOL* <<eof>>
  static boolean endofline(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "endofline")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = endofline_0(builder_, level_ + 1);
    if (!result_) result_ = endofline_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // EOL+
  private static boolean endofline_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "endofline_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, EOL);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, EOL)) break;
      if (!empty_element_parsed_guard_(builder_, "endofline_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // EOL* <<eof>>
  private static boolean endofline_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "endofline_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = endofline_1_0(builder_, level_ + 1);
    result_ = result_ && eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // EOL*
  private static boolean endofline_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "endofline_1_0")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, EOL)) break;
      if (!empty_element_parsed_guard_(builder_, "endofline_1_0", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'char' property* endofline
  public static boolean font_char(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "font_char")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FONT_CHAR, "<font char>");
    result_ = consumeToken(builder_, "char");
    result_ = result_ && font_char_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean font_char_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "font_char_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "font_char_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'info' property* endofline
  public static boolean info(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "info")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INFO, "<info>");
    result_ = consumeToken(builder_, "info");
    result_ = result_ && info_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean info_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "info_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "info_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'kerning' property* endofline
  public static boolean kerning(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "kerning")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, KERNING, "<kerning>");
    result_ = consumeToken(builder_, "kerning");
    result_ = result_ && kerning_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean kerning_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "kerning_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "kerning_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'kernings' property* endofline
  public static boolean kernings(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "kernings")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, KERNINGS, "<kernings>");
    result_ = consumeToken(builder_, "kernings");
    result_ = result_ && kernings_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean kernings_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "kernings_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "kernings_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING
  public static boolean key(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "key")) return false;
    if (!nextTokenIs(builder_, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, UNQUOTED_STRING);
    exit_section_(builder_, marker_, KEY, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'page' property* endofline
  public static boolean page_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PAGE_DEFINITION, "<page definition>");
    result_ = consumeToken(builder_, "page");
    result_ = result_ && page_definition_1(builder_, level_ + 1);
    result_ = result_ && endofline(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // property*
  private static boolean page_definition_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "page_definition_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!property(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "page_definition_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // key '=' (value !'=' )?
  public static boolean property(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property")) return false;
    if (!nextTokenIs(builder_, UNQUOTED_STRING)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = key(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUALS);
    result_ = result_ && property_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, PROPERTY, result_);
    return result_;
  }

  // (value !'=' )?
  private static boolean property_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_2")) return false;
    property_2_0(builder_, level_ + 1);
    return true;
  }

  // value !'='
  private static boolean property_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = value(builder_, level_ + 1);
    result_ = result_ && property_2_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !'='
  private static boolean property_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "property_2_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, EQUALS);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | DOUBLE_QUOTED_STRING | SINGLE_QUOTED_STRING
  public static boolean value(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "value")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUE, "<value>");
    result_ = consumeToken(builder_, UNQUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_QUOTED_STRING);
    if (!result_) result_ = consumeToken(builder_, SINGLE_QUOTED_STRING);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

}
