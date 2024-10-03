package gen;

import java_cup.runtime.*;

%%

%class Scanner
%unicode
%cup
%line
%column

%{
    StringBuffer string = new StringBuffer();

    private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

WHITESPACE = [ \t\n\r\n]+
COMMENT = #[^\r\n]*(\r|\n|\r\n)?
IDENTIFIER = [A-Za-z_$][\w]*

%state DQ_STRING_LITERAL

%%

<YYINITIAL> {
    // keywords
    "let" { return symbol(sym.LET, yytext()); }
    "type" { return symbol(sym.TYPE, yytext()); }
    "node" { return symbol(sym.NODE, yytext()); }
    "@optional" { return symbol(sym.OPTIONAL, yytext()); }
    "@required" { return symbol(sym.REQUIRED, yytext()); }
    "@trigger" { return symbol(sym.TRIGGER, yytext()); }
    "@export" { return symbol(sym.EXPORT, yytext()); }
    "@import" { return symbol(sym.IMPORT, yytext()); }

    // separators
    \; { return symbol(sym.SEMI, yytext()); }
    \( { return symbol(sym.L_PAREN, yytext()); }
    \) { return symbol(sym.R_PAREN, yytext()); }
    \{ { return symbol(sym.L_CURLY_PAREN, yytext()); }
    \} { return symbol(sym.R_CURLY_PAREN, yytext()); }
    \[ { return symbol(sym.L_SQUARE_PAREN, yytext()); }
    \] { return symbol(sym.R_SQUARE_PAREN, yytext()); }
    \, { return symbol(sym.COMMA, yytext()); }
    \. { return symbol(sym.DOT, yytext()); }

    // operators
    \: { return symbol(sym.COLON, yytext()); }
    \= { return symbol(sym.EQUAL, yytext()); }
    \<\- { return symbol(sym.LEFT_ARROW, yytext()); }
    \=\> { return symbol(sym.BOLD_RIGHT_ARROW, yytext()); }

    // literals
    (true|false) { return symbol(sym.BOOLEAN_LITERAL, yytext()); }
    '.' { return symbol(sym.CHARACTER_LITERAL, yytext()); }
    [+-]?(0|[1-9][0-9]*)(\.[0-9]+)? { return symbol(sym.NUMBER_LITERAL, yytext()); }
    \" { string.setLength(0); yybegin(DQ_STRING_LITERAL); }

    {IDENTIFIER} { return symbol(sym.IDENTIFIER, yytext()); }
    {COMMENT} { return symbol(sym.SINGLE_LINE_COMMENT, yytext()); }

    {WHITESPACE} {}
}

// double quotes string literal
<DQ_STRING_LITERAL> {
    [^\"\\]+  { string.append(yytext()); }
    \\\"      { string.append('"'); }
    \\\\      { string.append('\\'); }
    \\n       { string.append('\n'); }
    \\r       { string.append('\r'); }
    \\t       { string.append('\t'); }
    \"        { yybegin(YYINITIAL); return symbol(sym.STRING_LITERAL, string.toString()); }
    \n|\r     { System.err.println("Unterminated string literal"); }
    \\.       { System.err.println("Invalid escape sequence \"" + yytext() + "\""); }
}

.  { System.out.println("Error: " + yytext()); }
