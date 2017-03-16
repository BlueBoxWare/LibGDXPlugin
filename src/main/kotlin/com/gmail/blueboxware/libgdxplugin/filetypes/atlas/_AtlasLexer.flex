package com.gmail.blueboxware.libgdxplugin.filetypes.atlas;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasElementTypes.*;

%%

%{
  public _AtlasLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _AtlasLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

EOL=\R
WHITE_SPACE=[ \t]+
STRING=[^,:\n\s]+

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  ":"                { return COLON; }
  ","                { return COMMA; }

  {EOL}              { return EOL; }
  {WHITE_SPACE}      { return WHITE_SPACE; }
  {STRING}           { return STRING; }

}

[^] { return BAD_CHARACTER; }
