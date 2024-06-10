use std::fs;
use std::result;

mod lexer;
use lexer::Lexer;

#[macro_use]
mod bnf;
use bnf::*;

use regex::Error;

#[derive(Debug, Copy, Clone)]
enum TokenType {
    VariableLiteral,
    
    Plus,
    // Multiplication,
    // Minus,

    Equal,
    
    OpenParan,
    ClosenParan,
    Semicolon
}

struct Parser {
    head: usize
}

type Result<T> = result::Result<T, ()>;

impl Parser {
    fn new() -> Self {
        Parser {
            head: 0
        }
    }

    fn sigma(&mut self, tokens: &Vec<(TokenType, String)>) -> Result<()> {
        return Ok(());
    }

    fn accept(&mut self, tokens: &Vec<(TokenType, String)>, symbol: char) -> Result<()> {
        if self.head >= tokens.len() {
            return Err(())
        }

        let cur: char = tokens[self.head].1.chars().next().unwrap();

        if cur == symbol {
            self.head += 1;
            return Ok(());
        }

        Err(())
    }

    fn parse_term_sufix(&mut self, tokens: &Vec<(TokenType, String)>) -> Result<()> { 
        let backup: usize = self.head;

        if self.accept(tokens, '+').is_ok() && self.parse_term_prefix(tokens).is_ok() && self.parse_term_sufix(tokens).is_ok() {
            return Ok(());
        } else {
            self.head = backup;
        }

        if self.sigma(tokens).is_ok() {
            return Ok(());
        } else {
            self.head = backup;
        }

        Err(())
    }

    fn parse_term_prefix(&mut self, tokens: &Vec<(TokenType, String)>) -> Result<()> {
        let backup: usize = self.head;

        if self.accept(tokens, '(').is_ok() && self.parse_term(tokens).is_ok() && self.accept(tokens, ')').is_ok() {
            return Ok(());
        } else {
            self.head = backup;
        }

        if self.accept(tokens, 'x').is_ok() {
            return Ok(());
        } else {
            self.head = backup;
        }
        
        if self.accept(tokens, 'y').is_ok() {
            return Ok(());
        } else {
            self.head = backup;
        }

        Err(())
    }

    fn parse_term(&mut self, tokens: &Vec<(TokenType, String)>) -> Result<()> {
        let backup: usize = self.head;

        if self.parse_term_prefix(tokens).is_ok() && self.parse_term_sufix(tokens).is_ok() {
            return Ok(())
        } else {
            self.head = backup;
        }

        Err(())
    }

    fn parse_expression(&mut self, tokens: &Vec<(TokenType, String)>) -> Result<()> {
        let backup: usize = self.head;

        if self.parse_term(tokens).is_ok() && self.accept(tokens, '=').is_ok() && self.parse_term(tokens).is_ok() {
            return Ok(())
        } else {
            self.head = backup;
        }

        Err(())
    }
}

fn main() {
    let rules = vec![
        (r"[A-Za-z]+",              TokenType::VariableLiteral),
        (r"\+",                     TokenType::Plus),
        // (r"\-",                     TokenType::Minus),

        (r"\=",                     TokenType::Equal),

        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        (r"\;",                     TokenType::Semicolon),
    ];

    let grammar = vec![
        Production {
            left: sym!("expression"),
            right: vec![ 
                vec![ sym!("term"), sym!(TokenType::Equal), sym!("term"), ]
            ]
        },
        Production {
            left: sym!("term"),
            right: vec![ 
                vec![ sym!("term_prefix"), sym!("term_sufix"), ]
            ]
        },
        Production {
            left: sym!("term_sufix"),
            right: vec![ 
                vec![ sym!(TokenType::Plus), sym!("term_prefix"), sym!("term_sufix") ],
                vec![ sigma!() ]
            ]
        },
        Production {
            left: sym!("term_prefix"),
            right: vec![ 
                vec![ sym!(TokenType::OpenParan), sym!("term"), sym!(TokenType::ClosenParan) ],
                vec![ sym!(TokenType::VariableLiteral)]
            ]
        }
    ];

    println!("{:?}\n", grammar);

    // let g0 = make_productions![
    //     ("expression"  => ("term", TokenType::Equal, "term")),
    //     ("term"        => ("term_prefix", "term_sufix")),
    //     ("term_sufix"  => (TokenType::Plus, "term_prefix", "term_sufix")),
    //     ("term_prefix" => (TokenType::OpenParan, "term", TokenType::ClosenParan))
    // ];


    // // # No left recursion
    // // <expressions> ::= <expression> ';'
    // // <expression> ::= <term> '=' <term>
    // // <term> ::= <term_prefix> <term_sufix>
    // // <term_sufix> ::= '+' <term_prefix> <term_sufix> | ε
    // // <term_prefix> ::= '(' <term> ')' | 'x'| 'y'
// 
    // let input: String = fs::read_to_string("test.txt").unwrap();
// 
    let tokens: Vec<(TokenType, String)> = Lexer::new(rules, "((x+(x+y))+(x+y)+(x+y+x))+x+((x+(x+y))+(x+y)+(x+y+x))=((x+y)+(x+y)+(x+y+x))".to_string()).unwrap().lex().unwrap();
    println!("{:?}", tokens);
// 
    println!("{:?}", Parser::new().parse_expression(&tokens));    
}

