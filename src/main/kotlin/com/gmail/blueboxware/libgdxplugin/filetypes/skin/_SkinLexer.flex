package com.gmail.blueboxware.libgdxplugin.filetypes.skin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes.*;

%%

%{
  public _SkinLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SkinLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*([^*]|\*+[^*/])*(\*+"/")?
DOUBLE_QUOTED_STRING=\"([^\\\"\r\n]|\\[^\n\r])*\"?
UNQUOTED_STRING=([^\\\":,{}\[\]/\r\n\t ]|\\[^\n\r])([^\\:}\]/,\r\n\t ]|"/"[^/*:]|\\[^\n\r])*

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "{"                         { return L_CURLY; }
  "}"                         { return R_CURLY; }
  "["                         { return L_BRACKET; }
  "]"                         { return R_BRACKET; }
  ","                         { return COMMA; }
  ":"                         { return COLON; }

  {LINE_COMMENT}              { return LINE_COMMENT; }
  {BLOCK_COMMENT}             { return BLOCK_COMMENT; }
  {DOUBLE_QUOTED_STRING}      { return DOUBLE_QUOTED_STRING; }
  {UNQUOTED_STRING}           { return UNQUOTED_STRING; }

}

[^] { return BAD_CHARACTER; }
