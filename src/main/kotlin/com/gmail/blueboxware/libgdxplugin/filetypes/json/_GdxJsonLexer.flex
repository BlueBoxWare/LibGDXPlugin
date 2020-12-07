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

    StringBuffer string = new StringBuffer();
%}

%public
%class _GdxJsonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

WHITE_SPACE=\s+

LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*([^*]|\*+[^*/])*(\*+"/")?
ANY_CHAR=.

%state STRING
%state USTRING

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

  "\""                        { yybegin(STRING); }

  {LINE_COMMENT}              { return LINE_COMMENT; }
  {BLOCK_COMMENT}             { return BLOCK_COMMENT; }

  {ANY_CHAR}                  {  yypushback(1); string.setLength(0); yybegin(USTRING); }

}


<STRING> {
    "\\"       {}
    "\\\""     {}
    "\\\\"     {}
    "\""       { yybegin(YYINITIAL); return DOUBLE_QUOTED_STRING; }
    [^\"\\]+   {}
}

<USTRING> {
    ([^}\],:\r\n/]|\/[^*/])+  {
          string.setLength(0);
          string.append(yytext());
          while (string.length() > 0) {
              Character c = string.substring(string.length() - 1).charAt(0);
              if (Character.isSpaceChar(c)) {
                    yypushback(1);
                    string.setLength(string.length() - 1);
              } else {
                  break;
              }
          }
          yybegin(YYINITIAL);
          try {
              Double.parseDouble(string.toString());
              return NUMBER;
              } catch (NumberFormatException e) {}
          try {
              Long.parseLong(string.toString());
              return NUMBER;
              } catch (NumberFormatException e) {}
          return UNQUOTED_STRING;

      }
      [^]       {
          throw new Error("Illegal character <"+ yytext()+">");
      }
}


