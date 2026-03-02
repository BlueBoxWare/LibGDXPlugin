package com.gmail.blueboxware.libgdxplugin.filetypes.tree;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

%%

%{
    private boolean valueStarted = false;
%}

%class TreeLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

TINDENT=[ \t]+
NEWLINE=("\n" | "\r\n")
SPACE=[ \t\r]+
ID = [a-zA-Z_][a-zA-Z_0-9]*
TASKNAME = {ID} ("." {ID})* ("$" {ID})*
SUBTREEREFERENCE = "$" {ID}
ATTRNAME = {ID}
NUMBER = [0-9A-Fa-f._pPxXbBlL+InityN-]+
COMMENT = "#" [^\n\r]*
// TODO: Handle missing quote
DOUBLE_QUOTED_STRING=\"([^\\\"]|\\.)*\"
NOT_AN_ATTRIBUTE=[ \t]*[^: \t]

%state LINE
%state ATTRIBUTES
%state VALUE

%%

//			ws = [ \r\t];
//			nl = ('\n' | '\r\n') @newLine;
//			id = [a-zA-Z_] [a-zA-Z_0-9]*;
//			idBegin = [a-zA-Z_] >savePos [a-zA-Z_0-9]*;
//			comment = '#' /[^\r\n]*/ >savePos %comment;
//			indent = [ \t] @indent;
//			attrName = idBegin '?'? %attrName;
//			attrValue = '"' @quotedChars %attrValue '"' | ^[#:"()\r\n\t ] >unquotedChars %attrValue;
//			attribute = attrName ws* ':' ws* attrValue;
//			attributes = (ws+ attribute)+;
//			taskName = idBegin ('.' id)* ('$' id)* '?'? %{isSubtreeRef = false;} %taskName;
//			subtreeRef = '$' idBegin '?'? %{isSubtreeRef = true;} %taskName;
//			task = taskName attributes? | subtreeRef;  # either a task name with attributes or a subtree reference
//			guard = '(' @{isGuard = true;} ws* task? ws* ')' @{isGuard = false;};
//			guardableTask = (guard ws*)* task;
//			line = indent* guardableTask? ws* <: comment? %endLine;
//			main := line (nl line)** nl?;

<YYINITIAL> {

    {TINDENT}       { return TreeElementTypes.TINDENT; }

    [^]             { yypushback(1); yybegin(LINE); }

}

<LINE> {
    "import"/{NOT_AN_ATTRIBUTE}        { yybegin(ATTRIBUTES); return TreeElementTypes.TIMPORT; }
    "root"/{NOT_AN_ATTRIBUTE}          { yybegin(ATTRIBUTES); return TreeElementTypes.TROOT; }
    "subtree"/{NOT_AN_ATTRIBUTE}       { yybegin(ATTRIBUTES); return TreeElementTypes.TSUBTREE; }

    {TASKNAME}      {
        if (zzInput == YYEOF) {
            {
                switch (yytext().toString()) {
                    case "import": return TreeElementTypes.TIMPORT;
                    case "root": return TreeElementTypes.TROOT;
                    case "subtree": return TreeElementTypes.TSUBTREE;
                    default: return TreeElementTypes.TASK_NAME;
                }
            }
        } else {
            { yypushback(1); yybegin(ATTRIBUTES); }
        }
    }
    {TASKNAME}/{NOT_AN_ATTRIBUTE}      { yybegin(ATTRIBUTES); return TreeElementTypes.TASK_NAME; }

    {SUBTREEREFERENCE}    { return TreeElementTypes.SUBTREEREFERENCE; }

    "("             { return TreeElementTypes.LPAREN; }
    ")"             { return TreeElementTypes.RPAREN; }
    "?"             { return TreeElementTypes.QUESTION_MARK; }

    {COMMENT}       { return TreeElementTypes.COMMENT; }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    {SPACE}         { return TokenType.WHITE_SPACE; }

    [^]             { yypushback(1); yybegin(ATTRIBUTES); }

}

<ATTRIBUTES> {

    {ATTRNAME}      { return TreeElementTypes.ATTRNAME; }
    "?"             { return TreeElementTypes.QUESTION_MARK; }
    ":"             { valueStarted = false; yybegin(VALUE); return TreeElementTypes.COLON; }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    [#)]            { yybegin(YYINITIAL); yypushback(1);}

    {SPACE}         { return TokenType.WHITE_SPACE; }

    [^]             { return TokenType.BAD_CHARACTER; }
}

<VALUE> {

    "true"          { valueStarted = true; return TreeElementTypes.TRUE; }
    "false"         { valueStarted = true; return TreeElementTypes.FALSE; }
    "null"          { valueStarted = true; return TreeElementTypes.NULL; }

    {NUMBER}        { valueStarted = true; return TreeElementTypes.NUMBER; }
    {DOUBLE_QUOTED_STRING} { valueStarted = true; return TreeElementTypes.STRING; }

    [#:\"()]        { yybegin(ATTRIBUTES); yypushback(1); }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    {SPACE}         {
        if (valueStarted) {
            yybegin(ATTRIBUTES); yypushback(1);
        } else {
            return TokenType.WHITE_SPACE;
        }
    }

    [^]             { return TokenType.BAD_CHARACTER; }

}
