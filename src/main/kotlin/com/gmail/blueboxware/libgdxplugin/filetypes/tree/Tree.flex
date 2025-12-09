package com.gmail.blueboxware.libgdxplugin.filetypes.tree;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

%%

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
NUMBER = [0-9A-Fa-f._pPxXbBlL+-InityN]+
COMMENT = "#" [^\n\r]*
DOUBLE_QUOTED_STRING=\"([^\\\"]|\\.)*\"

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
    "import"        { yybegin(ATTRIBUTES); return TreeElementTypes.TIMPORT; }
    "root"          { yybegin(ATTRIBUTES); return TreeElementTypes.TROOT; }
    "subtree"       { yybegin(ATTRIBUTES); return TreeElementTypes.TSUBTREE; }

    {TASKNAME}      { yybegin(ATTRIBUTES); return TreeElementTypes.TASK_NAME; }
    {SUBTREEREFERENCE}    { return TreeElementTypes.SUBTREEREFERENCE; }

    "("             { return TreeElementTypes.LPAREN; }
    ")"             { return TreeElementTypes.RPAREN; }
    "?"             { return TreeElementTypes.QUESTION_MARK; }

    {COMMENT}       { return TreeElementTypes.COMMENT; }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    {SPACE}         { return TokenType.WHITE_SPACE; }

    [^]             { return TokenType.BAD_CHARACTER; }

}

<ATTRIBUTES> {

    {ATTRNAME}      { return TreeElementTypes.ATTRNAME; }
    "?"             { return TreeElementTypes.QUESTION_MARK; }
    ":"             { yybegin(VALUE); return TreeElementTypes.COLON; }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    [#)]            { yybegin(YYINITIAL); yypushback(1);}

    {SPACE}         { return TokenType.WHITE_SPACE; }

    [^]             { return TokenType.BAD_CHARACTER; }
}

<VALUE> {

    "true"          { yybegin(ATTRIBUTES); return TreeElementTypes.TRUE; }
    "false"         { yybegin(ATTRIBUTES); return TreeElementTypes.FALSE; }
    "null"          { yybegin(ATTRIBUTES); return TreeElementTypes.NULL; }

    {NUMBER}        { yybegin(ATTRIBUTES); return TreeElementTypes.NUMBER; }
    {DOUBLE_QUOTED_STRING} { yybegin(ATTRIBUTES); return TreeElementTypes.STRING; }

    [#:\"()]        { yybegin(ATTRIBUTES); yypushback(1); }

    {NEWLINE}       { yybegin(YYINITIAL); return TreeElementTypes.EOL; }
    <<EOF>>         { yybegin(YYINITIAL); return TreeElementTypes.EOL; }

    {SPACE}         { return TokenType.WHITE_SPACE; }

    [^]             { return TokenType.BAD_CHARACTER; }

}
