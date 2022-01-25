package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2ElementTypes.*;

%%

%{
  public _Atlas2Lexer() {
    this((java.io.Reader)null);
  }

  boolean nonEmpty = false;
%}

%public
%class _Atlas2Lexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=[ \t]+

%state KEY_SEPARATOR, _VALUE, LINE_SEPARATOR

%%
<YYINITIAL> {
    {EOL}         {
                      yypushback(1);
                      yybegin(LINE_SEPARATOR);
                      return nonEmpty ? THEADER : EMPTY_LINE;
                  }
    ":"           { yypushback(1); yybegin(KEY_SEPARATOR); return TKEY; }
    [^ \t]        { nonEmpty = true; }
    [^]           {  }
}

<KEY_SEPARATOR> {
    ":"           { yybegin(_VALUE); return COLON; }
}

<_VALUE> {
    ","           { return COMMA; }
    [^,\n\r]+     { return TVALUE; }
    {EOL}         { nonEmpty = false; yybegin(YYINITIAL); return WHITE_SPACE; }
}

<LINE_SEPARATOR> {
    {EOL}+        { nonEmpty = false; yybegin(YYINITIAL); return WHITE_SPACE; }
}

