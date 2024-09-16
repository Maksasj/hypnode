# hypnode 🌀 

node based programming language

> **hypnode** is probably a new javascript framework, but i am not sure

Cool looking widgets
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Maksasj/hypnode/test.yml?logo=github&label=build)
![GitHub License](https://img.shields.io/github/license/Maksasj/hypnode)
![GitHub Repo stars](https://img.shields.io/github/stars/Maksasj/hypnode?style=flat)

### Links
1. Source code available at [github.com/Maksasj/hypnode](https://github.com/Maksasj/hypnode)

## Architecture
*Todo*

## Features
*Todo*

## Build
*Todo*

## Language

### BNF (Backus–Naur form)
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

<type_name> ::= ...
<field_name> ::= ...
<node_name> ::= ...
```

## License
hypnode is free, open source programming language. All code in this repository is licensed under
- MIT License ([LICENSE.md](https://github.com/Maksasj/hypnode/blob/master/LICENSE.md) or https://opensource.org/license/mit/)

Copyright 2024 © Maksim Jaroslavcevas, Aleksandras Šukelovič 