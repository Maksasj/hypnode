mod lexer;
use lexer::Lexer;

#[derive(Debug, Copy, Clone, PartialEq)]
enum Token {
    VariableLiteral,
    
    Plus,
    Equal,
    
    OpenParan,
    ClosenParan,
    Semicolon
}

use uroboros::{ gram, grammar::*, parser::{llgparser::*, Parser}, sym };

fn main() {
    let rules = vec![
        (r"[A-Za-z]+", Token::VariableLiteral),
        (r"\+",        Token::Plus),
        (r"\=",        Token::Equal),
        (r"\(",        Token::OpenParan),
        (r"\)",        Token::ClosenParan),
        (r"\;",        Token::Semicolon),
    ];

    let grammar: Grammar<Token> = gram![
        ("expr" => ("term", Token::Equal, "term")),
        ("term" => ("term", Token::Plus, Token::VariableLiteral) | (Token::VariableLiteral))
    ].remove_left_recursion();

    grammar.debug_log();

    let tokens: Vec<Token> = 
        Lexer::new(rules, "x = x + x + x;".to_string())
        .unwrap()
        .lex()
        .unwrap()
        .iter()
        .map(|x| x.0)
        .collect();

    println!("{:?}\n", tokens);

    let result = LLGParser::new(grammar, sym!("expr")).parse(&tokens);

    println!("{:?}\n", result);

    match result.unwrap().tree {
        Some(tree) => (*tree).debug_log(),
        None => todo!(),
    }    
}

