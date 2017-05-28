package com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.bitmapFont.BitmapFontElementTypes.*;

%%

%{
  public _BitmapFontLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _BitmapFontLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

EOL=\R
WHITE_SPACE=[ \t]+
DOUBLE_QUOTED_STRING=\"([^\\\"\r\n]|\\[^\r\n])*\"
SINGLE_QUOTED_STRING='([^\\'\r\n]|\\[^\r\n])*'
UNQUOTED_STRING=[^\s\n=]+

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "="                         { return EQUALS; }
  ","                         { return COMMA; }

  {EOL}                       { return EOL; }
  {WHITE_SPACE}               { return WHITE_SPACE; }
  {DOUBLE_QUOTED_STRING}      { return DOUBLE_QUOTED_STRING; }
  {SINGLE_QUOTED_STRING}      { return SINGLE_QUOTED_STRING; }
  {UNQUOTED_STRING}           { return UNQUOTED_STRING; }

}

[^] { return BAD_CHARACTER; }
