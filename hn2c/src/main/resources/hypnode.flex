package gen;

import java_cup.runtime.*;

%%

%class Scanner
%unicode
%cup
%line
%column

%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline + 1, yycolumn + 1);
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline + 1, yycolumn + 1, value);
  }
%}

primitive_types = (i8|i16|i32|i64|u8|u16|u32|u64|f32|f64|string|bool)
whitespace = [ \t\n\r\n]+

%%

<YYINITIAL> {
    // ignore whitespace
    {whitespace} {}

    // type keywords
    {primitive_types} { return symbol(sym.PRIMITIVE_TYPE, yytext()); }

}

.		{System.out.println("Error:" + yytext());}
