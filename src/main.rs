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
    // Multiplication,
    // Minus,

    Equal,
    
    OpenParan,
    ClosenParan,
    Semicolon
}

struct LLParser {
    head: usize
}

type Result<T> = result::Result<T, ()>;

impl LLParser {
    fn new() -> Self {
        LLParser {
            head: 0
        }
    }

    fn parse(&mut self, tokens: &Vec<(TokenType, String)>, grammar: &Vec<Production<TokenType>>, entry: &GrammaSymbols<TokenType>) -> Result<()> {
        println!("-> {:?}", entry);

        match entry {
            GrammaSymbols::<TokenType>::NonTerminal(_) => {
                for prod in grammar.iter() {
                    println!("prod -> {:?} {:?}", entry.clone(), prod.left);

                    if prod.left != entry.clone() { continue; }
                    
                    println!("expand prod");

                    let backup: usize = self.head;

                    let mut suc: bool = false;

                    for right in prod.right.0.iter() {
                        if self.parse(tokens, grammar, &right.clone()).is_err() {
                            self.head = backup;
                            println!("Broke !");
                            suc = false;
                            break;
                        } else {
                            suc = true;
                        }
                    }
                    
                    if !suc {
                        let backup: usize = self.head;
                        for right in prod.right.1.iter() {
                            if self.parse(tokens, grammar, &right.clone()).is_err() {
                                self.head = backup;
                                suc = false;
                                break;
                            } else {
                                suc = true;
                            }
                        }
                    } 
                   
                    if suc {
                        return Ok(());
                    }
                    
                    return Err(());
                }

                return Err(())
            },
            GrammaSymbols::<TokenType>::Terminal(token) => {
                println!("token {:?}", token);
                
                if self.head >= tokens.len() {
                    return Err(())
                }

                println!("token {:?} {:?}", token, tokens[self.head].0);
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

/*
type ParseResult<T> = Result<(T, String), String>;

trait Parser {
    fn parse(&self, slice: &[TokenType]) -> Result<(), String>;
}


struct OrParser;

impl Parser for OrParser {
    fn parse(&self, slice: &[TokenType]) -> Result<(), String> {
        todo!()
    }
}

struct TokenParser(TokenType);

impl Parser for TokenParser {
    fn parse(&self, slice: &[TokenType]) -> Result<(), String> {
        todo!()
    }
}

struct ItemsSet(Vec<Production<TokenType>>);
 */

// Todo check for depth recursion
fn remove_left_recursion(grammar: Vec<Production<TokenType>>) -> Vec<Production<TokenType>> {
    let mut new: Vec<Production<TokenType>> = vec![];

    for prod in grammar.iter() {
        if prod.left != prod.right.0[0] {
            new.push(prod.clone());
            continue;
        }
        
        let mut a_star_name: String = prod.left.get_string().to_string();
        a_star_name.push('\'');

        let a_start_non_term =  GrammaSymbols::NonTerminal(a_star_name.clone());

        let mut a_star_right = Vec::from(&prod.right.0[1..prod.right.0.len()]);
        a_star_right.push(a_start_non_term.clone());  

        let mut a_right = prod.right.1.clone();
        a_right.push(a_start_non_term);

        new.push(Production {
            left: prod.left.clone(),
            right: ( a_right , vec![] )
        });
        
        new.push(Production {
            left: GrammaSymbols::NonTerminal(a_star_name),
            right: ( a_star_right, vec![ GrammaSymbols::Sigma ] )
        });
    }

    new
}

fn print_grammar(grammar: &Vec<Production<TokenType>>) {
    for prod in grammar.iter() {
        print!("{:?} => ", prod.left);

        print!("{:?}, ", prod.right.0);

        print!(" | ");

        print!("{:?}, ", prod.right.1);

        print!("\n");
    }
}

fn main() {
    // expr -> term = term ;
    // term -> term + num | num 


    let grammar0 = vec![
        Production {
            left: sym!("expr"),
            right: ( 
                vec![ sym!("term"), sym!(TokenType::Equal), sym!("term"), sym!(TokenType::Semicolon) ],
                vec![ ]
            )
        },
        Production {
            left: sym!("term"),
            right: ( 
                vec![ sym!("term"), sym!(TokenType::Plus), sym!("num") ], 
                vec![ sym!("num") ] 
            )
        },
        Production {
            left: sym!("num"),
            right: ( 
                vec![ sym!(TokenType::VariableLiteral) ], 
                vec![ ] 
            )
        }
    ];
    /*
    let grammar0 = vec![
        Production {
            left: sym!("expr"),
            right: ( 
                vec![ sym!("expr"), sym!(TokenType::Plus), sym!("num"), ],
                vec![ sym!("num") ]
            )
        },
        Production {
            left: sym!("num"),
            right: ( 
                vec![ sym!(TokenType::VariableLiteral) ], 
                vec![] 
            )
        }
    ];
     */

    println!("Grammar 0");
    print_grammar(&grammar0);
    println!("");

    let rules = vec![
        (r"[A-Za-z]+",              TokenType::VariableLiteral),
        (r"\+",                     TokenType::Plus),
        // (r"\-",                     TokenType::Minus),

        (r"\=",                     TokenType::Equal),

        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        (r"\;",                     TokenType::Semicolon),
    ];

    


    // let g0 = make_productions![
    //     ("expression"  => ("term", TokenType::Equal, "term")),
    //     ("term"        => ("term_prefix", "term_sufix")),
    //     ("term_sufix"  => (TokenType::Plus, "term_prefix", "term_sufix")),
    //     ("term_prefix" => (TokenType::OpenParan, "term", TokenType::ClosenParan))
    // ];

    // expr -> expr + num | num
    // num -> 'x' | 'y' 

    let grammar1 = remove_left_recursion(grammar0);

    println!("Grammar 1");
    print_grammar(&grammar1);
    println!("");

    let tokens: Vec<(TokenType, String)> = Lexer::new(rules, "x + x = x + x + x;".to_string()).unwrap().lex().unwrap();

    println!("{:?}", tokens);

    println!("{:?}", LLParser::new().parse(&tokens, &grammar1, &sym!("expr")));    


    /*
    let rules = vec![
        (r"[A-Za-z]+",              TokenType::VariableLiteral),
        (r"\+",                     TokenType::Plus),
        // (r"\-",                     TokenType::Minus),

        (r"\=",                     TokenType::Equal),

        (r"\(",                     TokenType::OpenParan),
        (r"\)",                     TokenType::ClosenParan),
        (r"\;",                     TokenType::Semicolon),
    ];

    let tokens: Vec<(TokenType, String)> = Lexer::new(rules, "()".to_string()).unwrap().lex().unwrap().into_iter().map(|x| x.0).collect();
    // println!("{:?}", tokens);

    let s: TokenParser = TokenParser(TokenType::OpenParan);

    println!("{:?}", s.parse(&tokens));
     */
    /*
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
    println!("{:?}", LLParser::new().parse_expression(&tokens));    
    */
}

