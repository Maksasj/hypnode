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

nl		=  \n|\r|\r\n

%%

// Todo

{nl}|" " 	{;}

.		{System.out.println("Error:" + yytext());}

