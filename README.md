# hypnode 🌀

node based programming language

## BNF (Backus–Naur form)

```bnf
<program> ::= ( <node_definition> | <type_definition> )*

<type_definition> ::= <type_declaration> "=" <type_implementation>
<type_declaration> ::= "type" <type_name>
<type_implementation> ::= "{" ( <field_definition> )* "}" | <type>
<field_definition> ::= <field_name> ":" <type_implementation> ";"

<node_definition> ::= <node_declaration> "=" <node_implementation>
<node_declaration> ::= "node" <node_name>
<node_implementation> ::= "{" ( <statement_definition> )* "}"
<statement_definition> ::= <port_definition> | <node_instance> | <node_connection>
<port_definition> ::= ("input" | "output") <field_name> ":" <type_implementation> ";"
<node_instance> ::= "let" <field_name> ":" <node_name> ";"

<node_connection> ::= <field_access> "<-" <field_access> ";"
<field_access> ::= <field_name> ("." <field_name>)*

<type> ::= <primitive_type> | <type_name>
<primitive_type> ::= "i64" | "i32" | "i16" | "i8" | "u64" | "u32" | "u16" | "u8" | "string" | "f32" | "f64" | "bool"

<type_name> ::= <uppercase_letter> <letter_or_digit>*
<field_name> ::= <lowercase_letter> <letter_or_digit>*
<node_name> ::= <letter> <letter>*

<letter_or_digit> ::= <letter> | <digit>
<letter> ::= <lowercase_letter> | <uppercase_letter>
<lowercase_letter> ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
<uppercase_letter> ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
<digit> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
```

## License

hypnode is free, open source programming language. All code in this repository is licensed under

- MIT License ([LICENSE.md](https://github.com/Maksasj/hypnode/blob/master/LICENSE.md) or https://opensource.org/license/mit/)
