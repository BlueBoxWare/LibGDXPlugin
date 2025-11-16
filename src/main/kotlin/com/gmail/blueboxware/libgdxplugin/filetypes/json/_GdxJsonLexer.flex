package com.gmail.blueboxware.libgdxplugin.filetypes.json;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*;

%%

%{
  public _GdxJsonLexer() {
      this(null);
    }
%}

%public
%class _GdxJsonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE=[ \t\n]+

LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*([^*]|\*+[^*/])*(\*+"/")?
DOUBLE_QUOTED_STRING=\"([^\\\"]|\\.)*\"

%state PART

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "{"                         { return L_CURLY; }
  "}"                         { return R_CURLY; }
  "["                         { return L_BRACKET; }
  "]"                         { return R_BRACKET; }
  ","                         { return COMMA; }
  ":"                         { return COLON; }
  "/"                         { return SLASH; }
  "*"                         { return ASTERIX; }

  {DOUBLE_QUOTED_STRING}      { return DOUBLE_QUOTED_STRING; }

  {LINE_COMMENT}              { return LINE_COMMENT; }
  {BLOCK_COMMENT}             { return BLOCK_COMMENT; }

  [^:}\],\n/\"]               { yypushback(1); yybegin(PART); }

  [^]                         { return BAD_CHARACTER; }

}

<PART> {
    [^:}\],\n/\s]+       { yybegin(YYINITIAL); return STRING_PART; }
    <<EOF>>            { yybegin(YYINITIAL); return STRING_PART; }
}

