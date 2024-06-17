use regex::Regex;

pub struct Lexer<T : Clone> {
    rules: Vec<(Regex, T)>,
    input: String,
    position: usize
}

impl<T : Clone> Lexer<T> {
    pub fn new(input_rules: Vec<(&str, T)>, input: String) -> Result<Self, String> {
        Ok(Lexer {
            rules: input_rules.iter().map(|x| (Regex::new(x.0).unwrap(), x.1.clone())).collect(), 
            input: input.clone(),
            position: 0
        })
    } 

    fn skip_whitespace(&mut self) {
        let mut i = self.input[self.position..].chars();

        while let Some(ch) = i.next()     {
            match ch {
                ch if ch.is_whitespace() => {
                    self.position += 1;
                    continue
                },
                _ => break
            }
        }
    }

    fn next_token(&mut self) -> Result<Option<(T, String)>, (String, String)> {
        self.skip_whitespace();

        if self.position >= self.input.len() {
            return Ok(None);
        }

        let remaining_input = &self.input[self.position..];

        for (regex, token_type) in &self.rules {
            if let Some(mat) = regex.find(&remaining_input) {
                if mat.start() == 0 {
                    self.position += mat.end();

                    return Ok(Some((token_type.clone(), mat.as_str().to_string())));
                }
            }
        }

        return Err(("Unknown token".to_string(), remaining_input.to_string()));
    }

    pub fn lex(&mut self) -> Result<Vec<(T, String)>, (String, String)> {
        let mut tokens: Vec<(T, String)> = vec![];

        loop {
            let t = self.next_token();

            match t {
                Ok(Some(token)) => tokens.push(token),  // Token
                Ok(None) => break,                      // End of file
                Err(error) => return Err(error)         // Unknown token
            }
        }
        
        return Ok(tokens);
    }
}
