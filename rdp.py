import re

class Tokenizer:
    def __init__(self, input_string):
        self.tokens = re.findall(r'\(|\)|\+|=|x|y|;', input_string)
        self.position = 0

    def get_next_token(self):
        if self.position < len(self.tokens):
            token = self.tokens[self.position]
            self.position += 1
            return token
        return None

    def peek_next_token(self):
        if self.position < len(self.tokens):
            return self.tokens[self.position]
        return None

class Parser:
    def __init__(self, tokenizer):
        self.tokenizer = tokenizer

    def parse_expressions(self):
        expr = self.parse_expression()
        if self.tokenizer.get_next_token() != ';':
            raise SyntaxError("Expected ';' at the end of the expression")
        return expr

    def parse_expression(self):
        left_term = self.parse_term()
        if self.tokenizer.get_next_token() != '=':
            raise SyntaxError("Expected '=' in the expression")
        right_term = self.parse_term()
        return ('expression', left_term, '=', right_term)

    def parse_term(self):
        factor = self.parse_factor()
        term_tail = self.parse_term_tail()
        return ('term', factor, term_tail)

    def parse_term_tail(self):
        if self.tokenizer.peek_next_token() == '+':
            self.tokenizer.get_next_token()  # consume '+'
            factor = self.parse_factor()
            term_tail = self.parse_term_tail()
            return ('term_tail', '+', factor, term_tail)
        return ('term_tail',)  # epsilon (empty)

    def parse_factor(self):
        token = self.tokenizer.peek_next_token()

        if token == '(':
            self.tokenizer.get_next_token()  # consume '('
            term = self.parse_term()
            if self.tokenizer.get_next_token() != ')':
                raise SyntaxError("Expected ')' after term")
            return ('factor', '(', term, ')')

        token = self.tokenizer.get_next_token()
        if token in ['x', 'y']:
            return ('factor', token)
        else:
            raise SyntaxError(f"Unexpected token: {token}")

# Example usage:
input_string = "x+(y+(x+y))=x+(y+(x+y+y+(x+(y+(x+y)))));"
tokenizer = Tokenizer(input_string)
parser = Parser(tokenizer)
parsed_tree = parser.parse_expressions()
print(parsed_tree)
