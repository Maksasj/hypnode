use std::fs;
use std::error::Error;

mod lexer;
use lexer::Lexer;

#[derive(Debug, Clone)]
enum TokenType {
    Word,

    NumberLiteral,
    StringLiteral,
    
    Plus,
    
    Equal,
    Less,
    More,

    Comma,
    
    Minus,
    OpenParan,
    ClosenParan,

    OpenBracket,
    ClosenBracket,

    Semicolon
}

fn main() -> Result<(), Box<dyn Error>> {
    let rules = vec![
        (r"[A-Za-z]+",              TokenType::Word),
        (r"[0-9]+",                 TokenType::NumberLiteral),
        ("\"([^\"\\\\]|\\\\.)*\"",  TokenType::StringLiteral),
        (r"\+",                     TokenType::Plus),
        (r"\=",                     TokenType::Equal),
        
        (r"<",                      TokenType::Less),
        (r">",                      TokenType::More),
        
        (r"\-",                     TokenType::Minus),
        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        
        (r"\;",                     TokenType::Semicolon),
        (r"\,",                     TokenType::Comma),
        (r"\{",                     TokenType::OpenBracket),
        (r"\}",                     TokenType::ClosenBracket),
    ];

    let input: String = fs::read_to_string("test.txt")?;
    let tokens = Lexer::new(rules, input)?.lex();

    println!("{:?}", tokens);

    Ok(())
}

