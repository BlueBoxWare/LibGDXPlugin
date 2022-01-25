// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ElementTypes.*;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ParserUtil.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class Atlas2Parser implements PsiParser, LightPsiParser {

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
    return atlas2(b, l + 1);
  }

  /* ********************************************************** */
  // EMPTY_LINE* field* page*
  static boolean atlas2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = atlas2_0(b, l + 1);
    r = r && atlas2_1(b, l + 1);
    r = r && atlas2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EMPTY_LINE*
  private static boolean atlas2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas2_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, EMPTY_LINE)) break;
      if (!empty_element_parsed_guard_(b, "atlas2_0", c)) break;
    }
    return true;
  }

  // field*
  private static boolean atlas2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas2_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "atlas2_1", c)) break;
    }
    return true;
  }

  // page*
  private static boolean atlas2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas2_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!page(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "atlas2_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // key COLON (value | COMMA)*
  public static boolean field(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field")) return false;
    if (!nextTokenIs(b, TKEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = key(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && field_2(b, l + 1);
    exit_section_(b, m, FIELD, r);
    return r;
  }

  // (value | COMMA)*
  private static boolean field_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "field_2", c)) break;
    }
    return true;
  }

  // value | COMMA
  private static boolean field_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_2_0")) return false;
    boolean r;
    r = value(b, l + 1);
    if (!r) r = consumeToken(b, COMMA);
    return r;
  }

  /* ********************************************************** */
  // THEADER
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, THEADER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, THEADER);
    exit_section_(b, m, HEADER, r);
    return r;
  }

  /* ********************************************************** */
  // TKEY
  public static boolean key(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "key")) return false;
    if (!nextTokenIs(b, TKEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TKEY);
    exit_section_(b, m, KEY, r);
    return r;
  }

  /* ********************************************************** */
  // header field* region* (EMPTY_LINE | <<eof>>)
  public static boolean page(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page")) return false;
    if (!nextTokenIs(b, THEADER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header(b, l + 1);
    r = r && page_1(b, l + 1);
    r = r && page_2(b, l + 1);
    r = r && page_3(b, l + 1);
    exit_section_(b, m, PAGE, r);
    return r;
  }

  // field*
  private static boolean page_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "page_1", c)) break;
    }
    return true;
  }

  // region*
  private static boolean page_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!region(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "page_2", c)) break;
    }
    return true;
  }

  // EMPTY_LINE | <<eof>>
  private static boolean page_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EMPTY_LINE);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // header field*
  public static boolean region(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region")) return false;
    if (!nextTokenIs(b, THEADER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header(b, l + 1);
    r = r && region_1(b, l + 1);
    exit_section_(b, m, REGION, r);
    return r;
  }

  // field*
  private static boolean region_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!field(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "region_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // TVALUE
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    if (!nextTokenIs(b, TVALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TVALUE);
    exit_section_(b, m, VALUE, r);
    return r;
  }

}
