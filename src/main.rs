mod lexer;
use lexer::Lexer;

#[derive(Debug, Copy, Clone, PartialEq)]
enum TokenType {
    VariableLiteral,
    
    Plus,
    Equal,
    
    OpenParan,
    ClosenParan,
    Semicolon
}

use uroboros::{gram, grammar::*, parser::{ll_grammar_parser::*, Parser}, sym};

fn main() {
    let rules = vec![
        (r"[A-Za-z]+",              TokenType::VariableLiteral),

        (r"\+",                     TokenType::Plus),
        (r"\=",                     TokenType::Equal),

        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        (r"\;",                     TokenType::Semicolon),
    ];

    let grammar: Grammar<TokenType> = gram![
        ("expr" => ("term", TokenType::Equal, "term")),
        ("term" => ("term", TokenType::Plus, "num") | ("num")),
        ("num" => (TokenType::VariableLiteral))
    ].remove_left_recursion();

    grammar.debug_log();

    let tokens: Vec<(TokenType, String)> = 
        Lexer::new(rules, "x = x;".to_string())
        .unwrap()
        .lex()
        .unwrap();
   
    let result = LLGrammarParser::new(grammar, sym!("expr")).parse(&tokens);

    println!("{:?}", result);

    match result.unwrap() {
        Some(tree) => (*tree).debug_log(),
        None => todo!(),
    }    
}

