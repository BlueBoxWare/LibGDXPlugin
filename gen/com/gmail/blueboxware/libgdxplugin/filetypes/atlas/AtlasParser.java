// This is a generated file. Not intended for manual editing.
package com.gmail.blueboxware.libgdxplugin.filetypes.atlas;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class AtlasParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == FILTER) {
      r = filter(b, 0);
    }
    else if (t == FILTER_VALUE) {
      r = filter_value(b, 0);
    }
    else if (t == FORMAT) {
      r = format(b, 0);
    }
    else if (t == FORMAT_VALUE) {
      r = format_value(b, 0);
    }
    else if (t == INDEX) {
      r = index(b, 0);
    }
    else if (t == OFFSET) {
      r = offset(b, 0);
    }
    else if (t == ORIG) {
      r = orig(b, 0);
    }
    else if (t == PAD) {
      r = pad(b, 0);
    }
    else if (t == PAGE) {
      r = page(b, 0);
    }
    else if (t == REGION) {
      r = region(b, 0);
    }
    else if (t == REGION_NAME) {
      r = regionName(b, 0);
    }
    else if (t == REPEAT) {
      r = repeat(b, 0);
    }
    else if (t == REPEAT_VALUE) {
      r = repeat_value(b, 0);
    }
    else if (t == ROTATE) {
      r = rotate(b, 0);
    }
    else if (t == ROTATE_VALUE) {
      r = rotate_value(b, 0);
    }
    else if (t == SIZE) {
      r = size(b, 0);
    }
    else if (t == SPLIT) {
      r = split(b, 0);
    }
    else if (t == VALUE) {
      r = value(b, 0);
    }
    else if (t == XY) {
      r = xy(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return atlas(b, l + 1);
  }

  /* ********************************************************** */
  // EOL? page (EOL page)* EOL*
  static boolean atlas(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas")) return false;
    if (!nextTokenIs(b, "", EOL, STRING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = atlas_0(b, l + 1);
    r = r && page(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, atlas_2(b, l + 1));
    r = p && atlas_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // EOL?
  private static boolean atlas_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas_0")) return false;
    consumeToken(b, EOL);
    return true;
  }

  // (EOL page)*
  private static boolean atlas_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!atlas_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "atlas_2", c)) break;
    }
    return true;
  }

  // EOL page
  private static boolean atlas_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EOL);
    r = r && page(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EOL*
  private static boolean atlas_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atlas_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, EOL)) break;
      if (!empty_element_parsed_guard_(b, "atlas_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'filter' ':' filter_value ',' filter_value
  public static boolean filter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "filter")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FILTER, "<filter>");
    r = consumeToken(b, "filter");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, filter_value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && filter_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'Nearest' | 'Linear' | 'MipMap' | 'MipMapNearestNearest' | 'MipMapLinearNearest' | 'MipMapNearestLinear' | 'MipMapLinearLinear'
  public static boolean filter_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "filter_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FILTER_VALUE, "<Nearest, Linear, MipMap, MipMapNearestNearest, MipMapLinearNearest, MipMapNearestLinear or MipMapLinearLinear>");
    r = consumeToken(b, "Nearest");
    if (!r) r = consumeToken(b, "Linear");
    if (!r) r = consumeToken(b, "MipMap");
    if (!r) r = consumeToken(b, "MipMapNearestNearest");
    if (!r) r = consumeToken(b, "MipMapLinearNearest");
    if (!r) r = consumeToken(b, "MipMapNearestLinear");
    if (!r) r = consumeToken(b, "MipMapLinearLinear");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'format' ':' format_value
  public static boolean format(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FORMAT, "<format>");
    r = consumeToken(b, "format");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && format_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'Alpha' | 'Intensity' | 'LuminanceAlpha' | 'RGB565' | 'RGBA4444' | 'RGB888' | 'RGBA8888'
  public static boolean format_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "format_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAT_VALUE, "<Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888 or RGBA8888>");
    r = consumeToken(b, "Alpha");
    if (!r) r = consumeToken(b, "Intensity");
    if (!r) r = consumeToken(b, "LuminanceAlpha");
    if (!r) r = consumeToken(b, "RGB565");
    if (!r) r = consumeToken(b, "RGBA4444");
    if (!r) r = consumeToken(b, "RGB888");
    if (!r) r = consumeToken(b, "RGBA8888");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'index' ':' value
  public static boolean index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "index")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INDEX, "<index>");
    r = consumeToken(b, "index");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'offset' ':' value ',' value
  public static boolean offset(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "offset")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OFFSET, "<offset>");
    r = consumeToken(b, "offset");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'orig' ':' value ',' value
  public static boolean orig(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "orig")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ORIG, "<orig>");
    r = consumeToken(b, "orig");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'pad' ':' value ',' value ',' value ',' value
  public static boolean pad(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pad")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAD, "<pad>");
    r = consumeToken(b, "pad");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // value EOL (size EOL)? format EOL filter EOL repeat EOL region*
  public static boolean page(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && page_2(b, l + 1);
    r = r && format(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && filter(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && repeat(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && page_9(b, l + 1);
    exit_section_(b, m, PAGE, r);
    return r;
  }

  // (size EOL)?
  private static boolean page_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_2")) return false;
    page_2_0(b, l + 1);
    return true;
  }

  // size EOL
  private static boolean page_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = size(b, l + 1);
    r = r && consumeToken(b, EOL);
    exit_section_(b, m, null, r);
    return r;
  }

  // region*
  private static boolean page_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "page_9")) return false;
    while (true) {
      int c = current_position_(b);
      if (!region(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "page_9", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !EOL
  static boolean recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, EOL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // regionName EOL rotate EOL xy EOL size EOL (split EOL (pad EOL)?)? orig EOL offset EOL index EOL?
  public static boolean region(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = regionName(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && rotate(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && xy(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && size(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && region_8(b, l + 1);
    r = r && orig(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && offset(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && index(b, l + 1);
    r = r && region_14(b, l + 1);
    exit_section_(b, m, REGION, r);
    return r;
  }

  // (split EOL (pad EOL)?)?
  private static boolean region_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_8")) return false;
    region_8_0(b, l + 1);
    return true;
  }

  // split EOL (pad EOL)?
  private static boolean region_8_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_8_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = split(b, l + 1);
    r = r && consumeToken(b, EOL);
    r = r && region_8_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (pad EOL)?
  private static boolean region_8_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_8_0_2")) return false;
    region_8_0_2_0(b, l + 1);
    return true;
  }

  // pad EOL
  private static boolean region_8_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_8_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pad(b, l + 1);
    r = r && consumeToken(b, EOL);
    exit_section_(b, m, null, r);
    return r;
  }

  // EOL?
  private static boolean region_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "region_14")) return false;
    consumeToken(b, EOL);
    return true;
  }

  /* ********************************************************** */
  // value+
  public static boolean regionName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regionName")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = value(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!value(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "regionName", c)) break;
    }
    exit_section_(b, m, REGION_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // 'repeat' ':' repeat_value
  public static boolean repeat(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REPEAT, "<repeat>");
    r = consumeToken(b, "repeat");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && repeat_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'x' | 'y' | 'xy' | 'none'
  public static boolean repeat_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REPEAT_VALUE, "<x, y, xy or none>");
    r = consumeToken(b, "x");
    if (!r) r = consumeToken(b, "y");
    if (!r) r = consumeToken(b, "xy");
    if (!r) r = consumeToken(b, "none");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'rotate' ':' rotate_value
  public static boolean rotate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rotate")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ROTATE, "<rotate>");
    r = consumeToken(b, "rotate");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && rotate_value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'true' | 'false'
  public static boolean rotate_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rotate_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ROTATE_VALUE, "<true or false>");
    r = consumeToken(b, "true");
    if (!r) r = consumeToken(b, "false");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'size' ':' value ',' value
  public static boolean size(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "size")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SIZE, "<size>");
    r = consumeToken(b, "size");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // 'split' ':' value ',' value ',' value ',' value
  public static boolean split(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "split")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SPLIT, "<split>");
    r = consumeToken(b, "split");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  /* ********************************************************** */
  // STRING
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // 'xy' ':' value ',' value
  public static boolean xy(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xy")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, XY, "<xy>");
    r = consumeToken(b, "xy");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COLON));
    r = p && report_error_(b, value(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, COMMA)) && r;
    r = p && value(b, l + 1) && r;
    exit_section_(b, l, m, r, p, recover_parser_);
    return r || p;
  }

  final static Parser recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover(b, l + 1);
    }
  };
}
