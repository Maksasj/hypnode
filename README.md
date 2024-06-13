# hypnode 🌀 

hypnode - node based esoteric programming language

## Overview

### Simple way
A -> A $\alpha$ | $\beta$ <br> 

A -> $\beta$ A' <br>
A' -> $\alpha$ A' | $\epsilon$

### Simple way
expr -> expr + num | num
num -> 'x' | 'y'

expr -> num expr'
expr' -> + num expr' | e

A -> $\beta$ A' <br>
A' -> $\alpha$ A' | $\epsilon$


### More comples case

A -> A $\alpha_1$ | A $\alpha_2$ | A $\alpha_3$ | ... | $\beta_1$ | $\beta_1$ | $\beta_3$ <br> 

A -> $\beta_1$ A' | $\beta_2$ A' | $\beta_3$ A' | ... <br>
A' -> $\alpha_1$ A' | $\alpha_2$ A' | $\alpha_3$ A' | ... | $\epsilon$

## License
hypnode is free, open source programming language. All code in this repository is licensed under
- MIT License ([LICENSE.md](https://github.com/Maksasj/hypnode/blob/master/LICENSE.md) or https://opensource.org/license/mit/)