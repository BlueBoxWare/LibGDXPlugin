package com.gmail.blueboxware.libgdxplugin.filetypes.json;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;

%%

%{
  public _GdxJsonLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _GdxJsonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

ANY_CHAR=.

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  "{"                { return L_CURLY; }
  "}"                { return R_CURLY; }
  "["                { return L_BRACKET; }
  "]"                { return R_BRACKET; }
  ","                { return COMMA; }
  ":"                { return COLON; }
  "\""               { return DOUBLE_QUOTE; }
  "/"                { return SLASH; }
  "\\"               { return BACK_SLASH; }
  "*"                { return ASTERIX; }

  {ANY_CHAR}         { return ANY_CHAR; }

}

[^] { return BAD_CHARACTER; }
