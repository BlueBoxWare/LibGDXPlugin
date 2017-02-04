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

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == CHARS) {
      r = chars(b, 0);
    }
    else if (t == COMMON) {
      r = common(b, 0);
    }
    else if (t == FONT_CHAR) {
      r = font_char(b, 0);
    }
    else if (t == INFO) {
      r = info(b, 0);
    }
    else if (t == KERNING) {
      r = kerning(b, 0);
    }
    else if (t == KERNINGS) {
      r = kernings(b, 0);
    }
    else if (t == KEY) {
      r = key(b, 0);
    }
    else if (t == PAGE_DEFINITION) {
      r = page_definition(b, 0);
    }
    else if (t == PROPERTY) {
      r = property(b, 0);
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
    return bitmapFont(b, l + 1);
  }

  /* ********************************************************** */
  // info common page_definition* chars? font_char* kernings? kerning*
  static boolean bitmapFont(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = info(b, l + 1);
    r = r && common(b, l + 1);
    r = r && bitmapFont_2(b, l + 1);
    r = r && bitmapFont_3(b, l + 1);
    r = r && bitmapFont_4(b, l + 1);
    r = r && bitmapFont_5(b, l + 1);
    r = r && bitmapFont_6(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // page_definition*
  private static boolean bitmapFont_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!page_definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitmapFont_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // chars?
  private static boolean bitmapFont_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont_3")) return false;
    chars(b, l + 1);
    return true;
  }

  // font_char*
  private static boolean bitmapFont_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont_4")) return false;
    int c = current_position_(b);
    while (true) {
      if (!font_char(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitmapFont_4", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // kernings?
  private static boolean bitmapFont_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont_5")) return false;
    kernings(b, l + 1);
    return true;
  }

  // kerning*
  private static boolean bitmapFont_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bitmapFont_6")) return false;
    int c = current_position_(b);
    while (true) {
      if (!kerning(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bitmapFont_6", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'chars' property* endofline
  public static boolean chars(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chars")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CHARS, "<chars>");
    r = consumeToken(b, "chars");
    r = r && chars_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean chars_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chars_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "chars_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'common' property* endofline
  public static boolean common(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "common")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COMMON, "<common>");
    r = consumeToken(b, "common");
    r = r && common_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean common_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "common_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "common_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // EOL+ | EOL* <<eof>>
  static boolean endofline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endofline")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = endofline_0(b, l + 1);
    if (!r) r = endofline_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EOL+
  private static boolean endofline_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endofline_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EOL);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, EOL)) break;
      if (!empty_element_parsed_guard_(b, "endofline_0", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // EOL* <<eof>>
  private static boolean endofline_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endofline_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = endofline_1_0(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EOL*
  private static boolean endofline_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "endofline_1_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!consumeToken(b, EOL)) break;
      if (!empty_element_parsed_guard_(b, "endofline_1_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'char' property* endofline
  public static boolean font_char(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "font_char")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FONT_CHAR, "<font char>");
    r = consumeToken(b, "char");
    r = r && font_char_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean font_char_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "font_char_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "font_char_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'info' property* endofline
  public static boolean info(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "info")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INFO, "<info>");
    r = consumeToken(b, "info");
    r = r && info_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean info_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "info_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "info_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'kerning' property* endofline
  public static boolean kerning(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "kerning")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KERNING, "<kerning>");
    r = consumeToken(b, "kerning");
    r = r && kerning_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean kerning_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "kerning_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "kerning_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'kernings' property* endofline
  public static boolean kernings(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "kernings")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, KERNINGS, "<kernings>");
    r = consumeToken(b, "kernings");
    r = r && kernings_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean kernings_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "kernings_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "kernings_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING
  public static boolean key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key")) return false;
    if (!nextTokenIs(b, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNQUOTED_STRING);
    exit_section_(b, m, KEY, r);
    return r;
  }

  /* ********************************************************** */
  // 'page' property* endofline
  public static boolean page_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PAGE_DEFINITION, "<page definition>");
    r = consumeToken(b, "page");
    r = r && page_definition_1(b, l + 1);
    r = r && endofline(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // property*
  private static boolean page_definition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_definition_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "page_definition_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // key '=' (value !'=' )?
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    if (!nextTokenIs(b, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = key(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    r = r && property_2(b, l + 1);
    exit_section_(b, m, PROPERTY, r);
    return r;
  }

  // (value !'=' )?
  private static boolean property_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_2")) return false;
    property_2_0(b, l + 1);
    return true;
  }

  // value !'='
  private static boolean property_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value(b, l + 1);
    r = r && property_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !'='
  private static boolean property_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, EQUALS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | DOUBLE_QUOTED_STRING | SINGLE_QUOTED_STRING
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
    r = consumeToken(b, UNQUOTED_STRING);
    if (!r) r = consumeToken(b, DOUBLE_QUOTED_STRING);
    if (!r) r = consumeToken(b, SINGLE_QUOTED_STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
