use std::result;

mod lexer;
use lexer::Lexer;

#[macro_use]
mod bnf;
use bnf::*;

#[derive(Debug, Copy, Clone, PartialEq)]
enum TokenType {
    VariableLiteral,
    
    Plus,
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

    fn parse(&mut self, tokens: &Vec<(TokenType, String)>, grammar: &Vec<Production<TokenType>>, entry: &GrammaSymbols<TokenType>) -> Result<()> {
        match entry {
            GrammaSymbols::<TokenType>::NonTerminal(_) => {
                for prod in grammar.iter() {
                    if prod.left != entry.clone() { continue; }

                    let backup: usize = self.head;

                    let mut suc: bool = false;

                    for right in prod.right.iter() {
                        for symbol in right.iter() {
                            if self.parse(tokens, grammar, &symbol.clone()).is_err() {
                                self.head = backup;
                                suc = false;
                                break;
                            } else {
                                suc = true;
                            }
                        }
                    }

                    return match suc {
                        true => Ok(()),
                        false => Err(()),
                    }
                }

                return Err(())
            },
            GrammaSymbols::<TokenType>::Terminal(token) => {
                if self.head >= tokens.len() {
                    return Err(())
                }

                if token.clone() == tokens[self.head].0 {
                    self.head += 1;
                    return Ok(());
                }
        
                return Err(()); 
            },
            sigma!() => {
                return Ok(());
            }
        }
    }

    /*
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
     */
}

fn main() {
    let rules = vec![
        (r"[A-Za-z]+",              TokenType::VariableLiteral),

        (r"\+",                     TokenType::Plus),
        (r"\=",                     TokenType::Equal),

        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        (r"\;",                     TokenType::Semicolon),
    ];

    let grammar: Grammar<TokenType> = vec![
        prod!("expr" => ("term", TokenType::Equal, "term")),
        prod!("term" => ("term", TokenType::Plus, "num") | ("num")),
        prod!("num" => (TokenType::VariableLiteral)),
    ].remove_left_recursion();

    grammar.debug_log();

    let tokens: Vec<(TokenType, String)> = 
        Lexer::new(rules, "x + x = x + x + x;".to_string())
        .unwrap()
        .lex()
        .unwrap();
   
    println!("{:?}", Parser::new().parse(&tokens, &grammar, &sym!("expr")));    
}

